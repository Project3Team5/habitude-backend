package com.habitude.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.habitude.model.User;   // ← make sure you import your User entity

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The parent/therapist who “owns” this subject
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Child’s name
     */
    @Column(nullable = false)
    private String name;

    /**
     * Date of birth
     */
    @Column(nullable = false)
    private LocalDateTime dob;

    /**
     * Any free-text notes
     */
    @Column(columnDefinition = "TEXT")
    private String notes;

    /**
     * When this subject record was created
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy="subject", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Goal> goals = new ArrayList<>();
    @OneToMany(mappedBy="subject", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Observation> observations = new ArrayList<>();
    // JPA requires a no-arg constructor
    public Subject() {
    }

    // (Optional) convenience constructor
    public Subject(User user, String name, LocalDateTime dob, String notes) {
        this.user = user;
        this.name = name;
        this.dob = dob;
        this.notes = notes;
        this.createdAt = LocalDateTime.now();
    }

    // ——————— getters & setters ———————

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDob() {
        return dob;
    }

    public void setDob(LocalDateTime dob) {
        this.dob = dob;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Transient
    public int getAge() {
        return java.time.Period.between(this.dob.toLocalDate(), LocalDateTime.now().toLocalDate()).getYears();
    }
}

