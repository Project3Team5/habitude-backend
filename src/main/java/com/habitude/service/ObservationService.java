package com.habitude.service;
import com.habitude.model.Observation;
import org.springframework.stereotype.Service;
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
        List<Observation> mockObservation = new ArrayList<>();
        mockObservation.add(new Observation("Behavior A", "First behavior", 20.0, 3.0));
        mockObservation.add(new Observation("Behavior B", "Second hebavior", 15, 2.5));
        return mockObservation;
    }
}