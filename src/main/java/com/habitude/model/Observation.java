package com.habitude.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "observations")
public class Observation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** which subject this log belongs to */
    @ManyToOne(optional = false)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    /** who logged it */
    @ManyToOne(optional = false)
    @JoinColumn(name = "observer_id")
    private User observer;

    /** the behavior description */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String behavior;

    private String context;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    private Integer duration;  // in seconds
    private Integer frequency; // count
    private Intensity intensity;  // low/medium/high

    public Observation() {}

    public enum Intensity {
        LOW, MEDIUM, HIGH
    }

    @PrePersist
    public void prePersist() {
        if (this.timestamp == null) {
            this.timestamp = LocalDateTime.now();
        }
    }

    // getters & setters

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

    public User getObserver() {
        return observer;
    }
    public void setObserver(User observer) {
        this.observer = observer;
    }

    public String getBehavior() {
        return behavior;
    }
    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }

    public String getContext() {
        return context;
    }
    public void setContext(String context) {
        this.context = context;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getDuration() {
        return duration;
    }
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getFrequency() {
        return frequency;
    }
    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Intensity getIntensity() {
        return intensity;
    }
    public void setIntensity(String intensity) {
        this.intensity = Intensity.valueOf(intensity);
    }
}
