package com.example.LabSystemBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @version 1.0
 * @author Cong Liu, Siyan Li
 *
 * User
 */
@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @Transient
    public static final int ID_OF_SYSTEM = 0;
    @Transient
    public static final int ID_OF_UNREGISTERED = -1;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "userId", insertable=false, updatable=false, nullable = false)
    private Integer userId;
    @Column(name = "firstName", length = 64)
    private String firstName;
    @Column(name = "lastName", length = 64)
    private String lastName;
    @Column(name = "email", length = 64)
    private String email;
    @Column(name = "userRole", length = 64)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    @Column(name = "userPassword", length = 64)
    private String userPassword;
    @Column(name = "userAccountStatus", length = 64)
    @Enumerated(EnumType.STRING)
    private UserAccountStatus userAccountStatus;
    @Column(name = "receiveBulkEmail", length = 1, columnDefinition = "tinyint")
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
