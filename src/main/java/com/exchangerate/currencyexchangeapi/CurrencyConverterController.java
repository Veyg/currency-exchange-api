package com.exchangerate.currencyexchangeapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/api/currency")
public class CurrencyConverterController {

    private final CurrencyExchangeApiClient currencyExchangeApiClient;
    private final ApiKeyService apiKeyService;

    public CurrencyConverterController(CurrencyExchangeApiClient currencyExchangeApiClient, ApiKeyService apiKeyService) {
        this.currencyExchangeApiClient = currencyExchangeApiClient;
        this.apiKeyService = apiKeyService;
    }

    @GetMapping("/convert/{baseCurrency}/{targetCurrency}/{amount}")
    public CompletableFuture<ResponseEntity<?>> convertCurrency(
        @PathVariable String baseCurrency,
        @PathVariable String targetCurrency,
        @PathVariable double amount,
        @RequestParam("apiKey") String apiKey)
    {
        log.info("Received request to convert from {} to {} amount {}", baseCurrency, targetCurrency, amount);

        if(!apiKeyService.isValidApiKey(apiKey)){
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body("Invalid API key"));
        }

        ApiKeyRecord apiKeyRecord = apiKeyService.getApiKeyRecord(apiKey);
        if (apiKeyRecord == null) {
            return CompletableFuture.completedFuture(ResponseEntity.status(500).body("Error retrieving API key record"));
        }

        int maxRequestsFromDB = apiKeyRecord.getMaxRequests();
        TimeUnit perTimeUnit = apiKeyRecord.getPerTimeUnit();

        if (apiKeyService.isRateLimitExceeded(apiKey, maxRequestsFromDB, perTimeUnit)) {
            return CompletableFuture.completedFuture(ResponseEntity.status(429).body("Rate limit exceeded"));
        }

        return currencyExchangeApiClient.getExchangeRate(baseCurrency, targetCurrency).thenApply(exchangeRate -> {
            try {
                double convertedAmount = exchangeRate * amount;
                return ResponseEntity.ok(convertedAmount);
            } catch (IllegalArgumentException e) {
                log.error("Error during the conversion request: ", e);
                return ResponseEntity.badRequest().body(e.getMessage());
            } catch (IllegalStateException e) {
                log.error("Error during the conversion request: ", e);
                return ResponseEntity.status(500).body("Error during the conversion request: " + e.getMessage());
            } catch (RuntimeException e) {
                log.error("Unexpected error during the currency conversion: ", e);
                return ResponseEntity.status(500).body("Unexpected error during the currency conversion: " + e.getMessage());
            }
        });
    }
}
