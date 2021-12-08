package com.example.LabSystemBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private Integer userId;

    private String firstName;

    private String lastName;

    private String email;

    private UserRole userRole;

    private String userPassword;

    private Integer verifyCode;

    private UserAccountStatus userAccountStatus;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", userRole=" + userRole +
                ", userPassword='" + userPassword + '\'' +
                ", verifyCode=" + verifyCode +
                ", userAccountStatus=" + userAccountStatus +
                '}';
    }
}
