package com.habitude.model;
import java.time.LocalDateTime;

public class Observation {
    private String type;
    private String description;
    private double duration;
    private double frequency;
    private LocalDateTime timeStamp;

    public Observation(String type, String description, double duration, double frequency) {
        this.type = type;
        this.description = description;
        this.duration = duration;
        this.frequency = frequency;
        this.timeStamp = LocalDateTime.now();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }
}