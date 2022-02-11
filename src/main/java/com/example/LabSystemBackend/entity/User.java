package com.example.LabSystemBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @version 1.0
 * @author Cong Liu, Siyan Li
 *
 * User
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    public static final int ID_OF_SYSTEM = 0;
    public static final int ID_OF_UNREGISTERED = -1;

    private Integer userId;

    private String firstName;

    private String lastName;

    private String email;

    private UserRole userRole;

    private String userPassword;

    private UserAccountStatus userAccountStatus;

    private boolean receiveBulkEmail;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", userRole=" + userRole + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userAccountStatus=" + userAccountStatus + + '\'' +
                ", receiveBulkEmail=" + receiveBulkEmail + + + '\'' +
                '}';
    }
}
