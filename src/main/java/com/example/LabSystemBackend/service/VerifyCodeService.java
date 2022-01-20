package com.example.LabSystemBackend.service;

import com.example.LabSystemBackend.entity.Notification;

public interface VerifyCodeService {
    String getRandomVerCode();
    Notification sendVerifyCode(String email, String code);
    boolean checkVerifyCode(String input, String verCode);
}
