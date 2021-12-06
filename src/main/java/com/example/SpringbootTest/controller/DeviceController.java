package com.example.SpringbootTest.controller;


import com.example.SpringbootTest.common.Result;
import com.example.SpringbootTest.service.DeviceService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @ApiOperation("get a list of all devices and their amounts")
    @GetMapping("getAllDevicesAndAmount")
    public Result getAllDevicesAndAmount() {
        return null;
    }


    @ApiOperation("add a new device")
    @PostMapping("addDevice")
    public Result addDevice(String deviceName, int Amount, String links) {
        return null;
    }

    @ApiOperation("delete a device from database")
    @DeleteMapping("deleteDevice")
    public Result deleteDevice(int deviceId) {
        return null;
    }

    @ApiOperation("change the amount of one device")
    @PostMapping("changeDeviceAmount")
    public Result changeDeviceAmount(int deviceId, int newAmount) {
        return null;
    }

    @ApiOperation("merge two device and sum the amounts")
    @PostMapping("mergeDevice")
    public Result mergeDevice(String deviceName, String link, int submitterId, String targetDevice) {
        return null;
    }
}
