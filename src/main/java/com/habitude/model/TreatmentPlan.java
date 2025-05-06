package com.habitude.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "treatment_plans")
public class TreatmentPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Which child this plan is for */
    @ManyToOne(optional = false)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    /** Which goal this plan supports */
    @ManyToOne
    @JoinColumn(name = "goal_id")
    private Goal goal;

    /** link back to a specific observation */
    @ManyToOne
    @JoinColumn(name = "observation_id")
    private Observation observation;

    /** The actual intervention steps */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String plan;

    /** When this plan was created */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    /** When to revisit/update this plan */
    @Column(name = "next_review")
    private LocalDateTime nextReview;

    /** Any extra notes */
    @Column(columnDefinition = "TEXT")
    private String notes;

    // JPA requires a no-arg constructor
    public TreatmentPlan() {}

    // Convenience constructor
    public TreatmentPlan(Subject subject,
                         Goal goal,
                         Observation observation,
                         String plan,
                         LocalDateTime nextReview,
                         String notes) {
        this.subject     = subject;
        this.goal        = goal;
        this.observation = observation;
        this.plan        = plan;
        this.nextReview  = nextReview;
        this.notes       = notes;
        this.createdAt   = LocalDateTime.now();
    }

    // ————— getters & setters —————

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Subject getSubject() {
        return subject;
    }
    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Goal getGoal() {
        return goal;
    }
    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public Observation getObservation() {
        return observation;
    }
    public void setObservation(Observation observation) {
        this.observation = observation;
    }

    public String getPlan() {
        return plan;
    }
    public void setPlan(String plan) {
        this.plan = plan;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getNextReview() {
        return nextReview;
    }
    public void setNextReview(LocalDateTime nextReview) {
        this.nextReview = nextReview;
    }

    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setPlanDetails(String s) {
    }
}
