package com.exchangerate.currencyexchangeapi;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class CurrencyExchangeApiClient {
    private String apiUrl;
    private String apiKey;

    public CurrencyExchangeApiClient(String apiUrl, String secretFile) {
        this.apiUrl = "https://openexchangerates.org/api/";
        this.apiKey = loadApiKeyFromSecret(secretFile);
    }

    private String loadApiKeyFromSecret (String secretFile){
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(secretFile));
            return properties.getProperty("api.key");
        } catch (Exception e) {
            System.out.println("Failed to load API key from config file: " + e.getMessage());
            return null;
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
        CurrencyExchangeApiClient client = new CurrencyExchangeApiClient("https://openexchangerates.org/api/", "src/main/java/com/exchangerate/currencyexchangeapi/secrets.properties");

        double exchangeRate = client.getExchangeRate("USD", "EUR");
        System.out.println("Exchange rate: " + exchangeRate);
    }
}
