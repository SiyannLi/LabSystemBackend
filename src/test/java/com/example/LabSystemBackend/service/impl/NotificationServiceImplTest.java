package com.example.LabSystemBackend.service.impl;

import com.example.LabSystemBackend.dao.NotificationDao;
import com.example.LabSystemBackend.entity.*;
import com.example.LabSystemBackend.service.NotificationService;
import com.example.LabSystemBackend.service.UserService;
import com.example.LabSystemBackend.ui.NotificationTemplate;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
/**
 * @version 1.0
 * @author  Sheyang Li
 *
 * ItemServiceImpl Test
 */
@ActiveProfiles("unittest")
@RunWith(SpringRunner.class)
@SpringBootTest
class NotificationServiceImplTest {
    @Autowired
    NotificationService notificationService;
    @MockBean
    NotificationDao notificationDao;
    @MockBean
    UserService userService;
    User user = new User(5,"firstName","lastName","test_user@testpse.com",
            UserRole.ADMIN,"a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3",
            UserAccountStatus.ACTIVE,true);
    // registeredUser email
    String email = "sheyang-li@hotmail.com";
    // admin email
    String opEmail = "Labsystem22@outlook.com";
    String userName ="testName";
    // not registeredUser
    String email_Not = "test_Not@hotmail11023-not.com";
    Notification notificationtest = new Notification(1,0,2,"test subject","test content");
    Order order = new Order(1,1,"handy,","www.ebay.de",10,OrderStatus.PENDING,email);
    String timeSlot = "8:00-10:00";
    String date = "2030-01-01";

    @Test
    void sendNotification() {
        Mockito.when(userService.getUser(notificationtest.getRecipientId())).thenReturn(user);
        Mockito.when(notificationDao.insertNotification(notificationtest)).thenReturn(1);
        assertEquals(1, notificationService.sendNotification(notificationtest));
    }

    @Test
    void testSendNotification() throws MessagingException {
        Mockito.when(notificationDao.insertNotification(notificationtest)).thenReturn(1);
        assertEquals(1, notificationService.sendNotification(email, notificationtest));
    }

    private void sendNotificationByTemplate(boolean emailExists, String email, NotificationTemplate template,
                                            String userName) throws MessagingException {
        Mockito.when(userService.getUserByEmail(email)).thenReturn(user);
        Mockito.when(userService.emailExists(Mockito.anyString())).thenReturn(emailExists);
        Mockito.when(notificationDao.insertNotification(Mockito.any())).thenReturn(1);
        assertEquals(1, notificationService.sendNotificationByTemplate(email, template, userName));
    }

    @Test
    void sendNotificationByTemplate() throws MessagingException {
        sendNotificationByTemplate(true, user.getEmail(), NotificationTemplate.VERIFICATION_CODE, user.getFullName()
        );
    }
    @Test
    void sendNotificationByTemplateNoEmail() throws MessagingException {
        sendNotificationByTemplate(false, "No@hotmail.com", NotificationTemplate.VERIFICATION_CODE, user.getFullName()
        );
    }


    private void SendNotificationByTemplateByOpEmail(boolean emailExists, String email, NotificationTemplate template,
                                                     String userName, String opEmail) throws MessagingException {
        Mockito.when(userService.getUserByEmail(email)).thenReturn(user);
        Mockito.when(userService.emailExists(Mockito.anyString())).thenReturn(emailExists);
        Mockito.when(notificationDao.insertNotification(Mockito.any())).thenReturn(1);
        assertEquals(1, notificationService.sendNotificationByTemplate(email, template, userName, opEmail));
    }
    @Test
    void sendNotificationByTemplateByOpEmail() throws MessagingException {
        SendNotificationByTemplateByOpEmail(true, user.getEmail(), NotificationTemplate.VERIFICATION_CODE, user.getFullName()
                , this.opEmail);
    }
    @Test
    void sendNotificationByTemplatebyOpEmailNoEmail() throws MessagingException {
        SendNotificationByTemplateByOpEmail(false, "No@hotmail.com", NotificationTemplate.VERIFICATION_CODE, user.getFullName(), opEmail
        );
    }

