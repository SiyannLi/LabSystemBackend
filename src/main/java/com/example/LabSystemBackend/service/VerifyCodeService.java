package com.example.LabSystemBackend.service;

import com.example.LabSystemBackend.entity.Notification;

import javax.mail.MessagingException;

/**
 * @version 1.0
 * @author Cong Liu
 *
 * Verification Code Service
 */
public interface VerifyCodeService {
    String getRandomVerCode();
    Notification sendVerifyCode(String email, String code) throws MessagingException;
    boolean checkVerifyCode(String input, String verCode);
}
