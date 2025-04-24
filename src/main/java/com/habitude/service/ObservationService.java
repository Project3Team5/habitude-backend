package com.habitude.service;

import com.habitude.model.Observation;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ObservationService {
    public Map<String, Object> getSummary() {
        Map<String, Object> summary = new HashMap<>();
        double averageFrequency = 3.5;
        double averageDuration = 15.0;
        summary.put("averageFrequency", averageFrequency);
        summary.put("averageDuration", averageDuration);
        return summary;
    }

    public List<Observation> getAllObservations() {
        List<Observation> mockObservations = new ArrayList<>();

        // first fake record
        Observation o1 = new Observation();             // no-arg constructor
        o1.setBehavior("Behavior A");                  // we assume your entity has these setters
        o1.setContext("First behavior");
        o1.setDuration(20);
        o1.setFrequency(3);
        o1.setTimestamp(LocalDateTime.now());
        mockObservations.add(o1);

        // second fake record
        Observation o2 = new Observation();
        o2.setBehavior("Behavior B");
        o2.setContext("Second behavior");
        o2.setDuration(15);
        o2.setFrequency(2);
        o2.setTimestamp(LocalDateTime.now());
        mockObservations.add(o2);

        return mockObservations;
    }
}
