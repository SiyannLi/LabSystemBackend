package com.example.LabSystemBackend.common;

import org.springframework.util.StringUtils;

public class ResponseGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";
    private static final String DEFAULT_FAIL_MESSAGE = "FAIL";

    public static Response genSuccessResult() {
        Response response = new Response<>();
        response.setResultCode(Constants.RESULT_CODE_SUCCESS);
        response.setMessage(DEFAULT_SUCCESS_MESSAGE);
        return response;
    }

    public static Response genSuccessResult(String message) {
        Response<Object> response = new Response<>();
        response.setResultCode(Constants.RESULT_CODE_SUCCESS);
        response.setMessage(message);
        return response;
    }

    public static Response genSuccessResultStringData(String data) {
        Response<Object> response = new Response<>();
        response.setResultCode(Constants.RESULT_CODE_SUCCESS);
        response.setData(data);
        return response;
    }

    public static Response genSuccessResult(String token, String message) {
        Response<Object> response = new Response<>();
        response.setResultCode(Constants.RESULT_CODE_SUCCESS);
        response.setToken(token);
        response.setMessage(message);
        return response;
    }

    public static Response genSuccessResult(String token, String firstName, String lastName, String email
            , boolean isUserLogged, boolean isAdminLogged) {
        Response<Object> response = new Response<>();
        response.setResultCode(Constants.RESULT_CODE_SUCCESS);
        response.setToken(token);
        response.setEmail(email);
        response.setFirstName(firstName);
        response.setLastName(lastName);
        response.setUserLogged(isUserLogged);
        response.setAdminLogged(isAdminLogged);

        return response;
    }



    public static Response genSuccessResult(String token, Object data) {
        Response<Object> response = new Response<>();
        response.setResultCode(Constants.RESULT_CODE_SUCCESS);
        response.setMessage(DEFAULT_SUCCESS_MESSAGE);
        response.setToken(token);
        response.setData(data);
        return response;
    }

    public static Response genSuccessResult(String token, boolean isReceiveBulkEmail) {
        Response<Object> response = new Response<>();
        response.setResultCode(Constants.RESULT_CODE_SUCCESS);
        response.setMessage(DEFAULT_SUCCESS_MESSAGE);
        response.setToken(token);
        response.setReceiveBulkEmail(isReceiveBulkEmail);
        return response;
    }


    public static Response genSuccessResult(Object data) {
        Response<Object> response = new Response<>();
        response.setResultCode(Constants.RESULT_CODE_SUCCESS);
        response.setMessage(DEFAULT_SUCCESS_MESSAGE);
        response.setData(data);
        return response;
    }

    public static Response genFailResult(String message) {
        Response<Object> response = new Response<>();
        response.setResultCode(Constants.RESULT_CODE_SERVER_ERROR);
        if (StringUtils.isEmpty(message)) {
            response.setMessage(DEFAULT_FAIL_MESSAGE);
        } else {
            response.setMessage(message);
        }
        return response;
    }

    public static Response genFailResultToken(String message) {
        Response<Object> response = new Response<>();
        response.setResultCode(Constants.RESULT_CODE_NOT_LOGIN);
        if (StringUtils.isEmpty(message)) {
            response.setMessage(DEFAULT_FAIL_MESSAGE);
        } else {
            response.setMessage(message);
        }
        return response;
    }


    public static Response genFailResult(String token, String message) {
        Response<Object> response = new Response<>();
        response.setResultCode(Constants.RESULT_CODE_SERVER_ERROR);
        if (StringUtils.isEmpty(message)) {
            response.setMessage(DEFAULT_FAIL_MESSAGE);
        } else {
            response.setMessage(message);
        }
        response.setToken(token);
        return response;
    }

    public static Response genNullResult(String message) {
        Response<Object> response = new Response<>();
        response.setResultCode(Constants.RESULT_CODE_BAD_REQUEST);
        response.setMessage(message);
        return response;
    }

    public static Response genErrorResult(int code, String message) {
        Response response = new Response<>();
        response.setResultCode(code);
        response.setMessage(message);
        return response;
    }
}
