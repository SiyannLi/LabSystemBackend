package com.example.LabSystemBackend.entity;

/**
 * @version 1.0
 * @author Cong Liu, Siyan Li
 *
 * User account status
 */
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
