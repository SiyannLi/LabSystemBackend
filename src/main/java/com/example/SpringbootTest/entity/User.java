package com.example.SpringbootTest.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private int userId;

    private String userName;

    private String email;

    private String userRole;

    private int newsBoxId;

    private String userPassword;

    private String realName;

    private int verifyCode;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public int getNewsBoxId() {
        return newsBoxId;
    }

    public void setNewsBoxId(int newsBoxId) {
        this.newsBoxId = newsBoxId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(int verifyCode) {
        this.verifyCode = verifyCode;
    }

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
