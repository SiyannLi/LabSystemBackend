package com.example.LabSystemBackend.service.impl;

import com.example.LabSystemBackend.entity.Notification;
import com.example.LabSystemBackend.ui.NotificationTemplate;
import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.service.NotificationService;
import com.example.LabSystemBackend.service.UserService;
import com.example.LabSystemBackend.service.VerifyCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Random;
/**
 * @version 1.0
 * @author Cong Liu
 *
 * Implement of Verification Code Service
 */
@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {
    @Autowired
    private NotificationService notificationService;
    @Autowired
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
    public Notification sendVerifyCode(String email, String code) throws MessagingException {

        Notification notification = new Notification();
        notification.setSenderId(User.ID_OF_SYSTEM);
        notification.setContent(String.format(NotificationTemplate.VERIFICATION_CODE.getContent(), code));
        notification.setSubject(NotificationTemplate.VERIFICATION_CODE.getSubject());
        if(userService.emailExists(email)) {
            User recipient = userService.getUserByEmail(email);
            notification.setRecipientId(recipient.getUserId());
        } else {
            notification.setRecipientId(User.ID_OF_UNREGISTERED);
        }
        notificationService.sendNotification(email, notification);
        return notification;
    }

    @Override
    public boolean checkVerifyCode(String input, String verCode) {
        return input.equals(verCode);
    }
}
