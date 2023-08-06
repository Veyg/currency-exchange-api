package com.exchangerate.currencyexchangeapi;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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

    @Test
    public void testApiResponse() throws Exception {
        // Load environment variables from .env file
        Dotenv dotenv = Dotenv.configure().load();
        
        // Mock the behavior of CurrencyExchangeApiClient
        Mockito.when(currencyExchangeApiClient.getExchangeRate("USD", "EUR"))
                .thenReturn(CompletableFuture.completedFuture(1.0));
    
        // Define the base currency, target currency, and amount
        String baseCurrency = "USD";
        String targetCurrency = "EUR";
        double amount = 100;
    
        // Retrieve API key from environment variables
        String apiKey = dotenv.get("API_KEY_TEST");
        
        // Print the constructed URL for debugging
        String requestUrl = "/api/currency/convert/{baseCurrency}/{targetCurrency}/{amount}";
        System.out.println("API Key: " + apiKey);
        System.out.println("Request URL: " + requestUrl);

        // Perform the request with the API key and expect a successful response
        mockMvc.perform(MockMvcRequestBuilders.get(requestUrl, baseCurrency, targetCurrency, amount)
                .param("apiKey", apiKey))
                .andExpect(status().isOk());
    }
}
