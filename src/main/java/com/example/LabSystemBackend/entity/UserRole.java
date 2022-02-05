package com.example.LabSystemBackend.entity;

public enum UserRole {
    USER("user"),
    ADMIN("admin"),
    SUPER_ADMIN("super admin");

    private final String roleValue;


    UserRole(String roleValue) {
        this.roleValue = roleValue;
    }

    public String getRoleValue() {
        return this.roleValue;
    }
}
