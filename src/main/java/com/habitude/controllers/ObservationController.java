package com.habitude.controllers;
import com.habitude.model.Observation;
import com.habitude.service.ObservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
public class ObservationController {
    @Autowired
    private ObservationService observationService;

    @GetMapping("/api/observations/summary")
    public Map<String, Object> getSummary() {
        return observationService.getSummary();
    }

    @GetMapping("/api/observations")
    public List<Observation> getAllObservations() {
        return observationService.getAllObservations();
    }
}