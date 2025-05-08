package com.habitude.dto;

import com.habitude.model.TreatmentPlan;

import java.time.LocalDateTime;

public class TreatmentPlanResponseDto {

    private Long id;
    private String plan;
    private String notes;
    private LocalDateTime nextReview;
    private LocalDateTime createdAt;

    private Long subjectId;
    private String subjectName;

    private Long goalId;
    private String goalDescription;

    private Long observationId;

    public TreatmentPlanResponseDto(TreatmentPlan plan) {
        this.id = plan.getId();
        this.plan = plan.getPlan();
        this.notes = plan.getNotes();
        this.nextReview = plan.getNextReview();
        this.createdAt = plan.getCreatedAt();

        this.subjectId = plan.getSubject().getId();
        this.subjectName = plan.getSubject().getName();

        if (plan.getGoal() != null) {
            this.goalId = plan.getGoal().getId();
            this.goalDescription = plan.getGoal().getDescription();
        }

        if (plan.getObservation() != null) {
            this.observationId = plan.getObservation().getId();
        }
    }

    // Getters only
    public Long getId() { return id; }
    public String getPlan() { return plan; }
    public String getNotes() { return notes; }
    public LocalDateTime getNextReview() { return nextReview; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Long getSubjectId() { return subjectId; }
    public String getSubjectName() { return subjectName; }
    public Long getGoalId() { return goalId; }
    public String getGoalDescription() { return goalDescription; }
    public Long getObservationId() { return observationId; }
}