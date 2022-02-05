package com.example.LabSystemBackend.ui;

import com.example.LabSystemBackend.common.Response;
import com.example.LabSystemBackend.common.ResponseGenerator;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {

    
    @ExceptionHandler(RuntimeException.class)
    public Response exceptionHandler(RuntimeException ex) {
        return ResponseGenerator.genFailResultToken(ex.getMessage());
    }

}