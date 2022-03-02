package com.example.LabSystemBackend.dao;

import com.example.LabSystemBackend.entity.Notification;
import com.example.LabSystemBackend.util.DataGenerate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
/**
 * @version 1.0
 * @author  Sheyang Li
 *
 * NotificationDao Test
 */
@ActiveProfiles("unittest")
@RunWith(SpringRunner.class)
@SpringBootTest
@javax.transaction.Transactional
@Rollback(value = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NotificationDaoTest {
    @Autowired
    NotificationDao notificationDao;

    int[] senderId = new int[]{1,1};
    int[] recipientId = new int[]{2,3};
    String[] subject = new String[]{"good news", "bad news"};
    String[]  content = new String[]{"this is your Money","your order"};

    @Test
    @Transactional
    void insertNotification() {

        Notification newNotification = DataGenerate.generateNotification();
        notificationDao.insertNotification(newNotification);
        assertEquals(subject.length+1, newNotification.getNotificationId());
    }

    @Test
    @Transactional
    void getAllNotification() {

            List<Notification> notifications = notificationDao.getAllNotification();
            assertEquals(subject.length,notifications.size());
            assertNotNull(notifications);

    }
        @Test
        @Transactional
        void getUserAllNotification() {
            int id = new Random().nextInt(subject.length) + 1;
            int testUserId = recipientId[id-1];
                List<Notification> notifications = notificationDao.getUserAllNotification(testUserId);
                assertEquals(1, notifications.size());
        }

        @Test
        @Transactional
        void getLastNotification() {
            Notification testNotification = notificationDao.getLastNotification();
            int num = subject.length;
            assertAll("notification",
                    () -> assertEquals(senderId[num - 1], notificationDao.getLastNotification().getSenderId()),
                    () -> assertEquals(recipientId[num - 1], notificationDao.getLastNotification().getRecipientId()),
                    () -> assertTrue(testNotification.getContent().equals(content[num-1]))
            );
        }
    }