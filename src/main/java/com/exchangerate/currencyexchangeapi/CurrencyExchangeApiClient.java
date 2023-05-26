package com.exchangerate.currencyexchangeapi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CurrgencyExchangeApiClient {
    private String apiUrl;
    private String apiKey;

    public CurrencyExchangeApiClient(String apiUrl, String apiKey) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    public double getExchangeRate(String baseCurrency, String targetCurrency) {
        try {
            String requestUrl = apiUrl + "?base=" + baseCurrency + "&symbols=" + targetCurrency;

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
            String rateKey = "\"" + targetCurrency + "\"";
            int startIndex = response.indexOf(rateKey) + rateKey.length();
            int endIndex = response.indexOf(",", startIndex);
            String rateValue = response.substring(startIndex, endIndex).trim();
            exchangeRate = Double.parseDouble(rateValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exchangeRate;
    }

    public static void main(String[] args) {
        // Create an instance of CurrencyExchangeApiClient
        CurrencyExchangeApiClient client = new CurrencyExchangeApiClient("your-api-url", "your-api-key");

        // Call the getExchangeRate method to perform currency exchange
        double exchangeRate = client.getExchangeRate("base-currency", "target-currency");

        // Print the exchange rate
        System.out.println("Exchange rate: " + exchangeRate);
    }
}
