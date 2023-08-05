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
        // Mock the behavior of CurrencyExchangeApiClient
        Mockito.when(currencyExchangeApiClient.getExchangeRate("USD", "EUR"))
                .thenReturn(CompletableFuture.completedFuture(1.0));

        // Define the base currency, target currency, and amount
        String baseCurrency = "USD";
        String targetCurrency = "EUR";
        double amount = 100.0;
        
        // Get API key from environment variable
        String apiKey = System.getenv("API_KEY_TEST");
        System.out.println("API_KEY_TEST: " + apiKey);

        // Perform the request with the API key and expect a successful response
        mockMvc.perform(MockMvcRequestBuilders.get("/api/currency/convert/{baseCurrency}/{targetCurrency}/{amount}?apiKey={apiKey}", baseCurrency, targetCurrency, amount, apiKey))
                .andExpect(status().isOk());
    }
}
