package com.exchangerate.currencyexchangeapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/currency")
public class CurrencyConverterController {
    private final CurrencyExchangeApiClient currencyExchangeApiClient;
    
    @Autowired
    public CurrencyConverterController(CurrencyExchangeApiClient currencyExchangeApiClient) {
        this.currencyExchangeApiClient = currencyExchangeApiClient;
    }

    @GetMapping("/convert/{baseCurrency}/{targetCurrency}/{amount}")
    public double convertCurrency(
        @PathVariable String baseCurrency,
        @PathVariable String targetCurrency,
        @PathVariable double amount)
        {
            return currencyExchangeApiClient.getExchangeRate(baseCurrency, targetCurrency) * amount;

        }
    }
