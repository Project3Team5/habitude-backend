package com.habitude.dto;

import com.habitude.model.Subject;

import java.time.LocalDateTime;

public class SubjectResponseDto {

    private Long id;
    private String name;
    private LocalDateTime dob;
    private int age;
    private String notes;
    private LocalDateTime createdAt;

    private Long userId;
    private String userName;

    public SubjectResponseDto(Subject subject) {
        this.id = subject.getId();
        this.name = subject.getName();
        this.dob = subject.getDob();
        this.age = subject.getAge();
        this.notes = subject.getNotes();
        this.createdAt = subject.getCreatedAt();
        this.userId = subject.getUser().getId();
        this.userName = subject.getUser().getName();
    }

    // Getters only
    public Long getId() { return id; }
    public String getName() { return name; }
    public LocalDateTime getDob() { return dob; }
    public int getAge() { return age; }
    public String getNotes() { return notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Long getUserId() { return userId; }
    public String getUserName() { return userName; }
}