    private void testSendNotificationByTemplateByOrder(boolean emailExists, String email, NotificationTemplate template,
                                                       String userName, Order order) throws MessagingException {
        Mockito.when(userService.getUserByEmail(email)).thenReturn(user);
        Mockito.when(userService.emailExists(Mockito.anyString())).thenReturn(emailExists);
        Mockito.when(notificationDao.insertNotification(Mockito.any())).thenReturn(1);
        assertEquals(1, notificationService.sendNotificationByTemplate(email, template, userName, order));
    }
    @Test
    void sendNotificationByTemplateByOder() throws MessagingException {
        testSendNotificationByTemplateByOrder(true, user.getEmail(), NotificationTemplate.VERIFICATION_CODE, user.getFullName()
                , order);
    }
    @Test
    void sendNotificationByTemplateByOrderNoEmail() throws MessagingException {
        testSendNotificationByTemplateByOrder(false, "No@hotmail.com", NotificationTemplate.VERIFICATION_CODE, user.getFullName(), order
        );
    }

    private void testSendNotificationByTemplateByOrderByOpEmail(boolean emailExists, String email, NotificationTemplate template,
                                                                String userName, String opEmail, Order order) throws MessagingException {
        Mockito.when(userService.getUserByEmail(email)).thenReturn(user);
        Mockito.when(userService.emailExists(Mockito.anyString())).thenReturn(emailExists);
        Mockito.when(notificationDao.insertNotification(Mockito.any())).thenReturn(1);
        assertEquals(1, notificationService.sendNotificationByTemplate(email, template, userName, opEmail, order));
    }
    @Test
    void sendNotificationByTemplateByOderByOpEmail() throws MessagingException {
        testSendNotificationByTemplateByOrderByOpEmail(true, user.getEmail(), NotificationTemplate.VERIFICATION_CODE, user.getFullName()
                , opEmail, order);
    }
    @Test
    void sendNotificationByTemplateByOrderByOpEmailNoEmail() throws MessagingException {
        testSendNotificationByTemplateByOrderByOpEmail(false, "No@hotmail.com",
                NotificationTemplate.VERIFICATION_CODE, user.getFullName(), opEmail, order
        );
    }

    private void sendNotificationAddOrDeleteAppointment(boolean emailExists, String email, NotificationTemplate template,
                                                        String userName, String timeSlot, String date,
                                                        String operatorName) throws MessagingException {
        Mockito.when(userService.getUserByEmail(email)).thenReturn(user);
        Mockito.when(userService.emailExists(Mockito.anyString())).thenReturn(emailExists);
        Mockito.when(notificationDao.insertNotification(Mockito.any())).thenReturn(1);
        assertEquals(1, notificationService.sendNotificationAddOrDeleteAppointment(email, template,
                userName, timeSlot, date, operatorName));
    }
    @Test
    void sendNotsendNotificationAddOrDeleteAppointment() throws MessagingException {
        sendNotificationAddOrDeleteAppointment(true, user.getEmail(),
                NotificationTemplate.APPOINTMENT_BOOKED, user.getFullName()
                , timeSlot, date, user.getFullName());
    }
    @Test
    void sendNotificationAddOrDeleteAppointmentNoEmail() throws MessagingException {
        sendNotificationAddOrDeleteAppointment(false, "No@hotmail.com",
                NotificationTemplate.APPOINTMENT_BOOKED, user.getFullName(), timeSlot, date, user.getFullName()
        );
    }
    @Test
    void getAllNotification() {
        List<Notification> notifications = new ArrayList<>();
        notifications.add(notificationtest);
        Mockito.when(notificationDao.getAllNotification()).thenReturn(notifications);
        Assert.assertEquals(notificationtest,notificationService.getAllNotification().get(0));
    }

    @Test
    void getUserAllNotification() {
        List<Notification> notifications = new ArrayList<>();
        notifications.add(notificationtest);
        Mockito.when(notificationDao.getUserAllNotification(Mockito.anyInt())).thenReturn(notifications);
        Assert.assertEquals(notificationtest,notificationService.getUserAllNotification(4).get(0));
    }

    @Test
    void sendToAllAdmin() throws MessagingException {
        List<User> users = new ArrayList<>();
        users.add(user);
        Mockito.when(userService.getAllAdminReceiveBulkEmail()).thenReturn(users);
        Mockito.when(userService.getUserByEmail(Mockito.anyString())).thenReturn(user);
        Mockito.when(userService.emailExists(Mockito.anyString())).thenReturn(true);
        assertEquals(1,notificationService.sendToAllAdmin(NotificationTemplate.NEW_REGISTRATION_REQUEST));

    }
}