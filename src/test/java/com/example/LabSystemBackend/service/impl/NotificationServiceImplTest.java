package com.example.LabSystemBackend.service.impl;

import com.example.LabSystemBackend.dao.NotificationDao;
import com.example.LabSystemBackend.entity.Notification;
import com.example.LabSystemBackend.entity.Order;
import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.entity.UserAccountStatus;
import com.example.LabSystemBackend.service.NotificationService;
import com.example.LabSystemBackend.service.UserService;
import com.example.LabSystemBackend.ui.NotificationTemplate;
import com.example.LabSystemBackend.util.DataGenerate;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.example.LabSystemBackend.entity.OrderStatus.CONFIRMED;
import static com.example.LabSystemBackend.entity.OrderStatus.PENDING;
import static com.example.LabSystemBackend.entity.UserRole.ADMIN;
import static com.example.LabSystemBackend.ui.NotificationTemplate.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
/**
 * @version 1.0
 * @author  Sheyang Li
 *
 * NotificationServiceImpl Test
 */
@SpringBootTest
class NotificationServiceImplTest {
    @Mock
    UserService userService;
    @Mock
    Notification notification;
    @Autowired
    NotificationService notificationService;
    @Autowired
    NotificationDao notificationDao;
    // registeredUser email
    String email = "sheyang-li@hotmail.com";
    // admin email
    String opEmail = "Labsystem22@outlook.com";
    String userName ="testName";
    // not registeredUser
    String email_Not = "test_Not@hotmail11023-not.com";
    Notification notificationtest = new Notification(1,0,39,"test subject","test content");
    @Test
    @Transactional
    void sendNotification() {
        int result = notificationService.sendNotification(notificationtest);
        assertTrue(notificationDao.getLastNotification().getSubject().equals("test subject"));
    }
    @Test
    @Transactional
    void testSendNotification() throws MessagingException {
        when(notification.getContent()).thenReturn("test content");
        when(notification.getSubject()).thenReturn("test subject");
        notificationService.sendNotification(email,notification);
        assertTrue(notificationDao.getLastNotification().getSubject().equals("test subject"));
    }

    @Test
    @Transactional
    void testSendNotificationByTemplate_registeredUser() throws MessagingException {

    notificationService.sendNotificationByTemplate(email,NotificationTemplate.RESISTER_FINISHED,userName);
    assertTrue(notificationDao.getLastNotification().getRecipientId() >= 0);
    }

    @Test
    @Transactional
    void testSendNotificationByTemplate_unregisteredUser() throws MessagingException {
        notificationService.sendNotificationByTemplate(email_Not,NotificationTemplate.RESISTER_FINISHED,userName);
        assertTrue(notificationDao.getLastNotification().getRecipientId() < 0);
    }

    @Test
    @Transactional
    void testSendNotificationByTemplate_byopEmail_registeredUser() throws MessagingException {
        notificationService.sendNotificationByTemplate(email,NotificationTemplate.BECOME_ADMIN,userName,opEmail);
        assertTrue(notificationDao.getLastNotification().getRecipientId() >= 0);
    }

    @Test
    @Transactional
    void testSendNotificationByTemplate_byopEmail_unregisteredUser() throws MessagingException {
        notificationService.sendNotificationByTemplate(email_Not,NotificationTemplate.BECOME_ADMIN,userName,opEmail);
        assertTrue(notificationDao.getLastNotification().getRecipientId() < 0);
    }

    @Test
    @Transactional
    void testSendNotificationByTemplate_oder_registeredUser() throws MessagingException {
        Order order = new Order(100,101,"handy","www.ebay.de",20,CONFIRMED,
                email);
        notificationService.sendNotificationByTemplate(email,ORDER_CONFIRMED,userName,order);
        assertTrue(notificationDao.getLastNotification().getRecipientId() >= 0);
    }

    @Test
    @Transactional
    void testSendNotificationByTemplate_oder_unregisteredUser() throws MessagingException {
        Order order = new Order(100,101,"handy","www.ebay.de",20,CONFIRMED,
                email);
        notificationService.sendNotificationByTemplate(email_Not,ORDER_CONFIRMED,userName,order);
        assertTrue(notificationDao.getLastNotification().getRecipientId() < 0);
    }

    @Test
    @Transactional
    void testSendNotificationByTemplate_oder_byopEmail_registeredUser() throws MessagingException {
        Order order = new Order(100,101,"handy","www.ebay.de",20,PENDING,
                email);
        notificationService.sendNotificationByTemplate(email,ORDER_REJECTED,userName,opEmail,order);
        assertTrue(notificationDao.getLastNotification().getRecipientId() >= 0);
    }

    @Test
    @Transactional
    void testSendNotificationByTemplate_oder_byopEmail_unregisteredUser() throws MessagingException {
        Order order = new Order(100,101,"handy","www.ebay.de",20,PENDING,
                email);
        notificationService.sendNotificationByTemplate(email_Not,ORDER_REJECTED,userName,opEmail,order);
        assertTrue(notificationDao.getLastNotification().getRecipientId() < 0);
    }

    @Test
    @Transactional
    void sendNotificationAddOrDeleteAppointment_appointment_registeredUser() throws ParseException, MessagingException {
        int recipientId = notificationDao.getLastNotification().getRecipientId();
        notificationService.sendNotificationAddOrDeleteAppointment(email,APPOINTMENT_BOOKED,userName,"1","2030-01-01","yang");
        assertNotEquals(recipientId,notificationDao.getLastNotification().getRecipientId());
    }

    @Test
    @Transactional
    void sendNotificationAddOrDeleteAppointment_appointment_unregisteredUser() throws ParseException, MessagingException {
        int recipientId = notificationDao.getLastNotification().getRecipientId();
        notificationService.sendNotificationAddOrDeleteAppointment(email_Not,APPOINTMENT_BOOKED,userName,"1","2030-01-01","yang");
        assertTrue(notificationDao.getLastNotification().getRecipientId() < 0);
    }

    @Test
    void testGetAllNotification() {
        List<Notification> notifications = notificationService.getAllNotification();
        assertNotNull(notifications);
    }

    @Test
    void testGetUserAllNotification() {
        for (int i = 0; i < 3; i++) { Notification notification = DataGenerate.generateNotification();
            notificationDao.insertNotification(notification);
        }
        Integer userId = notificationDao.getLastNotification().getRecipientId();
        List<Notification> notifications = notificationService.getUserAllNotification(userId);
        assertEquals(userId, notifications.get(0).getRecipientId());
    }

    @Test
    void testSendToAllAdmin() throws MessagingException {

        List<User> users = new ArrayList<User>();
        User userOne = new User(1,"John", "John", email,
                ADMIN,"", UserAccountStatus.ACTIVE,true);
        User userTwo = new User(2, "Alex", "Bau", email,
                ADMIN,"", UserAccountStatus.ACTIVE,true);
        User userThree = new User(3, "Steve", "Wau", email,
                ADMIN,"", UserAccountStatus.ACTIVE,true);
        users.add(userOne);
        users.add(userTwo);
        users.add(userThree);

        when(userService.getAllAdminReceiveBulkEmail()).thenReturn(users);
        System.out.println(users);
        int i = notificationService.sendToAllAdmin(NotificationTemplate.RESISTER_FINISHED);
        assertEquals(1,i);
    }
    @Before
    public void before(){
        MockitoAnnotations.openMocks(this);

    }

}