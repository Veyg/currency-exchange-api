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
public class RateLimitTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private CurrencyExchangeApiClient currencyExchangeApiClient;

    @Test
    public void testRateLimit() throws Exception {
        // Mock the behavior of CurrencyExchangeApiClient
        Mockito.when(currencyExchangeApiClient.getExchangeRate("USD", "EUR"))
                .thenReturn(CompletableFuture.completedFuture(1.0));

        // Define the base currency, target currency and the amount
        String baseCurrency = "USD";
        String targetCurrency = "EUR";
        double amount = 100.0;
        
        // First request should be OK
        mockMvc.perform(MockMvcRequestBuilders.get("/api/currency/convert/{baseCurrency}/{targetCurrency}/{amount}", baseCurrency, targetCurrency, amount))
                .andExpect(status().isOk());
        // Sleep for a certain amount of time to ensure the rate limiting kicks in
        Thread.sleep(6000); // 6000 milliseconds = 6 seconds, more than your limit of 5 seconds for 1 request

        mockMvc.perform(MockMvcRequestBuilders.get("/api/currency/convert/{baseCurrency}/{targetCurrency}/{amount}", baseCurrency, targetCurrency, amount))
                .andExpect(status().isOk());
    }
}
