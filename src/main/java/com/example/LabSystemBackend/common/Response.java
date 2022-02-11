package com.example.LabSystemBackend.common;

import java.io.Serializable;

/**
 * @version 1.0
 * @author Cong Liu, Siyan Li
 *
 * Generic return data type
 */
public class Response<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private int resultCode;
    private String message;
    private String token;
    private String email;
    private String firstName;
    private String lastName;
    private boolean isUserLogged;
    private boolean isAdminLogged;
    private T data;

    public Response() {
    }

    public Response(int resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }
    public boolean isUserLogged() {
        return isUserLogged;
    }

    public boolean isAdminLogged() {
        return isAdminLogged;
    }

    public void setAdminLogged(boolean adminLogged) {
        isAdminLogged = adminLogged;
    }

    public void setUserLogged(boolean userLogged) {
        isUserLogged = userLogged;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Response<T> failure(String code) {
        return new Response<>(500, "server error");
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Response{" +
                "resultCode=" + resultCode +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}