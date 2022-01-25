package com.example.LabSystemBackend.controller;

import com.example.LabSystemBackend.common.Response;
import com.example.LabSystemBackend.common.ResponseGenerator;
import com.example.LabSystemBackend.entity.Appointment;
import com.example.LabSystemBackend.entity.TimeSlot;
import com.example.LabSystemBackend.entity.TimeSlotStatus;
import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.service.AppointmentService;
import com.example.LabSystemBackend.service.TimeSlotService;
import com.example.LabSystemBackend.service.UserService;
import com.github.javafaker.App;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private UserService userService;
    @Autowired
    private TimeSlotService timeSlotService;

    @ApiOperation("get list of all appointments of this user")
    @GetMapping("getUserAppointments")
    public Response getUserAppointments(int userId) {

        return ResponseGenerator.genSuccessResult(appointmentService.getUserAppointments(userId));
    }

    @ApiOperation("get list of all appointments of this user, with the given email")
    @PostMapping("getUserAppointmentsWithEmail")
    public Response getUserAppointments(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        User user = userService.getUserByEmail(email);
        List<Appointment> appointments = appointmentService.getUserAppointments(user.getUserId());
        List<TimeSlot> timeSlots = new ArrayList<>();
        for (Appointment appointment : appointments) {
            TimeSlot timeSlot = timeSlotService.getTimeSlotById(appointment.getTimeSlotId());
            timeSlots.add(timeSlot);
        }
        return ResponseGenerator.genSuccessResult(timeSlots);
    }


    @ApiOperation("get list of all appointments")
    @GetMapping("getAllAppointments")
    public Response getAllAppointments() {
        return ResponseGenerator.genSuccessResult(appointmentService.getAllAppointments());
    }

    @ApiOperation("delete one appointment")
    @PostMapping("deleteAppointmentByDate")
    public Response deleteAppointmentById(@RequestBody Map<String, String> body) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(body.get("date"));
        int slot = Integer.parseInt(body.get("slot"));
        TimeSlot timeSlot = timeSlotService.getTimeSlot(date, slot);
        timeSlotService.updateTimeSlotStatus(timeSlot.getTimeSlotId(), TimeSlotStatus.FREE);
        appointmentService.deleteAppointmentByTimeSlotId(timeSlot.getTimeSlotId());
        return ResponseGenerator.genSuccessResult();
    }

    @ApiOperation("User create one new appointment")
    @PostMapping("addAppointment")
    public Response addAppointment(@RequestBody Map<String, String> body) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(body.get("date"));
        int slot = Integer.parseInt(body.get("slot"));
        String email = body.get("email");
        TimeSlot timeSlot = timeSlotService.getTimeSlot(date, slot);
        if (timeSlot.getTimeSlotStatus() != TimeSlotStatus.FREE) {
            return ResponseGenerator.genFailResult("time slot not free");
        }
        User user = userService.getUserByEmail(email);

        if (user == null) {
            return ResponseGenerator.genFailResult("no such user");
        }

        timeSlotService.updateTimeSlotStatus(timeSlot.getTimeSlotId(), TimeSlotStatus.BOOKED);


        appointmentService.addAppointment(user.getUserId(), timeSlot.getTimeSlotId());

        return ResponseGenerator.genSuccessResult();

    }

    @PostMapping("deleteAppointment")
    public Response deleteAppointment(@RequestBody Map<String, String> body) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(body.get("date"));
        int slot = Integer.parseInt(body.get("slot"));

        TimeSlot timeSlot = timeSlotService.getTimeSlot(date, slot);

        timeSlotService.updateTimeSlotStatus(timeSlot.getTimeSlotId(), TimeSlotStatus.FREE);

        appointmentService.deleteAppointmentByTimeSlotId(timeSlot.getTimeSlotId());

        return ResponseGenerator.genSuccessResult();

    }
}
