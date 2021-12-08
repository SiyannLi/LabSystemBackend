package com.example.LabSystemBackend.controller;

import com.example.LabSystemBackend.common.Response;
import com.example.LabSystemBackend.common.ResponseGenerator;
import com.example.LabSystemBackend.service.AppointmentService;
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
    public Response getUserAppointments(String userId){

        return ResponseGenerator.genSuccessResult(appointmentService.getUserAppointments(userId));
    }

    @ApiOperation("get a list of all available Time slots from start date")
    @GetMapping("getAvailableTimeSlots")
    public Response getAvailableTimeFrames(Date startDate){
        return null;
    }

    @ApiOperation("set one available time slot")
    @PostMapping("setAvailableTimeSlots")
    public Response setAvailableTimeFrames(Date availableDate, int TimeFrame, int endRepeatAfter){
        return null;
    }

    @ApiOperation("get list of all appointments")
    @GetMapping("getAllAppointments")
    public Response getAllAppointments(){
        return null;
    }

    @ApiOperation("delete one appointment")
    @PostMapping("deleteAppointment")
    public Response deleteAppointment(int appointmentId){
        return null;
    }

    @ApiOperation("User create one new appointment")
    @PostMapping("addAppointment")
    public Response addAppointment(int userId,Date date, int TimeFrame, String email){
        return null;
    }
}
