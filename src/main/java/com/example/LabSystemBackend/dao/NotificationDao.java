package com.example.LabSystemBackend.dao;

import com.example.LabSystemBackend.entity.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version 1.0
 * @author  Siyan Li
 */
@Mapper
public interface NotificationDao {
    int insertNotification(@Param("noti") Notification notification);

    List<Notification> getAllNotification();

    List<Notification> getUserAllNotification(@Param("recipientId") int recipientId);

    Notification getLastNotification();
}
