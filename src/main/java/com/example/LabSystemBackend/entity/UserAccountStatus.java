package com.example.LabSystemBackend.entity;

public enum UserAccountStatus {
    PENDING("pending"),
    ACTIVE("active"),
    INACTIVE("inactive");

    private final String statusValue;


    UserAccountStatus(String statusValue) {
        this.statusValue = statusValue;
    }

    public String getStatusValue() {
        return this.statusValue;
    }
}
