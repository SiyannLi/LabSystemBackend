package com.example.LabSystemBackend.service;

import com.example.LabSystemBackend.entity.Notification;
import com.example.LabSystemBackend.entity.Order;
import com.example.LabSystemBackend.ui.NotificationTemplate;

import javax.mail.MessagingException;
import java.util.List;

public interface NotificationService {

    int sendNotification(Notification notification);

    int sendNotification(String email, Notification notification)throws MessagingException;

    int sendNotificationByTemplate(String email, NotificationTemplate template, String userName) throws MessagingException;

    int sendNotificationByTemplate(String email, NotificationTemplate template, String userName, String opEmail) throws MessagingException;

    int sendNotificationByTemplate(String email, NotificationTemplate template, String userName
            , Order order) throws MessagingException;

    int sendNotificationByTemplate(String email, NotificationTemplate template, String userName, String opEmail
            , Order order) throws MessagingException;
    public int sendNotificationAddOrDeleteAppointment(String email, NotificationTemplate template, String userName,
                                                      String timeSlot, String date, String operationName) throws MessagingException;

    List<Notification> getAllNotification();

    List<Notification> getUserAllNotification(int recipientId);

    int sendToAllAdmin(String subject, String content);

}
