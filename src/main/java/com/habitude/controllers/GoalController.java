package com.habitude.controllers;

import com.habitude.dto.GoalCreateDto;
import com.habitude.dto.GoalUpdateDto;
import com.habitude.dto.GoalResponseDto;
import com.habitude.model.Goal;
import com.habitude.model.Subject;
import com.habitude.repository.GoalRepository;
import com.habitude.repository.SubjectRepository;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class GoalController {

    private final GoalRepository goalRepo;
    private final SubjectRepository subjectRepo;

    public GoalController(GoalRepository goalRepo, SubjectRepository subjectRepo) {
        this.goalRepo = goalRepo;
        this.subjectRepo = subjectRepo;
    }

    // Create goal
    @PostMapping("/subjects/{subjectId}/goals")
    public ResponseEntity<GoalResponseDto> createGoal(@PathVariable Long subjectId,
                                                      @Valid @RequestBody GoalCreateDto dto) {
        Subject subject = subjectRepo.findById(subjectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found"));

        Goal goal = new Goal();
        goal.setSubject(subject);
        goal.setDescription(dto.getDescription());
        goal.setTargetDate(dto.getTargetDate());
        goal.setStatus(dto.getStatus());

        Goal saved = goalRepo.save(goal);
        return ResponseEntity.status(HttpStatus.CREATED).body(new GoalResponseDto(saved));
    }

    // List all goals for a subject
    @GetMapping("/subjects/{subjectId}/goals")
    public ResponseEntity<List<GoalResponseDto>> getGoalsForSubject(@PathVariable Long subjectId) {
        if (!subjectRepo.existsById(subjectId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found");
        }

        List<GoalResponseDto> goals = goalRepo.findBySubjectId(subjectId).stream()
                .map(GoalResponseDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(goals);
    }

    // Get goal by ID
    @GetMapping("/goals/{id}")
    public ResponseEntity<GoalResponseDto> getGoal(@PathVariable Long id) {
        Goal goal = goalRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found"));

        return ResponseEntity.ok(new GoalResponseDto(goal));
    }

    // Update goal
    @PutMapping("/goals/{id}")
    public ResponseEntity<GoalResponseDto> updateGoal(@PathVariable Long id,
                                                      @Valid @RequestBody GoalUpdateDto dto) {
        Goal goal = goalRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found"));

        if (dto.getDescription() != null) goal.setDescription(dto.getDescription());
        if (dto.getTargetDate() != null) goal.setTargetDate(dto.getTargetDate());
        if (dto.getStatus() != null) goal.setStatus(dto.getStatus());

        Goal updated = goalRepo.save(goal);
        return ResponseEntity.ok(new GoalResponseDto(updated));
    }

    // Delete goal
    @DeleteMapping("/goals/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGoal(@PathVariable Long id) {
        if (!goalRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found");
        }
        goalRepo.deleteById(id);
    }
}