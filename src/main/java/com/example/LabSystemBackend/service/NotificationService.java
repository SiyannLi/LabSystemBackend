package com.example.LabSystemBackend.service;

import com.example.LabSystemBackend.entity.Notification;
import com.example.LabSystemBackend.ui.NotificationTemplate;

import java.util.List;

public interface NotificationService {

    int sendNotification(Notification notification);

    int sendNotification(String email, Notification notification);

    int sendNotificationByTemplateWithName(String email, NotificationTemplate template, String userName);

    int sendNotificationByTemplateWithOrder(String email, NotificationTemplate template, String userName
            , int orderId);

    List<Notification> getAllNotification();

    List<Notification> getUserAllNotification(int recipientId);

    int sendToAllAdmin(String subject, String content);

}
