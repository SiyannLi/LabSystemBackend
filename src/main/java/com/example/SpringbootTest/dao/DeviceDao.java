package com.example.SpringbootTest.dao;

import com.example.SpringbootTest.entity.Device;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceDao {
    List<Device> getAllDevices();

    Device getDeviceByName(@Param("deviceName") String deviceName);

    Device getDeviceById(@Param("deviceId") int deviceId);

    int deleteDevice(@Param("deviceId") int deviceId);

    int changeDeviceAmount(@Param("deviceId") int deviceId, @Param("amount") int amount);

    int mergeDevice(@Param("deviceId") int deviceId, @Param("targetId") int targetId);


}
