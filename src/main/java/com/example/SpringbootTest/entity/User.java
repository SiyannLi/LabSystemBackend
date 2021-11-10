package com.example.SpringbootTest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private int userId;

    private String userName;

    private String email;

    private String userRole;

    private int newsBoxId;

    private String userPassword;

    private String realName;

    private int verifyCode;

    @Override
    public String toString() {
        return "User{" +
                "userid=" + userId + "\n" +
                "userName=" + userName + "\n" +
                "email=" + email + "\n" +
                "userRole=" + userRole + "\n" +
                "newsBoxId=" + newsBoxId + "\n" +
                "userPassword=" + userPassword + "\n" +
                "realName=" + realName + "\n" +
                "verifyCode=" + verifyCode +
                "}";
    }
}
