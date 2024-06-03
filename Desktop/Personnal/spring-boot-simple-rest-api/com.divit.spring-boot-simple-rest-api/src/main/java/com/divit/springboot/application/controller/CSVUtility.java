package com.divit.springboot.application.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class CSVUtility {

    @Value("${city.file}")
    private String cityFilePath;

    @Value("${job.file}")
    private String jobFilePath;

    @Value("${batch.size}")
    private int batchSize;

    public void mergeAndWrite() throws IOException {
        List<String> cities = readCSV(cityFilePath);
        List<String> jobs = readCSV(jobFilePath);

        List<String> mergedList = mergeCitiesAndJobs(cities, jobs);

        writeOutputToCSV(mergedList);
    }

    private List<String> readCSV(String fileName) throws IOException {
        List<String> list = new ArrayList<>();
        File file = ResourceUtils.getFile("classpath:" + fileName);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            list.add(line);
        }
        br.close();
        return list;
    }



    private List<String> mergeCitiesAndJobs(List<String> cities, List<String> jobs) {
        List<String> mergedList = new ArrayList<>();
        for (String city : cities) {
            String cleanCity = StringUtils.trimAllWhitespace(city).toLowerCase().replace(" ", ""); // Remove spaces and convert to lowercase
            for (String job : jobs) {
                String cleanJob = StringUtils.trimAllWhitespace(job).toLowerCase().replace(" ", ""); // Remove spaces and convert to lowercase
                if (cleanJob.matches("visit|discover")){
                    mergedList.add(cleanJob + cleanCity + ".com");}
                else {
                    mergedList.add(cleanCity + cleanJob + ".com");
                }

            }
        }
        return mergedList;
    }

    private void writeOutputToCSV(List<String> mergedList) throws IOException {
        int totalLines = mergedList.size();
        int fileCount = (totalLines / batchSize) + (totalLines % batchSize == 0 ? 0 : 1);

        for (int i = 0; i < fileCount; i++) {
            int startIdx = i * batchSize;
            int endIdx = Math.min((i + 1) * batchSize, totalLines);
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
}
