package com.example.SpringbootTest.common;

import java.io.Serializable;

public class Response<Object> implements Serializable {
    private static final long serialVersionUID = 1L;
    private int resultCode;
    private String message;
    private Object data;

    public Response() {
    }

    public Response(int resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
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

    public void setData(Object data) {
        this.data = data;
    }

    public Response<Object> failure(String code) {
        return new Response<>(500, "server error");
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