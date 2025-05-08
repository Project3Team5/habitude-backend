package com.habitude.dto;

import com.habitude.model.Observation.Intensity;

import java.time.LocalDateTime;

public class ObservationUpdateDto {

    private String behavior;
    private String context;
    private Integer duration;
    private Integer frequency;
    private Intensity intensity;
    private LocalDateTime timestamp;

    // Getters and Setters

    public String getBehavior() { return behavior; }
    public void setBehavior(String behavior) { this.behavior = behavior; }

    public String getContext() { return context; }
    public void setContext(String context) { this.context = context; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    public Integer getFrequency() { return frequency; }
    public void setFrequency(Integer frequency) { this.frequency = frequency; }

    public Intensity getIntensity() { return intensity; }
    public void setIntensity(Intensity intensity) { this.intensity = intensity; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}