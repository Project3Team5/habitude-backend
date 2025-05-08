package com.habitude.controllers;

import com.habitude.dto.TreatmentPlanCreateDto;
import com.habitude.dto.TreatmentPlanUpdateDto;
import com.habitude.dto.TreatmentPlanResponseDto;
import com.habitude.model.*;
import com.habitude.repository.*;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TreatmentPlanController {

    private final TreatmentPlanRepository planRepo;
    private final SubjectRepository subjectRepo;
    private final GoalRepository goalRepo;
    private final ObservationRepository obsRepo;

    public TreatmentPlanController(TreatmentPlanRepository planRepo,
                                   SubjectRepository subjectRepo,
                                   GoalRepository goalRepo,
                                   ObservationRepository obsRepo) {
        this.planRepo = planRepo;
        this.subjectRepo = subjectRepo;
        this.goalRepo = goalRepo;
        this.obsRepo = obsRepo;
    }

    // POST: Create treatment plan
    @PostMapping("/treatment-plans")
    public ResponseEntity<TreatmentPlanResponseDto> createPlan(@Valid @RequestBody TreatmentPlanCreateDto dto) {
        Subject subject = subjectRepo.findById(dto.getSubjectId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found"));

        Goal goal = null;
        if (dto.getGoalId() != null) {
            goal = goalRepo.findById(dto.getGoalId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found"));
        }

        Observation observation = null;
        if (dto.getObservationId() != null) {
            observation = obsRepo.findById(dto.getObservationId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Observation not found"));
        }

        TreatmentPlan plan = new TreatmentPlan(subject, goal, observation, dto.getPlan(), dto.getNextReview(), dto.getNotes());
        TreatmentPlan saved = planRepo.save(plan);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TreatmentPlanResponseDto(saved));
    }

    // GET: List by subject
    @GetMapping("/subjects/{subjectId}/treatment-plans")
    public ResponseEntity<List<TreatmentPlanResponseDto>> listBySubject(@PathVariable Long subjectId) {
        List<TreatmentPlan> plans = planRepo.findBySubjectId(subjectId);
        List<TreatmentPlanResponseDto> response = plans.stream()
                .map(TreatmentPlanResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // GET: One by ID
    @GetMapping("/treatment-plans/{id}")
    public ResponseEntity<TreatmentPlanResponseDto> getOne(@PathVariable Long id) {
        TreatmentPlan plan = planRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Treatment Plan not found"));
        return ResponseEntity.ok(new TreatmentPlanResponseDto(plan));
    }

    // PUT: Update plan
    @PutMapping("/treatment-plans/{id}")
    public ResponseEntity<TreatmentPlanResponseDto> update(@PathVariable Long id,
                                                           @Valid @RequestBody TreatmentPlanUpdateDto dto) {
        TreatmentPlan existing = planRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Treatment Plan not found"));

        if (dto.getPlan() != null) existing.setPlan(dto.getPlan());
        if (dto.getNextReview() != null) existing.setNextReview(dto.getNextReview());
        if (dto.getNotes() != null) existing.setNotes(dto.getNotes());

        TreatmentPlan updated = planRepo.save(existing);
        return ResponseEntity.ok(new TreatmentPlanResponseDto(updated));
    }

    // DELETE
    @DeleteMapping("/treatment-plans/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!planRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Treatment Plan not found");
        }
        planRepo.deleteById(id);
    }
}