package com.example.SpringbootTest.service.impl;

import com.example.SpringbootTest.dao.DeviceDao;
import com.example.SpringbootTest.entity.Device;
import com.example.SpringbootTest.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    private DeviceDao deviceDao;
    @Override
    public List<Device> getAllDevicesAndAmount() {
        return null;
    }

    @Override
    public Device addDevice(String deviceName, int Amount, String links) {
        return null;
    }

    @Override
    public Device deleteDevice(int deviceId) {
        return null;
    }

    @Override
    public Device changeDeviceAmount(int deviceId, int newAmount) {
        return null;
    }

    @Override
    public List<Device> mergeDevice(String deviceName, String link, int submitterId, String targetdevice) {
        return null;
    }
}
