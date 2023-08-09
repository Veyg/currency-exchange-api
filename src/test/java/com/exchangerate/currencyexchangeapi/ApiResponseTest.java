package com.exchangerate.currencyexchangeapi;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.util.concurrent.CompletableFuture;
import io.github.cdimascio.dotenv.Dotenv;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = CurrencyExchangeApiApplication.class)
@AutoConfigureMockMvc
public class ApiResponseTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyExchangeApiClient currencyExchangeApiClient;

    @MockBean
    private DataSource dataSource;

    @Test
    public void testApiResponse() throws Exception {
        // Load environment variables from .env file
        Dotenv dotenv = Dotenv.configure().load();

        // Mock the behavior of CurrencyExchangeApiClient
        Mockito.when(currencyExchangeApiClient.getExchangeRate("USD", "EUR"))
                .thenReturn(CompletableFuture.completedFuture(1.0));

        // Mock the behavior of JdbcTemplate
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        Connection mockConnection = Mockito.mock(Connection.class);
        PreparedStatement mockStatement = Mockito.mock(PreparedStatement.class);

        Mockito.when(dataSource.getConnection()).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockStatement);
        Mockito.when(mockStatement.execute()).thenReturn(true);
        Mockito.when(jdbcTemplate.queryForObject(Mockito.anyString(), Mockito.eq(Integer.class), Mockito.any()))
                .thenReturn(1); // Return a dummy count for testing

        // Create ApiKeyService with the mocked DataSource and JdbcTemplate
        ApiKeyService apiKeyService = new ApiKeyService(dataSource);

        // Define the base currency, target currency, and amount
        String baseCurrency = "USD";
        String targetCurrency = "EUR";
        double amount = 100;

        // Retrieve API key from environment variables
        String apiKey = dotenv.get("API_KEY_TEST");

        // Construct the request URL with the API key as a query parameter
        String requestUrl = "/api/currency/convert/{baseCurrency}/{targetCurrency}/{amount}?apiKey=" + apiKey;
        System.out.println("Request URL: " + requestUrl);

        // Perform the request and expect a successful response
        mockMvc.perform(MockMvcRequestBuilders.get(requestUrl, baseCurrency, targetCurrency, amount))
                .andExpect(status().isOk());
    }

    @Test
    public void testInvalidApiKey() throws Exception {
        // Mock the behavior of JdbcTemplate
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        Connection mockConnection = Mockito.mock(Connection.class);
        PreparedStatement mockStatement = Mockito.mock(PreparedStatement.class);

        Mockito.when(dataSource.getConnection()).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockStatement);
        Mockito.when(mockStatement.execute()).thenReturn(true);
        Mockito.when(jdbcTemplate.queryForObject(Mockito.anyString(), Mockito.eq(Integer.class), Mockito.any()))
                .thenReturn(0); // Return 0 to simulate invalid API key

        // Create ApiKeyService with the mocked DataSource and JdbcTemplate
        ApiKeyService apiKeyService = new ApiKeyService(dataSource);

        // Define the base currency, target currency, and amount
        String baseCurrency = "USD";
        String targetCurrency = "EUR";
        double amount = 100;

        // Construct the request URL with an invalid API key as a query parameter
        String requestUrl = "/api/currency/convert/{baseCurrency}/{targetCurrency}/{amount}?apiKey=invalid_key";

        // Perform the request and expect a bad request response
        mockMvc.perform(MockMvcRequestBuilders.get(requestUrl, baseCurrency, targetCurrency, amount))
                .andExpect(status().isBadRequest());
    }
}
