package com.example.SpringbootTest.common;
import org.springframework.util.StringUtils;

public class ResultGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";
    private static final String DEFAULT_FAIL_MESSAGE = "FAIL";
    public static Result<Object> genSuccessResult() {
        Result<Object> result = new Result<>();
        result.setResultCode(Constants.RESULT_CODE_SUCCESS);
        result.setMessage(DEFAULT_SUCCESS_MESSAGE);
        return result;
    }

    public static Result<Object> genSuccessResult(String message) {
        Result<Object> result = new Result<>();
        result.setResultCode(Constants.RESULT_CODE_SUCCESS);
        result.setMessage(message);
        return result;
    }

    public static Result<Object> genSuccessResult(Object data) {
        Result<Object> result = new Result<>();
        result.setResultCode(Constants.RESULT_CODE_SUCCESS);
        result.setMessage(DEFAULT_SUCCESS_MESSAGE);
        result.setData(data);
        return result;
    }

    public static Result<Object> genFailResult(String message) {
        Result<Object> result = new Result<>();
        result.setResultCode(Constants.RESULT_CODE_SERVER_ERROR);
        if (StringUtils.isEmpty(message)) {
            result.setMessage(DEFAULT_FAIL_MESSAGE);
        } else {
            result.setMessage(message);
        }
        return result;
    }

    public static Result<Object> genNullResult(String message) {
        Result<Object> result = new Result<>();
        result.setResultCode(Constants.RESULT_CODE_BAD_REQUEST);
        result.setMessage(message);
        return result;
    }

    public static Result<Object> genErrorResult(int code, String message) {
        Result<Object> result = new Result<>();
        result.setResultCode(code);
        result.setMessage(message);
        return result;
    }
}
