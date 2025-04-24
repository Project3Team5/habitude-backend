package com.habitude.controllers;

import com.habitude.model.LLMFeedback;
import com.habitude.model.Observation;
import com.habitude.repository.LLMFeedbackRepository;
import com.habitude.repository.ObservationRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LLMFeedbackController {

    private final LLMFeedbackRepository fbRepo;
    private final ObservationRepository obsRepo;

    public LLMFeedbackController(
            LLMFeedbackRepository fbRepo,
            ObservationRepository obsRepo
    ) {
        this.fbRepo = fbRepo;
        this.obsRepo = obsRepo;
    }

    /** 1) Create a new feedback for an existing observation */
    @PostMapping("/observations/{obsId}/llm-feedback")
    @ResponseStatus(HttpStatus.CREATED)
    public LLMFeedback createFeedback(
            @PathVariable Long obsId,
            @RequestBody LLMFeedback input
    ) {
        Observation obs = obsRepo.findById(obsId)
                .orElseThrow(() -> new RuntimeException("Observation not found"));
        input.setObservation(obs);
        return fbRepo.save(input);
    }

    /** 2) List all feedback for a given observation */
    @GetMapping("/observations/{obsId}/llm-feedback")
    public List<LLMFeedback> listForObservation(@PathVariable Long obsId) {
        return fbRepo.findByObservationId(obsId);
    }

    /** 3) GET all feedback entries */
    @GetMapping("/llm-feedback")
    public List<LLMFeedback> allFeedback() {
        return fbRepo.findAll();
    }

    /** 4) GET one by its ID */
    @GetMapping("/llm-feedback/{id}")
    public LLMFeedback getOne(@PathVariable Long id) {
        return fbRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));
    }

    /** 5) Update an existing feedback entry */
    @PutMapping("/llm-feedback/{id}")
    public LLMFeedback update(
            @PathVariable Long id,
            @RequestBody LLMFeedback updated
    ) {
        LLMFeedback existing = fbRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));
        existing.setSuggestion(updated.getSuggestion());
        existing.setAccepted(updated.getAccepted());
        existing.setConfidenceScore(updated.getConfidenceScore());
        // (we typically don’t change observation or generatedAt)
        return fbRepo.save(existing);
    }

    /** 6) Delete a feedback entry */
    @DeleteMapping("/llm-feedback/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        fbRepo.deleteById(id);
    }
}
