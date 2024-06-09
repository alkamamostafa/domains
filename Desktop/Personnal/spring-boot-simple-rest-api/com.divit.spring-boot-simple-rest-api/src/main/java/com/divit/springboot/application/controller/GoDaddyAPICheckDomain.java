package com.divit.springboot.application.controller;


import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GoDaddyAPICheckDomain {

    private static final String API_KEY = "eoBX7wY1nba8_5FZRHH3DCj6HqNQuLvey8V"; // Your API key
    private static final String API_SECRET = "S9g3ZU9kRXddCXPMDh3hqa"; // Updated API secret

    public static void main(String[] args) {
        String domain = "cd.com";
        checkDomainAvailability(domain);
    }

    public static void checkDomainAvailability(String domain) {
        String urlString = "https://api.ote-godaddy.com/v1/domains/available?checkType=FAST";
        String jsonInputString = "[\"" + domain + "\"]";

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "sso-key " + API_KEY + ":" + API_SECRET);
            conn.setDoOutput(true);

            // Write JSON input string to the output stream
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) { // Success
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Print the response
                System.out.println("Response: " + response.toString());
            } else {
                System.out.println("POST request failed. Response Code: " + responseCode);

                BufferedReader errorStream = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"));
                String errorLine;
                StringBuilder errorResponse = new StringBuilder();

                while ((errorLine = errorStream.readLine()) != null) {
                    errorResponse.append(errorLine);
                }
                errorStream.close();

                System.out.println("Error Response: " + errorResponse.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
