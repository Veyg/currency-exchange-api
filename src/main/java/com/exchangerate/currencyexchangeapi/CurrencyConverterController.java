package com.exchangerate.currencyexchangeapi;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/currency")
public class CurrencyConverterController {
    private final CurrencyExchangeApiClient currencyExchangeApiClient;

    public CurrencyConverterController(CurrencyExchangeApiClient currencyExchangeApiClient) {
        this.currencyExchangeApiClient = currencyExchangeApiClient;
    }

    @GetMapping("/convert/{baseCurrency}/{targetCurrency}/{amount}")
    public CompletableFuture<ResponseEntity<?>> convertCurrency(
        @PathVariable String baseCurrency,
        @PathVariable String targetCurrency,
        @PathVariable double amount)
    {
        return currencyExchangeApiClient.getExchangeRate(baseCurrency, targetCurrency).thenApply(exchangeRate -> {
            try {
                double convertedAmount = exchangeRate * amount;
                return ResponseEntity.ok(convertedAmount);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            } catch (IllegalStateException e) {
                return ResponseEntity.status(500).body("Error during the conversion request: " + e.getMessage());
            } catch (RuntimeException e) {
                return ResponseEntity.status(500).body("Unexpected error during the currency conversion: " + e.getMessage());
            }
        });
    }
}
