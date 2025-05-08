package com.habitude.dto;

import com.habitude.model.Goal;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class GoalResponseDto {

    private Long id;
    private String description;
    private LocalDate targetDate;
    private String status;
    private LocalDateTime createdAt;

    private Long subjectId;
    private String subjectName;

    public GoalResponseDto(Goal goal) {
        this.id = goal.getId();
        this.description = goal.getDescription();
        this.targetDate = goal.getTargetDate();
        this.status = goal.getStatus();
        this.createdAt = goal.getCreatedAt();

        this.subjectId = goal.getSubject().getId();
        this.subjectName = goal.getSubject().getName();
    }

    // Getters

    public Long getId() { return id; }
    public String getDescription() { return description; }
    public LocalDate getTargetDate() { return targetDate; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Long getSubjectId() { return subjectId; }
    public String getSubjectName() { return subjectName; }
}