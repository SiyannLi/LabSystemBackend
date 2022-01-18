package com.example.LabSystemBackend.controller;

import com.example.LabSystemBackend.common.Response;
import com.example.LabSystemBackend.common.ResponseGenerator;
import com.example.LabSystemBackend.entity.TimeSlot;
import com.example.LabSystemBackend.service.AppointmentService;
import com.example.LabSystemBackend.service.TimeSlotService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@CrossOrigin("*")
@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService ;
    

    @ApiOperation("get list of all appointments of this user")
    @GetMapping("getUserAppointments")
    public Response getUserAppointments(int userId){

        return ResponseGenerator.genSuccessResult(appointmentService.getUserAppointments(userId));
    }


    @ApiOperation("get list of all appointments")
    @GetMapping("getAllAppointments")
    public Response getAllAppointments(){
        return ResponseGenerator.genSuccessResult(appointmentService.getAllAppointments());
    }

    @ApiOperation("delete one appointment")
    @PostMapping("deleteAppointment")
    public Response deleteAppointment(int appointmentId){
        return ResponseGenerator.genSuccessResult(appointmentService.deleteAppointment(appointmentId));

    }

    @ApiOperation("User create one new appointment")
    @PostMapping("addAppointment")

    public Response addAppointment(int userId,int timeSlotId){
        return ResponseGenerator.genSuccessResult(appointmentService.addAppointment(userId, timeSlotId));

    }
}
