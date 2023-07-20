package com.exchangerate.currencyexchangeapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/currency")
public class CurrencyConverterController {
    private final CurrencyExchangeApiClient currencyExchangeApiClient;
   
    public CurrencyConverterController(CurrencyExchangeApiClient currencyExchangeApiClient) {
        this.currencyExchangeApiClient = currencyExchangeApiClient;
    }

    @GetMapping("/convert/{baseCurrency}/{targetCurrency}/{amount}")
    public ResponseEntity<?> convertCurrency(
        @PathVariable String baseCurrency,
        @PathVariable String targetCurrency,
        @PathVariable double amount)
        {
            try {
                double convertedAmount = currencyExchangeApiClient.getExchangeRate(baseCurrency, targetCurrency) * amount;
                return ResponseEntity.ok(convertedAmount);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            } catch (IllegalStateException e) {
                return ResponseEntity.status(500).body("Error during the conversion request: " + e.getMessage());
            } catch (RuntimeException e) {
                return ResponseEntity.status(500).body("Unexpected error during the currency conversion: " + e.getMessage());
            }
        }
}
