package com.exchangerate.currencyexchangeapi;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

@Service
public class CurrencyExchangeApiClient {
    private String apiUrl;
    private String apiKey;

    public CurrencyExchangeApiClient(@Value("${api.url}") String apiUrl, @Value("${secret.file}") String secretFile) {
        this.apiUrl = apiUrl;
        this.apiKey = loadApiKeyFromSecret(secretFile);
    }

    private String loadApiKeyFromSecret(String secretFile) {
        try {
            Properties properties = new Properties();
            properties.load(new ClassPathResource(secretFile).getInputStream());
            return properties.getProperty("api.key");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load API key from config file: " + e.getMessage(), e);
        }
    }

    public double getExchangeRate(String baseCurrency, String targetCurrency) {
        try {
            String requestUrl = apiUrl + "latest.json?app_id=" + apiKey + "&base=" + baseCurrency + "&symbols=" + targetCurrency;
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
                throw new RuntimeException("Error: " + responseCode);
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception: " + e.getMessage(), e);
        }
    }

    private double parseExchangeRateFromResponse(String response, String targetCurrency) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONObject rates = jsonResponse.getJSONObject("rates");
            return rates.getDouble(targetCurrency);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse exchange rate from response: " + e.getMessage(), e);
        }
    }
}
