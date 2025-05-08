package com.habitude.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class SubjectCreateDto {

    @NotBlank
    private String name;

    @NotNull
    private LocalDateTime dob;

    private String notes;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDateTime getDob() { return dob; }
    public void setDob(LocalDateTime dob) { this.dob = dob; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}