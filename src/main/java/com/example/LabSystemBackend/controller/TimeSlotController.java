package com.example.LabSystemBackend.controller;

import com.example.LabSystemBackend.common.Response;
import com.example.LabSystemBackend.common.ResponseGenerator;
import com.example.LabSystemBackend.service.TimeSlotService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@CrossOrigin("*")
@RestController
@RequestMapping("/timeslots")
public class TimeSlotController {
    @Autowired
    private TimeSlotService timeSlotService;

    @ApiOperation("get a list of all available Time slots from start date")
    @GetMapping("getAvailableTimeSlots")
    public Response getAvailableTimeFrames(Date startDate){
        return ResponseGenerator.genSuccessResult(timeSlotService.getAvailableTimeSlots(startDate));    }

    @ApiOperation("set one available time slot")
    @PostMapping("setAvailableTimeSlots")
    public Response setAvailableTimeFrames(Date availableDate, int TimeSlot, int endRepeatAfter){
        return ResponseGenerator.genSuccessResult(timeSlotService.setAvailableTimeSlots(availableDate,TimeSlot,endRepeatAfter));    }

}
