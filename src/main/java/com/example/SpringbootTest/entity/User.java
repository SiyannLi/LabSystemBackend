package com.example.SpringbootTest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private Integer userId;

    private String userName;

    private String email;

    private UserRole userRole;

    private String userPassword;

    private String realName;

    private Integer verifyCode;

    @Override
    public String toString() {
        return "User{" +
                "userid=" + userId + "\n" +
                "userName=" + userName + "\n" +
                "email=" + email + "\n" +
                "userRole=" + userRole.toString() + "\n" +
                "userPassword=" + userPassword + "\n" +
                "realName=" + realName + "\n" +
                "verifyCode=" + verifyCode +
                "}";
    }
}
