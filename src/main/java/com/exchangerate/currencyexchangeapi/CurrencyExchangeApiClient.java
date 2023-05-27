package com.exchangerate.currencyexchangeapi;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CurrencyExchangeApiClient {
    private String apiUrl;
    private String apiKey;

    public CurrencyExchangeApiClient(String apiUrl, String apiKey) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    public double getExchangeRate(String baseCurrency, String targetCurrency) {
        try {
            String requestUrl = apiUrl + "latest.json?app_id=" + apiKey;

            URL url = new URL(requestUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            if (apiKey != null && !apiKey.isEmpty()) {
                connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            }

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                double exchangeRate = parseExchangeRateFromResponse(response.toString(), targetCurrency);

                return exchangeRate;
            } else {
                System.out.println("Error: " + responseCode);
            }
            connection.disconnect();

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        return 0;
    }

    private double parseExchangeRateFromResponse(String response, String targetCurrency) {
        double exchangeRate = 0.0;
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONObject rates = jsonResponse.getJSONObject("rates");
            exchangeRate = rates.getDouble(targetCurrency);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exchangeRate;
    }

    public static void main(String[] args) {
        CurrencyExchangeApiClient client = new CurrencyExchangeApiClient("https://openexchangerates.org/api/", "your-api-key");

        double exchangeRate = client.getExchangeRate("base-currency", "target-currency");

        System.out.println("Exchange rate: " + exchangeRate);
    }
}
