package com.example.LabSystemBackend.service.impl;

import com.example.LabSystemBackend.dao.NotificationDao;
import com.example.LabSystemBackend.entity.Notification;
import com.example.LabSystemBackend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationDao notificationDao;

    @Override
    public int sendNotification(int receiverId, String content) {
        return notificationDao.insertNotification(new Notification());
    }

    @Override
    public List<Notification> getAllNotification() {
        return notificationDao.getAllNotification();
    }

    @Override
    public List<Notification> getUserAllNotification(int receiverId) {
        return notificationDao.getUserAllNotification(receiverId);
    }

    @Override
    public int sendToAllAdmin(String content) {
        return 0;
    }
}
