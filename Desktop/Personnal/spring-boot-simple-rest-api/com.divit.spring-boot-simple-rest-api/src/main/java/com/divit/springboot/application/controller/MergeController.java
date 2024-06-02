package com.divit.springboot.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MergeController {

    @Autowired
    private MergeService mergeService;

    @GetMapping("/merge")
    public String mergeCitiesAndJobs() {
        mergeService.mergeCitiesAndJobs();
        return "Merge process initiated. Check the generated files.";
    }
}
