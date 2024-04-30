package com.example.demo;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CurrencyConverter {
    private static final String API_KEY = "b2e0cd2565a170828dfc3b24";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    public static double convertCurrency(double amount, String sourceCurrency, String targetCurrency) throws IOException, ParseException {
        String apiUrl = BASE_URL + API_KEY + "/latest/" + sourceCurrency;
        String jsonResponse = getApiResponse(apiUrl);
        double conversionRate = parseConversionRate(jsonResponse, targetCurrency);
        return amount * conversionRate;
    }

    private static String getApiResponse(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        InputStream inputStream = connection.getInputStream();
        Scanner scanner = new Scanner(inputStream);
        StringBuilder response = new StringBuilder();
        while (scanner.hasNextLine()) {
            response.append(scanner.nextLine());
        }
        scanner.close();

        return response.toString();
    }

    private static double parseConversionRate(String jsonResponse, String targetCurrency) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(jsonResponse);
        JSONObject conversionRates = (JSONObject) jsonObject.get("conversion_rates");
        return (double) conversionRates.get(targetCurrency);
    }
}
