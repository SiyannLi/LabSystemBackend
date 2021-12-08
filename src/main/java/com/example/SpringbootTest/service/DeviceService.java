package com.example.SpringbootTest.service;

import com.example.SpringbootTest.entity.Device;

import java.util.List;

public interface DeviceService {

    List<Device> getAllDevicesAndAmount();


    Device addDevice(String deviceName, int Amount, String links);


    Device deleteDevice(int deviceId);


    Device changeDeviceAmount(int deviceId, int newAmount);

    List<Device> mergeDevice(String deviceName, String link, int submitterId, String targetdevice);
}
