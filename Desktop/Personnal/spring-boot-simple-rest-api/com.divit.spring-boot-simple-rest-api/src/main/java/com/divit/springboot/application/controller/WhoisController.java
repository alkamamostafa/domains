package com.divit.springboot.application.controller;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class WhoisController {

    private static final String WHOIS_SERVER = "whois.internic.net";
    private static final int WHOIS_PORT = 43;
    private static final Pattern EXPIRY_DATE_PATTERN = Pattern.compile("Registry Expiry Date: (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z)");

    @GetMapping("/check-domains")
    public String checkDomains() {
        String inputCsvPath = "output_1.csv";
        String outputCsvPath = "output_domains.csv";
        String expiringSoonCsvPath = "expiring_soon_domains.csv";

        try {
            List<String[]> domains = readDomainsFromCsv(inputCsvPath);
            List<String[]> results = new ArrayList<>();
            List<String[]> expiringSoonResults = new ArrayList<>();

            LocalDate currentDate = LocalDate.now();
            LocalDate thresholdDate = currentDate.plusDays(30);

            for (String[] domainEntry : domains) {
                String domain = domainEntry[0];
                String whoisResponse = queryWhoisServer(domain);

                String expiryDateStr = extractExpiryDate(whoisResponse);
                if (expiryDateStr != null) {
                    ZonedDateTime expiryDate = ZonedDateTime.parse(expiryDateStr);
                    if (expiryDate.toLocalDate().isBefore(thresholdDate)) {
                        expiringSoonResults.add(new String[]{domain, "Active", expiryDateStr});
                    }
                    results.add(new String[]{domain, "Active", expiryDateStr});
                } else {
                    results.add(new String[]{domain, "Expired", "N/A"});
                }
            }

            writeResultsToCsv(outputCsvPath, results);
            writeResultsToCsv(expiringSoonCsvPath, expiringSoonResults);
            return "Domain check completed. Results saved to " + outputCsvPath + " and " + expiringSoonCsvPath;
        } catch (IOException e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }
    }

    private List<String[]> readDomainsFromCsv(String filePath) throws IOException {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            return reader.readAll();
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeResultsToCsv(String filePath, List<String[]> data) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            writer.writeAll(data);
        }
    }

    private String queryWhoisServer(String domain) throws IOException {
        try (Socket socket = new Socket(WHOIS_SERVER, WHOIS_PORT);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(domain);
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line).append("\n");
            }
            return response.toString();
        }
    }

    private String extractExpiryDate(String whoisResponse) {
        Matcher matcher = EXPIRY_DATE_PATTERN.matcher(whoisResponse);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
