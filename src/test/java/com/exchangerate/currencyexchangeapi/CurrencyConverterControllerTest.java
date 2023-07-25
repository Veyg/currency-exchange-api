// package com.exchangerate.currencyexchangeapi;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.test.web.servlet.MockMvc;

// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

// @SpringBootTest
// @AutoConfigureMockMvc
// public class CurrencyConverterControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @MockBean
//     private CurrencyExchangeApiClient currencyExchangeApiClient;

//     @Test
//     public void convertCurrency() throws Exception {
//         String baseCurrency = "USD";
//         String targetCurrency = "EUR";
//         double amount = 100.0;

//         when(currencyExchangeApiClient.getExchangeRate(baseCurrency, targetCurrency)).thenReturn(0.82); // assuming 1 USD equals 0.82 EUR

//         mockMvc.perform(get("/api/currency/convert/" + baseCurrency + "/" + targetCurrency + "/" + amount))
//             .andExpect(status().isOk())
//             .andExpect(content().string(String.valueOf(0.82 * amount)));
//     }
// }
