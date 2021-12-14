package com.example.LabSystemBackend.service;

import com.example.LabSystemBackend.entity.Notification;

import java.util.List;

public interface NotificationService {
    //新建一个消息
    int sendNews(int senderId, int receiverId, String content);

    //获取所有消息
    List<Notification> getAllNews();

    //获取当前按用户所有收到的消息
    Notification getUserAllNews(int receiverId);

    int sendToAllAdmin(String content);
}
