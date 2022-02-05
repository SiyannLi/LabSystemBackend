package com.example.LabSystemBackend.controller;

import com.example.LabSystemBackend.common.Response;
import com.example.LabSystemBackend.common.ResponseGenerator;
import com.example.LabSystemBackend.entity.TimeSlot;
import com.example.LabSystemBackend.entity.TimeSlotStatus;
import com.example.LabSystemBackend.jwt.JwtUtil;
import com.example.LabSystemBackend.jwt.comment.AdminLoginToken;
import com.example.LabSystemBackend.service.TimeSlotService;
import com.example.LabSystemBackend.ui.KeyMessage;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/timeslots")
public class TimeSlotController {
    private static final Logger logger = LoggerFactory.getLogger(TimeSlotController.class);
    @Autowired
    private TimeSlotService timeSlotService;

    @ApiOperation("get a list of all available Time slots from start date")
    @GetMapping("getAvailableTimeSlots")
    public Response getAvailableTimeFrames(Date startDate) {
        return ResponseGenerator.genSuccessResult(timeSlotService.getAvailableTimeSlots(startDate));
    }

    @AdminLoginToken
    @ApiOperation("setPeriodTimeSlots")
    @PostMapping("setPeriodTimeSlots")
    public Response setPeriodTimeSlots(@RequestHeader(KeyMessage.TOKEN) String token,
                                       @RequestBody Map<String, String> body) throws ParseException {
        String opEmail = JwtUtil.getUserInfo(token, KeyMessage.EMAIL);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(body.get("startDate"));
        TimeSlotStatus status = TimeSlotStatus.valueOf(body.get("status"));
        int slot = Integer.parseInt(body.get("slot"));
        int endRepeatAfter = Integer.parseInt(body.get("endRepeatAfter"));

        return ResponseGenerator.genSuccessResult(UserController.emailTokens.get(opEmail)
                , timeSlotService.setPeriodTimeSlots(startDate, slot, endRepeatAfter, status));
    }

    @AdminLoginToken
    @PostMapping("timeSlotCalender")
    public Response timeSlotCalender(@RequestHeader(KeyMessage.TOKEN) String token,
                                     @RequestBody Map<String, String> body) throws ParseException {
        String opEmail = JwtUtil.getUserInfo(token, KeyMessage.EMAIL);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(body.get("startDate"));
        List<List<TimeSlot>> calender = new ArrayList<List<TimeSlot>>();

        Calendar cl = Calendar.getInstance();
        cl.setTime(startDate);
        for (int day = 0; day < 7; day++) {
            Date date = cl.getTime();
            List<TimeSlot> ts = new ArrayList<>();
            List<TimeSlot> oneDay = timeSlotService.timeSlotOneDay(date);
            for (int slot = 0; slot < 6; slot++) {
                TimeSlot toInsert = new TimeSlot();
                toInsert.setTimeSlotStatus(TimeSlotStatus.NA);
                toInsert.setSlot(slot);
                toInsert.setTimeSlotDate(cl.getTime());
                for (TimeSlot t : oneDay
                ) {
                    if (t.getSlot() == slot) {
                        toInsert = t;
                    }
                }
                ts.add(toInsert);
            }
            calender.add(ts);
            cl.add(Calendar.DAY_OF_YEAR, 1);
        }
        return ResponseGenerator.genSuccessResult(UserController.emailTokens.get(opEmail), calender);
    }

    @AdminLoginToken
    @GetMapping("getBookedTimeSlot")
    public Response getBookedTimeSlot(@RequestHeader(KeyMessage.TOKEN) String token) {
        String opEmail = JwtUtil.getUserInfo(token, KeyMessage.EMAIL);
        List<Map<String, Object>> books = timeSlotService.getBookedTimeSlot();
        Response response = ResponseGenerator.genSuccessResult(UserController.emailTokens.get(opEmail), books);
        return response;
    }
}
