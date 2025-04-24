package com.habitude.controllers;

import com.habitude.model.*;
import com.habitude.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        this.planRepo    = planRepo;
        this.subjectRepo = subjectRepo;
        this.goalRepo    = goalRepo;
        this.obsRepo     = obsRepo;
    }

    @PostMapping("/subjects/{subjectId}/treatment-plans")
    @ResponseStatus(HttpStatus.CREATED)
    public TreatmentPlan create(@PathVariable Long subjectId,
                                @RequestBody TreatmentPlan input) {
        Subject subject = subjectRepo.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        input.setSubject(subject);

        if (input.getGoal() != null) {
            Goal goal = goalRepo.findById(input.getGoal().getId())
                    .orElseThrow(() -> new RuntimeException("Goal not found"));
            input.setGoal(goal);
        }
        if (input.getObservation() != null) {
            Observation obs = obsRepo.findById(input.getObservation().getId())
                    .orElseThrow(() -> new RuntimeException("Observation not found"));
            input.setObservation(obs);
        }

        return planRepo.save(input);
    }

    @GetMapping("/subjects/{subjectId}/treatment-plans")
    public List<TreatmentPlan> listBySubject(@PathVariable Long subjectId) {
        return planRepo.findBySubjectId(subjectId);
    }

    @GetMapping("/treatment-plans/{id}")
    public TreatmentPlan getOne(@PathVariable Long id) {
        return planRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found"));
    }

    @PutMapping("/treatment-plans/{id}")
    public TreatmentPlan update(@PathVariable Long id,
                                @RequestBody TreatmentPlan updated) {
        TreatmentPlan existing = planRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found"));
        existing.setPlan(updated.getPlan());
        existing.setNextReview(updated.getNextReview());
        existing.setNotes(updated.getNotes());
        return planRepo.save(existing);
    }

    @DeleteMapping("/treatment-plans/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        planRepo.deleteById(id);
    }
}
