package com.exchangerate.currencyexchangeapi;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import org.springframework.cache.annotation.Cacheable;

@Slf4j
@Service
public class CurrencyExchangeApiClient {

    private String apiUrl;
    private String apiKey;

    // Load DB username and password from environment variables
    private String dbUsername = System.getenv("DB_USERNAME");
    private String dbPassword = System.getenv("DB_PASSWORD");

    public CurrencyExchangeApiClient(@Value("${api.url}") String apiUrl) {
        this.apiUrl = apiUrl;

        // Load API key from .env file using Dotenv
        Dotenv dotenv = Dotenv.configure().load();
        this.apiKey = dotenv.get("API_KEY");
        this.dbUsername = dotenv.get("DB_USERNAME");
        this.dbPassword = dotenv.get("DB_PASSWORD");

        // Debugging: Print the loaded environment variables
        log.info("Loaded environment variables:");
        log.info("DB_USERNAME: {}", dbUsername);
        log.info("DB_PASSWORD: {}", dbPassword);
        log.info("API_Key: {}", apiKey);
    }

    @Cacheable(value = "currencyExchange", key = "#baseCurrency.concat('-').concat(#targetCurrency)")
    public CompletableFuture<Double> getExchangeRate(String baseCurrency, String targetCurrency) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String requestUrl = apiUrl + "latest.json?app_id=" + apiKey + "&base=" + baseCurrency + "&symbols=" + targetCurrency;
                log.info("Sending request to {}", requestUrl);
                URL url = new URL(requestUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    return parseExchangeRateFromResponse(response.toString(), targetCurrency);
                } else {
                    log.error("Received an unexpected HTTP status: {}", responseCode);
                    throw new IllegalStateException("Received an unexpected HTTP status when trying to retrieve exchange rate: " + responseCode);
                }
            } catch (Exception e) {
                log.error("Exception occurred during the exchange rate retrieval", e);
                throw new RuntimeException("Exception occurred during the exchange rate retrieval: " + e.getMessage(), e);
            }
        });
    }

    private double parseExchangeRateFromResponse(String response, String targetCurrency) {
        try {
            if (response == null || response.isEmpty()) {
                log.error("Received an empty or null response from the API");
                throw new IllegalArgumentException("Received an empty or null response from the API");
            }

            JSONObject jsonResponse = new JSONObject(response);
            if (!jsonResponse.has("rates")) {
                log.error("The 'rates' field is missing in the response");
                throw new IllegalArgumentException("The 'rates' field is missing in the response");
            }

            JSONObject rates = jsonResponse.getJSONObject("rates");
            if (!rates.has(targetCurrency)) {
                log.error("The target currency is missing in the response rates");
                throw new IllegalArgumentException("The target currency is missing in the response rates");
            }

            return rates.getDouble(targetCurrency);
        } catch (Exception e) {
            log.error("Failed to parse the exchange rate from the response: ", e);
            throw new IllegalArgumentException("Failed to parse the exchange rate from the response: " + e.getMessage(), e);
        }
    }
}
