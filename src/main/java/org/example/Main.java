package org.example;

import org.json.JSONObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {

    // CoinGecko free API — no key required
    private static final String API_URL =
            "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=usd";

    public static void main(String[] args) {

        try {
            // --- 1. Fetch Bitcoin price ---
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.out.println("Error fetching price. Status: " + response.statusCode());
                return;
            }

            // --- 2. Parse the price from JSON ---
            // Response looks like: {"bitcoin":{"usd":62000.0}}
            JSONObject json     = new JSONObject(response.body());
            double bitcoinPrice = json.getJSONObject("bitcoin").getDouble("usd");

            System.out.printf("Current Bitcoin Price: $%,.2f USD%n", bitcoinPrice);

            // --- 3. Ask user how much Bitcoin they hold ---
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the amount of Bitcoin you own: ");
            double bitcoinOwned = scanner.nextDouble();
            scanner.close();

            // --- 4. Calculate total value ---
            double totalValue = bitcoinOwned * bitcoinPrice;

            // --- 5. Display result ---
            System.out.println("\n--- Portfolio Summary ---");
            System.out.printf("Bitcoin Owned : %.8f BTC%n", bitcoinOwned);
            System.out.printf("Price per BTC : $%,.2f USD%n", bitcoinPrice);
            System.out.printf("Total Value   : $%,.2f USD%n", totalValue);

        } catch (Exception e) {
            throw new RuntimeException("Something went wrong: " + e.getMessage(), e);
        }
    }
}