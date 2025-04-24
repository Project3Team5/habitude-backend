package com.habitude.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "llm_feedback")
public class LLMFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** which observation this feedback is for */
    @ManyToOne(optional = false)
    @JoinColumn(name = "observation_id")
    private Observation observation;

    /** the AI-generated suggestion */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String suggestion;

    /** whether the user accepted it */
    private Boolean accepted;

    /** model’s confidence (0.0–1.0) */
    private Float confidenceScore;

    /** when it was generated */
    @Column(name = "generated_at")
    private LocalDateTime generatedAt = LocalDateTime.now();

    public LLMFeedback() {}

    // — getters & setters —

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Observation getObservation() {
        return observation;
    }
    public void setObservation(Observation observation) {
        this.observation = observation;
    }

    public String getSuggestion() {
        return suggestion;
    }
    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public Boolean getAccepted() {
        return accepted;
    }
    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public Float getConfidenceScore() {
        return confidenceScore;
    }
    public void setConfidenceScore(Float confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }
    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }
}

