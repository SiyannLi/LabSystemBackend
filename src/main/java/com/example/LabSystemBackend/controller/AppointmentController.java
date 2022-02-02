package com.example.LabSystemBackend.controller;

import com.example.LabSystemBackend.common.Response;
import com.example.LabSystemBackend.common.ResponseGenerator;
import com.example.LabSystemBackend.entity.*;
import com.example.LabSystemBackend.service.AppointmentService;
import com.example.LabSystemBackend.service.NotificationService;
import com.example.LabSystemBackend.service.TimeSlotService;
import com.example.LabSystemBackend.service.UserService;
import com.example.LabSystemBackend.ui.NotificationTemplate;
import com.github.javafaker.App;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.mail.MessagingException;
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
    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private UserService userService;
    @Autowired
    private TimeSlotService timeSlotService;
    @Autowired
    private NotificationService notificationService;

    @ApiOperation("get list of all appointments of this user")
    @GetMapping("getUserAppointments")
    public Response getUserAppointments(int userId) {

        return ResponseGenerator.genSuccessResult(appointmentService.getUserAppointments(userId));
    }

    @ApiOperation("get list of all appointments of this user, with the given email")
    @PostMapping("getUserAppointmentsWithEmail")
    public Response getUserAppointments(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        logger.info("email: " + email);
        User user = userService.getUserByEmail(email);
        logger.info("user id: " + user.getUserId().toString());
        List<Appointment> appointments = appointmentService.getUserAppointments(user.getUserId());
        logger.info("appointments size: " + appointments.size());
        List<TimeSlot> timeSlots = new ArrayList<>();
        for (Appointment appointment : appointments) {
            TimeSlot timeSlot = timeSlotService.getTimeSlotById(appointment.getTimeSlotId());
            timeSlots.add(timeSlot);
        }
        logger.info("timeSlots size:" + timeSlots.size());
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
    public Response addAppointment(@RequestBody Map<String, String> body) throws ParseException, MessagingException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(body.get("date"));
        int slot = Integer.parseInt(body.get("slot"));
        String[] time = {"8:00-9:45","10:00-11:45","12:00-13:45","14:00-15:45","16:00-17:45","18:00-17:45"};
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

        Notification notification = new Notification();
        notification.setSenderId(User.ID_OF_SYSTEM);
        notification.setContent(String.format(NotificationTemplate.APPOINTMENT_SUCCESS.getContent(), user.getFullName(), date, time[slot]));
        notification.setSubject(NotificationTemplate.APPOINTMENT_SUCCESS.getSubject());
        notification.setRecipientId(user.getUserId());
        notificationService.sendNotification(email, notification);

        return ResponseGenerator.genSuccessResult();

    }

    @PostMapping("deleteAppointment")
    public Response deleteAppointment(@RequestBody Map<String, String> body) throws ParseException, MessagingException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(body.get("date"));
        int slot = Integer.parseInt(body.get("slot"));
        String[] time ={"8:00-9:45","10:00-11:45","12:00-13:45","14:00-15:45","16:00-17:45","18:00-17:45"};
        TimeSlot timeSlot = timeSlotService.getTimeSlot(date, slot);
        timeSlotService.updateTimeSlotStatus(timeSlot.getTimeSlotId(), TimeSlotStatus.FREE);
        int timeSlotId = timeSlot.getTimeSlotId();
        Appointment appointment= appointmentService.getAppointmentByTimeSlotId(timeSlotId);
        int userId = appointment.getUserId();
        User user = userService.getUser(userId);
        String email = user.getEmail();
        String name = user.getFullName();
        appointmentService.deleteAppointmentByTimeSlotId(timeSlot.getTimeSlotId());
        Notification notification = new Notification();
        notification.setSenderId(User.ID_OF_SYSTEM);
        notification.setContent(String.format(NotificationTemplate.APPOINTMENT_CANCEL.getContent(), name,date,time[slot]));
        notification.setSubject(NotificationTemplate.APPOINTMENT_CANCEL.getSubject());
        notification.setRecipientId(userId);
        notificationService.sendNotification(email, notification);
        return ResponseGenerator.genSuccessResult();

    }
}
