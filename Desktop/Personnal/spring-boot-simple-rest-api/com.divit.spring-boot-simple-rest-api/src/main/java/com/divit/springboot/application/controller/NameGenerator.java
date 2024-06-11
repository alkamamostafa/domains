package com.divit.springboot.application.controller;

import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NameGenerator {




    public static List<String> readBaseNamesFromCSV(String csvFile) throws IOException {
        List<String> list = new ArrayList<>();
        File file = ResourceUtils.getFile("classpath:" + csvFile);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            list.add(line);
        }
        br.close();
        return list;
    }

    public static List<String> generateNames(List<String> baseNames, int numberOfCharacters) {
         List<String> names = new ArrayList<>();


        for (String baseName : baseNames) {
            for (int i = 0; i < numberOfCharacters; i++) {
                char prefix1 = (char) ('a' + i);
                    String newName =    baseName+"s.com";
                    names.add(newName);

            }
        }

        return names;
    }



    private static void writeOutputToCSV(List<String> mergedList) throws IOException {
        int totalLines = mergedList.size();
        int fileCount = (totalLines / 2000) + (totalLines % 2000 == 0 ? 0 : 1);

        for (int i = 0; i < fileCount; i++) {
            int startIdx = i * 2000;
            int endIdx = Math.min((i + 1) * 2000, totalLines);
            List<String> batchList = mergedList.subList(startIdx, endIdx);

            File outputFile = new File("output_" + (i + 1) + ".csv");
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

            for (String line : batchList) {
                writer.write(line);
                writer.newLine();
            }

            writer.close();
        }
    }

    public static void main(String[] args) throws IOException {
        String csvFile = "keywords.csv"; // Specify your CSV file path here
        int numberOfCharacters = 1; // Number of characters in the alphabet

        List<String> baseNames = readBaseNamesFromCSV(csvFile);
        List<String> generatedNames = generateNames(baseNames, numberOfCharacters);

        writeOutputToCSV(generatedNames);
        // Printing the generated names
        for (String name : generatedNames) {
            System.out.println(name);
        }
    }
}
