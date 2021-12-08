package com.example.SpringbootTest.controller;


import com.example.SpringbootTest.common.Response;
import com.example.SpringbootTest.common.ResponseGenerator;
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
    public Response getAllDevicesAndAmount() {
        return ResponseGenerator.genSuccessResult(deviceService.getAllDevicesAndAmount());

    }


    @ApiOperation("add a new device")
    @PostMapping("addDevice")
    public Response addDevice(String deviceName, int Amount, String links) {
        return null;
    }

    @ApiOperation("delete a device from database")
    @DeleteMapping("deleteDevice")
    public Response deleteDevice(int deviceId) {
        return null;
    }

    @ApiOperation("change the amount of one device")
    @PostMapping("changeDeviceAmount")
    public Response changeDeviceAmount(int deviceId, int newAmount) {
        return null;
    }

    @ApiOperation("merge two device and sum the amounts")
    @PostMapping("mergeDevice")
    public Response mergeDevice(String deviceName, String link, int submitterId, String targetDevice) {
        return null;
    }
}
