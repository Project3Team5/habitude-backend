package com.habitude.controllers;

import com.habitude.dto.ObservationCreateDto;
import com.habitude.dto.ObservationUpdateDto;
import com.habitude.dto.ObservationResponseDto;
import com.habitude.model.Observation;
import com.habitude.model.Subject;
import com.habitude.model.User;
import com.habitude.repository.SubjectRepository;
import com.habitude.repository.UserRepository;
import com.habitude.service.ObservationService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/observations")
public class ObservationController {

    private final ObservationService observationService;
    private final SubjectRepository subjectRepo;
    private final UserRepository userRepo;

    public ObservationController(ObservationService observationService,
                                 SubjectRepository subjectRepo,
                                 UserRepository userRepo) {
        this.observationService = observationService;
        this.subjectRepo = subjectRepo;
        this.userRepo = userRepo;
    }

    // Summary (mocked or aggregated)
    @GetMapping("/summary")
    public Map<String, Object> getSummary() {
        return observationService.getSummary();
    }

    // List all observations for a subject
    @GetMapping("/subjects/{subjectId}")
    public ResponseEntity<List<ObservationResponseDto>> getAllObservationsBySubject(@PathVariable Long subjectId) {
        List<Observation> observations = observationService.getAllObservationsBySubject(subjectId);
        List<ObservationResponseDto> response = observations.stream()
                .map(ObservationResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // Get single observation
    @GetMapping("/{id}")
    public ResponseEntity<ObservationResponseDto> getObservation(@PathVariable Long id) {
        Observation obs = observationService.getObservationById(id);
        return ResponseEntity.ok(new ObservationResponseDto(obs));
    }

    // Trend data (all and by subject)
    @GetMapping("/trend")
    public Map<String, Object> getTrendData() {
        return observationService.getTrendData();
    }

    @GetMapping("/trend/by-subject/{subjectId}")
    public Map<String, Object> getTrendDataBySubject(@PathVariable Long subjectId) {
        return observationService.getTrendDataBySubject(subjectId);
    }

    // Mock observation data
    @GetMapping("/mock")
    public List<ObservationResponseDto> mockData() {
        return observationService.getMockObservations().stream()
                .map(ObservationResponseDto::new)
                .collect(Collectors.toList());
    }

    // Create a new observation
    @PostMapping
    public ResponseEntity<ObservationResponseDto> createObservation(@Valid @RequestBody ObservationCreateDto dto) {
        Subject subject = subjectRepo.findById(dto.getSubjectId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found"));

        User observer = userRepo.findById(dto.getObserverId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Observer not found"));

        Observation obs = new Observation();
        obs.setSubject(subject);
        obs.setObserver(observer);
        obs.setBehavior(dto.getBehavior());
        obs.setContext(dto.getContext());
        obs.setDuration(dto.getDuration());
        obs.setFrequency(dto.getFrequency());
        obs.setIntensity(dto.getIntensity());
        obs.setTimestamp(dto.getTimestamp());

        Observation saved = observationService.saveObservation(obs);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ObservationResponseDto(saved));
    }

    // Update an observation
    @PutMapping("/{id}")
    public ResponseEntity<ObservationResponseDto> updateObservation(@PathVariable Long id,
                                                                    @Valid @RequestBody ObservationUpdateDto dto) {
        Observation obs = observationService.getObservationById(id);

        if (dto.getBehavior() != null) obs.setBehavior(dto.getBehavior());
        if (dto.getContext() != null) obs.setContext(dto.getContext());
        if (dto.getDuration() != null) obs.setDuration(dto.getDuration());
        if (dto.getFrequency() != null) obs.setFrequency(dto.getFrequency());
        if (dto.getIntensity() != null) obs.setIntensity(dto.getIntensity());
        if (dto.getTimestamp() != null) obs.setTimestamp(dto.getTimestamp());

        Observation updated = observationService.updateObservation(id, obs);
        return ResponseEntity.ok(new ObservationResponseDto(updated));
    }

    // Delete observation
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteObservation(@PathVariable Long id) {
        observationService.deleteObservation(id);
    }
}