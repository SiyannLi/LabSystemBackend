package com.example.SpringbootTest.service;

import com.example.SpringbootTest.entity.Device;

import java.util.List;

public interface DeviceService {
    //获取所有设备和数量
    List<Device> getAllDevicesAndAmount();

    //新增一个设备
    Device addDevice(String deviceName, int Amount, String links);

    //删除一个设备
    Device deleteDevice(int deviceId);

    //修改设备数量
    Device changeDeviceAmount(int deviceId, int newAmount);

    //合并设备列表
    List<Device> mergeDevice(String deviceName, String link, int submitterId, String targetdevice);
}
