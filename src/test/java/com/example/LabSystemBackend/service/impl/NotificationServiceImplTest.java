package com.example.LabSystemBackend.service.impl;

import com.example.LabSystemBackend.dao.NotificationDao;
import com.example.LabSystemBackend.entity.Notification;
import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.service.NotificationService;
import com.example.LabSystemBackend.service.UserService;
import com.example.LabSystemBackend.util.DataGenerate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NotificationServiceImplTest {

    @Autowired
    UserService userService;
    @Autowired
    NotificationService notificationService;
    @Autowired
    NotificationDao notificationDao;

    @Test
    void sendNotification() {
        User recipient = userService.getAllUsers().get(0);
        User sender = userService.getAllUsers().get(1);
        Notification notification = DataGenerate.generateNotification();
        notification.setRecipientId(recipient.getUserId());
        notification.setSenderId(sender.getUserId());
        notificationService.sendNotification(notification);
        assertNotNull(notification.getNotificationId());
    }
    @Autowired
    private JavaMailSender mailSender;
    @Test
    void email(){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("tecolabsystem@outlook.com");
        message.setTo("keyi.li@student.kit.edu");
        message.setSubject("subject");
        message.setText("text");
        mailSender.send(message);
    }

    @Test
    void getAllNotification() {
        List<Notification> notifications = notificationService.getAllNotification();
        assertNotNull(notifications);
    }

    @Test
    void getUserAllNotification() {
        int id = notificationDao.getLastNotification().getRecipientId();
        List<Notification> notifications = notificationService.getUserAllNotification(id);
        for (Notification no : notifications) {
            assertEquals(id, no.getRecipientId());

        }
    }

    @Test
    void sendToAllAdmin() {
        Notification noti = DataGenerate.generateNotification();
        int rows = notificationService.sendToAllAdmin(noti.getSubject(),noti.getContent());
        assertNotEquals(0,rows);
    }
}