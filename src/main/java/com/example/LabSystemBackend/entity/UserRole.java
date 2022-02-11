package com.example.LabSystemBackend.entity;

/**
 * @version 1.0
 * @author Cong Liu, Siyan Li
 *
 * User role
 */
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
