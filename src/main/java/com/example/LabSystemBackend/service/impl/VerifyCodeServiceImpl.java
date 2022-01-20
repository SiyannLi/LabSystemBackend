package com.example.LabSystemBackend.service.impl;

import com.example.LabSystemBackend.entity.Notification;
import com.example.LabSystemBackend.entity.NotificationTemplate;
import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.service.NotificationService;
import com.example.LabSystemBackend.service.UserService;
import com.example.LabSystemBackend.service.VerifyCodeService;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {
    private static final int ID_OF_UNREGISTERED = -1;
    private NotificationService notificationService;
    private UserService userService;

    @Override
    public String getRandomVerCode() {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < 6; i++) {

            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    @Override
    public Notification sendVerifyCode(String email, String code) {

        Notification notification = new Notification();
        notification.setSenderId(0);
        if(userService.emailExists(email)) {
            User recipient = userService.getUserByEmail(email);
            notification.setRecipientId(recipient.getUserId());
        } else {
            notification.setRecipientId(ID_OF_UNREGISTERED);
        }

        notification.setContent(String.format(NotificationTemplate.VERIFICATION_CODE.getContent(), code));
        notification.setSubject(NotificationTemplate.VERIFICATION_CODE.getSubject());

        notificationService.sendNotification(notification);
        return notification;
    }

    @Override
    public boolean checkVerifyCode(String input, String verCode) {
        return input.equals(verCode);
    }
}
