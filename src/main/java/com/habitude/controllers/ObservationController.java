package com.habitude.controllers;
import com.habitude.model.Observation;
import com.habitude.service.ObservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.*;

@RestController
@RequestMapping("/api/observations")
public class ObservationController {

    @Autowired
    private ObservationService observationService;

    @GetMapping("/summary")
    public Map<String, Object> getSummary() {
        return observationService.getSummary();
    }

    @GetMapping
    public List<Observation> getAllObservations() {
        return observationService.getAllObservations();
    }

    @GetMapping("/trend")
    public Map<String, Object> getTrendData() {
        return observationService.getTrendData();
    }

    @GetMapping("/trend/by-subject/{subjectId}")
    public Map<String, Object> getTrendDataBySubject(@PathVariable Long subjectId) {
        return observationService.getTrendDataBySubject(subjectId);
    }

    @GetMapping("/mock")
    public List<Observation> mockData() {
        return observationService.getMockObservations();
    }
}