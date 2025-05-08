package com.habitude.dto;

import com.habitude.model.Observation;

import java.time.LocalDateTime;

public class ObservationResponseDto {

    private Long id;
    private String behavior;
    private String context;
    private Integer duration;
    private Integer frequency;
    private String intensity;
    private LocalDateTime timestamp;
    private LocalDateTime createdAt;

    private Long subjectId;
    private String subjectName;

    private Long observerId;
    private String observerName;

    public ObservationResponseDto(Observation obs) {
        this.id = obs.getId();
        this.behavior = obs.getBehavior();
        this.context = obs.getContext();
        this.duration = obs.getDuration();
        this.frequency = obs.getFrequency();
        this.intensity = obs.getIntensity() != null ? obs.getIntensity().name() : null;
        this.timestamp = obs.getTimestamp();
        this.createdAt = obs.getCreatedAt();

        this.subjectId = obs.getSubject().getId();
        this.subjectName = obs.getSubject().getName();

        this.observerId = obs.getObserver().getId();
        this.observerName = obs.getObserver().getName();
    }

    // Getters only

    public Long getId() { return id; }
    public String getBehavior() { return behavior; }
    public String getContext() { return context; }
    public Integer getDuration() { return duration; }
    public Integer getFrequency() { return frequency; }
    public String getIntensity() { return intensity; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public Long getSubjectId() { return subjectId; }
    public String getSubjectName() { return subjectName; }

    public Long getObserverId() { return observerId; }
    public String getObserverName() { return observerName; }
}