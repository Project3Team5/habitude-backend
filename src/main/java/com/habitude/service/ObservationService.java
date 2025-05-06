package com.habitude.service;

import com.habitude.model.Observation;
import com.habitude.repository.ObservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ObservationService {

    private final ObservationRepository observationRepository;

    public ObservationService(ObservationRepository observationRepository) {
        this.observationRepository = observationRepository;
    }

    public Observation saveObservation(Observation observation) {
        return observationRepository.save(observation);
    }

    public Observation getObservationById(Long id) {
        return observationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Observation not found"));
    }

    public Observation updateObservation(Long id, Observation updated) {
        Observation existing = getObservationById(id);
        existing.setBehavior(updated.getBehavior());
        existing.setContext(updated.getContext());
        existing.setTimestamp(updated.getTimestamp());
        existing.setDuration(updated.getDuration());
        existing.setFrequency(updated.getFrequency());
        existing.setIntensity(updated.getIntensity());
        return observationRepository.save(existing);
    }

    public void deleteObservation(Long id) {
        observationRepository.deleteById(id);
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
        Observation o1 = new Observation();
        o1.setBehavior("Behavior A");
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
        List<Observation> observations = observationRepository.findAll();
        if (observations.isEmpty()) {
            observations = getMockObservations();
        }
        Map<LocalDate, List<Observation>> grouped = observations.stream()
                .collect(Collectors.groupingBy(o -> o.getTimestamp().toLocalDate()));
        List<Map<String, Object>> frequencyData = new ArrayList<>();
        List<Map<String, Object>> durationData = new ArrayList<>();

        for (Map.Entry<LocalDate, List<Observation>> entry : grouped.entrySet()) {
            int freq = entry.getValue().stream()
                    .mapToInt(o -> Optional.ofNullable(o.getFrequency()).orElse(0)).sum();
            int dur = entry.getValue().stream()
                    .mapToInt(o -> Optional.ofNullable(o.getDuration()).orElse(0)).sum();
            frequencyData.add(Map.of("date", entry.getKey().atStartOfDay().toString(), "value", freq));
            durationData.add(Map.of("date", entry.getKey().atStartOfDay().toString(), "value", dur));
        }
        return Map.of(
                "frequencyData", frequencyData,
                "durationData", durationData
        );
    }

    public Map<String, Object> getTrendDataBySubject(Long subjectId) {
        List<Observation> observations = observationRepository.findBySubjectId(subjectId);
        if (observations.isEmpty()) {
            return Map.of(
                    "frequencyData", Collections.emptyList(),
                    "durationData", Collections.emptyList()
            );
        }
        Map<LocalDate, List<Observation>> grouped = observations.stream()
                .collect(Collectors.groupingBy(o -> o.getTimestamp().toLocalDate()));
        List<Map<String, Object>> frequencyData = new ArrayList<>();
        List<Map<String, Object>> durationData = new ArrayList<>();

        grouped.forEach((date, dailyObservations) -> {
            int totalFrequency = dailyObservations.stream()
                    .mapToInt(o -> Optional.ofNullable(o.getFrequency()).orElse(0)).sum();
            int totalDuration = dailyObservations.stream()
                    .mapToInt(o -> Optional.ofNullable(o.getDuration()).orElse(0)).sum();

            frequencyData.add(Map.of("date", date.toString(), "value", totalFrequency));
            durationData.add(Map.of("date", date.toString(), "value", totalDuration));
        });

        return Map.of(
                "frequencyData", frequencyData,
                "durationData", durationData
        );
    }
}
