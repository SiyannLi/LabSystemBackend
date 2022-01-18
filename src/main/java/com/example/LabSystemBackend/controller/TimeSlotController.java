package com.example.LabSystemBackend.controller;

import com.example.LabSystemBackend.common.Response;
import com.example.LabSystemBackend.common.ResponseGenerator;
import com.example.LabSystemBackend.entity.TimeSlot;
import com.example.LabSystemBackend.service.TimeSlotService;
import com.example.LabSystemBackend.util.DataGenerate;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@CrossOrigin("*")
@RestController
@RequestMapping("/timeslots")
public class TimeSlotController {
    @Autowired
    private TimeSlotService timeSlotService;

    @ApiOperation("get a list of all available Time slots from start date")
    @GetMapping("getAvailableTimeSlots")
    public Response getAvailableTimeFrames(@RequestParam("startDate")
                                              @DateTimeFormat(pattern="yyyy-MM-dd")
                                                       Date startDate){
        return ResponseGenerator.genSuccessResult(timeSlotService.getAvailableTimeSlots(startDate));    }

    @ApiOperation("set one available time slot")
    @PostMapping("setAvailableTimeSlots")
   public Response setAvailableTimeFrames(@DateTimeFormat(pattern="yyyy-MM-dd")Date availableDate, int TimeSlot, int endRepeatAfter){
        return ResponseGenerator.genSuccessResult(timeSlotService.setAvailableTimeSlots(availableDate,TimeSlot,endRepeatAfter));    }


}
