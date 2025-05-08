package com.habitude.dto;

import java.time.LocalDateTime;

public class TreatmentPlanUpdateDto {

    private String plan;
    private LocalDateTime nextReview;
    private String notes;

    // Getters and Setters
    public String getPlan() { return plan; }
    public void setPlan(String plan) { this.plan = plan; }

    public LocalDateTime getNextReview() { return nextReview; }
    public void setNextReview(LocalDateTime nextReview) { this.nextReview = nextReview; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}