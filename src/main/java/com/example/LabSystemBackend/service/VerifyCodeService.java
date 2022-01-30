package com.example.LabSystemBackend.service;

import com.example.LabSystemBackend.entity.Notification;

import javax.mail.MessagingException;

public interface VerifyCodeService {
    String getRandomVerCode();
    Notification sendVerifyCode(String email, String code) throws MessagingException;
    boolean checkVerifyCode(String input, String verCode);
}
