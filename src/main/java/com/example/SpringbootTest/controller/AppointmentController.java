package com.example.SpringbootTest.controller;

import com.example.SpringbootTest.common.Result;
import com.example.SpringbootTest.common.ResultGenerator;
import com.example.SpringbootTest.service.AppointmentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@CrossOrigin("*")
@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @ApiOperation("get list of all appointments of this user")
    @GetMapping("getUserAppointments")
    public Result getUserAppointments(String userId){

        return ResultGenerator.genSuccessResult(appointmentService.getUserAppointments(userId));
    }

    @ApiOperation("get a list of all available Time slots from start date")
    @GetMapping("getAvailableTimeSlots")
    public Result getAvailableTimeSlots(Date startDate){
        return null;
    }

    @ApiOperation("set one available time slot")
    @PostMapping("setAvailableTimeSlots")
    public Result setAvailableTimeSlots(Date availableDate, int TimeSlot, int endRepeatAfter){
        return null;
    }

    @ApiOperation("get list of all appointments")
    @GetMapping("getAllAppointments")
    public Result getAllAppointments(){
        return null;
    }

    @ApiOperation("delete one appointment")
    @PostMapping("deleteAppointment")
    public Result deleteAppointment(int appointmentId){
        return null;
    }

    @ApiOperation("User create one new appointment")
    @PostMapping("addAppointment")
    public Result addAppointment(int userId, int TimeSlot, String email){
        return null;
    }
}
