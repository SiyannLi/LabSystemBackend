package com.example.LabSystemBackend.service;

import com.example.LabSystemBackend.entity.Notification;

import java.util.List;

public interface NotificationService {

    int sendNotification(Notification notification);

    List<Notification> getAllNotification();

    List<Notification> getUserAllNotification(int recipientId);

    int sendToAllAdmin(String subject, String content);

}
