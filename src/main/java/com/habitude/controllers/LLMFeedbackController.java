package com.habitude.controllers;

import com.habitude.model.LLMFeedback;
import com.habitude.model.Observation;
import com.habitude.repository.LLMFeedbackRepository;
import com.habitude.repository.ObservationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @PostMapping("/observations/{obsId}/llm-feedback")
    @ResponseStatus(HttpStatus.CREATED)
    public LLMFeedback createFeedback(
            @PathVariable Long obsId,
            @RequestBody LLMFeedback input
    ) {
        Observation obs = obsRepo.findById(obsId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Observation not found"));
        input.setObservation(obs);
        return fbRepo.save(input);
    }

    @GetMapping("/observations/{obsId}/llm-feedback")
    public List<LLMFeedback> listForObservation(@PathVariable Long obsId) {
        if (!obsRepo.existsById(obsId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Observation not found");
        }
        return fbRepo.findByObservationId(obsId);
    }

    @GetMapping("/llm-feedback")
    public List<LLMFeedback> allFeedback() {
        return fbRepo.findAll();
    }

    @GetMapping("/llm-feedback/{id}")
    public LLMFeedback getOne(@PathVariable Long id) {
        return fbRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Feedback not found"));
    }

    @PutMapping("/llm-feedback/{id}")
    public LLMFeedback update(
            @PathVariable Long id,
            @RequestBody LLMFeedback updated
    ) {
        LLMFeedback existing = fbRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Feedback not found"));
        existing.setSuggestion(updated.getSuggestion());
        existing.setAccepted(updated.getAccepted());
        existing.setConfidenceScore(updated.getConfidenceScore());
        return fbRepo.save(existing);
    }

    @DeleteMapping("/llm-feedback/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!fbRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Feedback not found");
        }
        fbRepo.deleteById(id);
    }
}
