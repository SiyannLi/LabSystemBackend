package com.example.LabSystemBackend.service;

import com.example.LabSystemBackend.entity.Notification;

import java.util.List;

public interface NotificationService {
    //新建一个消息
    int sendNotification(int receiverId, String content);

    List<Notification> getAllNotification();

    List<Notification> getUserAllNotification(int receiverId);

    int sendToAllAdmin(String content);

}
