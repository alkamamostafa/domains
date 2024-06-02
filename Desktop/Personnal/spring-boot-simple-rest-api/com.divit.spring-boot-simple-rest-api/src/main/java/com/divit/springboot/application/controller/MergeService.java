package com.divit.springboot.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MergeService {

    @Autowired
    private CSVUtility csvUtility;

    public void mergeCitiesAndJobs() {
        try {
            csvUtility.mergeAndWrite();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error
        }
    }
}
