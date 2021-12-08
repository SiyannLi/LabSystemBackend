package com.example.LabSystemBackend.common;
import org.springframework.util.StringUtils;

public class ResponseGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";
    private static final String DEFAULT_FAIL_MESSAGE = "FAIL";
    public static Response<Object> genSuccessResult() {
        Response<Object> response = new Response<>();
        response.setResultCode(Constants.RESULT_CODE_SUCCESS);
        response.setMessage(DEFAULT_SUCCESS_MESSAGE);
        return response;
    }

    public static Response<Object> genSuccessResult(String message) {
        Response<Object> response = new Response<>();
        response.setResultCode(Constants.RESULT_CODE_SUCCESS);
        response.setMessage(message);
        return response;
    }

    public static Response<Object> genSuccessResult(Object data) {
        Response<Object> response = new Response<>();
        response.setResultCode(Constants.RESULT_CODE_SUCCESS);
        response.setMessage(DEFAULT_SUCCESS_MESSAGE);
        response.setData(data);
        return response;
    }

    public static Response<Object> genFailResult(String message) {
        Response<Object> response = new Response<>();
        response.setResultCode(Constants.RESULT_CODE_SERVER_ERROR);
        if (StringUtils.isEmpty(message)) {
            response.setMessage(DEFAULT_FAIL_MESSAGE);
        } else {
            response.setMessage(message);
        }
        return response;
    }

    public static Response<Object> genNullResult(String message) {
        Response<Object> response = new Response<>();
        response.setResultCode(Constants.RESULT_CODE_BAD_REQUEST);
        response.setMessage(message);
        return response;
    }

    public static Response<Object> genErrorResult(int code, String message) {
        Response<Object> response = new Response<>();
        response.setResultCode(code);
        response.setMessage(message);
        return response;
    }
}
