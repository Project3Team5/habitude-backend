package com.habitude.service;

import com.habitude.model.Observation;
import com.habitude.repository.ObservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ObservationService {

    private final ObservationRepository observationRepository;

    public ObservationService(ObservationRepository observationRepository) {
        this.observationRepository = observationRepository;
    }

    // summary for mock data
    public Map<String, Object> getSummary() {
        Map<String, Object> summary = new HashMap<>();
        double averageFrequency = 3.5;
        double averageDuration = 15.0;
        summary.put("averageFrequency", averageFrequency);
        summary.put("averageDuration", averageDuration);
        return summary;
    }

    public List<Observation> getAllObservations() {
        List<Observation> observations = observationRepository.findAll();
        if (observations.isEmpty()) {
            observations = getMockObservations();
        }
        return observations;
    }

    public List<Observation> getMockObservations() {
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

    public Map<String, Object> getTrendData() {
        // actual observation data from DB
        List<Observation> observations = observationRepository.findAll();

        if (observations.isEmpty()) {
            return Map.of("frequencyData", Collections.emptyList(), "durationData", Collections.emptyList());
        }
        Map<LocalDateTime, List<Observation>> grouped = observations.stream()
                .collect(Collectors.groupingBy(o -> LocalDateTime.from(o.getTimestamp().toLocalDate())));
        List<Map<String, Object>> frequencyData = new ArrayList<>();
        List<Map<String, Object>> durationData = new ArrayList<>();

        grouped.keySet().forEach(date -> {
            List<Observation> dailyObservations = grouped.get(date);
            int totalFrequency = dailyObservations.stream().mapToInt(Observation::getFrequency).sum();
            int totalDuration = dailyObservations.stream().mapToInt(Observation::getDuration).sum();

            frequencyData.add(Map.of("date", date.toString(), "value", totalFrequency));
            durationData.add(Map.of("date", date.toString(), "value", totalDuration));
        });
        return Map.of(
                "frequencyData", frequencyData,
                "durationData", durationData
        );
    }
    public Map<String, Object> getTrendDataBySubject(Long subjectId) {
        List<Observation> observations = observationRepository.findBySubjectId(subjectId); // Make sure this exists

        if (observations.isEmpty()) {
            return Map.of("frequencyData", Collections.emptyList(), "durationData", Collections.emptyList());
        }

        Map<LocalDateTime, List<Observation>> grouped = observations.stream()
                .collect(Collectors.groupingBy(o -> o.getTimestamp().toLocalDate().atStartOfDay()));

        List<Map<String, Object>> frequencyData = new ArrayList<>();
        List<Map<String, Object>> durationData = new ArrayList<>();

        grouped.forEach((date, dailyObservations) -> {
            int totalFrequency = dailyObservations.stream().mapToInt(Observation::getFrequency).sum();
            int totalDuration = dailyObservations.stream().mapToInt(Observation::getDuration).sum();

            frequencyData.add(Map.of("date", date.toString(), "value", totalFrequency));
            durationData.add(Map.of("date", date.toString(), "value", totalDuration));
        });

        return Map.of(
                "frequencyData", frequencyData,
                "durationData", durationData
        );
    }

}
