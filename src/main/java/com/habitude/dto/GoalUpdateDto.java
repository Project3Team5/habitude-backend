package com.habitude.dto;

import java.time.LocalDate;

public class GoalUpdateDto {

    private String description;
    private LocalDate targetDate;
    private String status;

    // Getters and setters

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getTargetDate() { return targetDate; }
    public void setTargetDate(LocalDate targetDate) { this.targetDate = targetDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}