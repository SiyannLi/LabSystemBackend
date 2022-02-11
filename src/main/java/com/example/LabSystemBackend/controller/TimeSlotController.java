package com.example.LabSystemBackend.controller;

import com.example.LabSystemBackend.common.Response;
import com.example.LabSystemBackend.common.ResponseGenerator;
import com.example.LabSystemBackend.entity.TimeSlot;
import com.example.LabSystemBackend.entity.TimeSlotStatus;
import com.example.LabSystemBackend.jwt.JwtUtil;
import com.example.LabSystemBackend.jwt.annotation.AdminLoginToken;
import com.example.LabSystemBackend.service.TimeSlotService;
import com.example.LabSystemBackend.ui.InputMessage;
import com.example.LabSystemBackend.ui.KeyMessage;
import com.example.LabSystemBackend.ui.OutputMessage;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @version 1.0
 * @author Siyan Li
 *
 * Time slot Controller
 */
@CrossOrigin
@RestController
@RequestMapping("/timeslots")
public class TimeSlotController {
    private static final Logger logger = LoggerFactory.getLogger(TimeSlotController.class);
    @Autowired
    private TimeSlotService timeSlotService;

    @AdminLoginToken
    @ApiOperation("setPeriodTimeSlotsNA")
    @PostMapping("setPeriodTimeSlotsNA")
    public Response setPeriodTimeSlotsNA(@RequestHeader(KeyMessage.TOKEN) String token,
                                       @RequestBody Map<String, String> body) throws ParseException {
        String opEmail = JwtUtil.getUserInfo(token, KeyMessage.EMAIL);
        SimpleDateFormat sdf = new SimpleDateFormat(InputMessage.DATE_FORMAT);
        Date startDate = sdf.parse(body.get(KeyMessage.START_DATE));
        int slot = Integer.parseInt(body.get(KeyMessage.SLOT));
        int endRepeatAfter = Integer.parseInt(body.get(KeyMessage.END_REPEAT_AFTER));

        int changedLine = timeSlotService.setPeriodTimeSlotsNA(startDate, slot, endRepeatAfter);
        if (changedLine == endRepeatAfter) {
            return ResponseGenerator.genSuccessResult(UserController.emailTokens.get(opEmail), OutputMessage.SUCCEED);
        }
        else {
            return ResponseGenerator.genFailResult(UserController.emailTokens.get(opEmail)
                    ,String.format(OutputMessage.NOT_ALL_TIME_SET_TO_NA, changedLine));
        }
    }

    @AdminLoginToken
    @ApiOperation("setPeriodTimeSlotsFREE")
    @PostMapping("setPeriodTimeSlotsFREE")
    public Response setPeriodTimeSlotsFREE(@RequestHeader(KeyMessage.TOKEN) String token,
                                         @RequestBody Map<String, String> body) throws ParseException {
        String opEmail = JwtUtil.getUserInfo(token, KeyMessage.EMAIL);
        SimpleDateFormat sdf = new SimpleDateFormat(InputMessage.DATE_FORMAT);
        Date startDate = sdf.parse(body.get(KeyMessage.START_DATE));
        int slot = Integer.parseInt(body.get(KeyMessage.SLOT));
        int endRepeatAfter = Integer.parseInt(body.get(KeyMessage.END_REPEAT_AFTER));

        int changedLine = timeSlotService.setPeriodTimeSlotsFREE(startDate, slot, endRepeatAfter);
        if (changedLine == endRepeatAfter) {
            return ResponseGenerator.genSuccessResult(UserController.emailTokens.get(opEmail),OutputMessage.SUCCEED);
        }
        else {
            return ResponseGenerator.genFailResult(UserController.emailTokens.get(opEmail)
                    , String.format(OutputMessage.NOT_ALL_TIME_SET_TO_FREE, changedLine));
        }

    }

    @AdminLoginToken
    @PostMapping("timeSlotCalender")
    public Response timeSlotCalender(@RequestHeader(KeyMessage.TOKEN) String token,
                                     @RequestBody Map<String, String> body) throws ParseException {
        String opEmail = JwtUtil.getUserInfo(token, KeyMessage.EMAIL);
        SimpleDateFormat sdf = new SimpleDateFormat(InputMessage.DATE_FORMAT);
        Date startDate = sdf.parse(body.get(KeyMessage.START_DATE));
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
        Date today = new Date();
        List<Map<String, Object>> books = timeSlotService.getBookedTimeSlot(today);
        Response response = ResponseGenerator.genSuccessResult(UserController.emailTokens.get(opEmail), books);
        return response;
    }
}
