package com.yourname.sample_analysis_tool.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Sample {

    @Id
    private String sampleId;
    private String status;
    private LocalDateTime scannedInTime;
    private LocalDateTime receivedTime;
    private LocalDateTime completedTime;

    // Constructors
    public Sample() {
    }

    public Sample(String sampleId, String status, LocalDateTime scannedInTime) {
        this.sampleId = sampleId;
        this.status = status;
        this.scannedInTime = scannedInTime;
    }

    // Getters and setters
    public String getSampleId() {
        return sampleId;
    }

    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getScannedInTime() {
        return scannedInTime;
    }

    public void setScannedInTime(LocalDateTime scannedInTime) {
        this.scannedInTime = scannedInTime;
    }

    public LocalDateTime getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(LocalDateTime receivedTime) {
        this.receivedTime = receivedTime;
    }

    public LocalDateTime getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(LocalDateTime completedTime) {
        this.completedTime = completedTime;
    }
}
