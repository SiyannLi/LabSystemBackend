package com.example.LabSystemBackend.dao;

import com.example.LabSystemBackend.entity.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationDao {
    int insertNotification(@Param("notification") Notification notification);

    List<Notification> getAllNotification();

    List<Notification> getUserAllNotification(@Param("receiverId") int receiverId);


}
