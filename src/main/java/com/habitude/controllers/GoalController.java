package com.habitude.controllers;

import com.habitude.model.Goal;
import com.habitude.model.Subject;
import com.habitude.repository.GoalRepository;
import com.habitude.repository.SubjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GoalController {

    private final GoalRepository goalRepo;
    private final SubjectRepository subjectRepo;

    public GoalController(GoalRepository goalRepo,
                          SubjectRepository subjectRepo) {
        this.goalRepo    = goalRepo;
        this.subjectRepo = subjectRepo;
    }

    // 1) Create a new goal for a subject
    @PostMapping("/subjects/{subjectId}/goals")
    @ResponseStatus(HttpStatus.CREATED)
    public Goal createGoal(@PathVariable Long subjectId,
                           @RequestBody Goal input) {
        Subject subject = subjectRepo.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        input.setSubject(subject);
        return goalRepo.save(input);
    }

    // 2) List all goals for a subject
    @GetMapping("/subjects/{subjectId}/goals")
    public List<Goal> getGoalsForSubject(@PathVariable Long subjectId) {
        return goalRepo.findBySubjectId(subjectId);
    }

    // 3) Get a single goal by its ID
    @GetMapping("/goals/{id}")
    public Goal getGoal(@PathVariable Long id) {
        return goalRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Goal not found"));
    }

    // 4) Update a goal
    @PutMapping("/goals/{id}")
    public Goal updateGoal(@PathVariable Long id,
                           @RequestBody Goal updated) {
        Goal existing = goalRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Goal not found"));
        existing.setDescription(updated.getDescription());
        existing.setTargetDate(updated.getTargetDate());
        existing.setStatus(updated.getStatus());
        return goalRepo.save(existing);
    }

    // 5) Delete a goal
    @DeleteMapping("/goals/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGoal(@PathVariable Long id) {
        goalRepo.deleteById(id);
    }
}

