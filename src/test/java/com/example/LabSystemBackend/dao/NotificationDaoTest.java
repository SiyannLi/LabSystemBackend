package com.example.LabSystemBackend.dao;

import com.example.LabSystemBackend.entity.Notification;
import com.example.LabSystemBackend.util.DataGenerate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NotificationDaoTest {
    @Autowired
    NotificationDao notificationDao;

    @Test
    void insertNotification() {
        Notification notification = DataGenerate.generateNotification();
        notificationDao.insertNotification(notification);
        assertNotNull(notification.getNotificationId());
    }

    @Test
    void getAllNotification() {
        List<Notification> notifications = notificationDao.getAllNotification();
        assertNotNull(notifications);
    }

    @Test
    void getUserAllNotification() {
        Integer userId = notificationDao.getLastNotification().getRecipientId();
        List<Notification> notifications = notificationDao.getUserAllNotification(userId);
        assertEquals(userId, notifications.get(0).getRecipientId());
    }

    @Test
    void addFakeData() {
        for (int i = 0; i < 10; i++) {
            Notification notification = DataGenerate.generateNotification();
            notificationDao.insertNotification(notification);
        }
    }

}