package com.example.LabSystemBackend.dao;

import com.example.LabSystemBackend.entity.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationDao {
    int insertNews(@Param("notification") Notification notification);

    List<Notification> getAllNews();

    Notification getUserAllNews(@Param("receiverId") int receiverId);


}
