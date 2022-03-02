package com.example.LabSystemBackend.controller;

import com.example.LabSystemBackend.common.Response;
import com.example.LabSystemBackend.common.ResponseGenerator;
import com.example.LabSystemBackend.entity.Appointment;
import com.example.LabSystemBackend.entity.TimeSlot;
import com.example.LabSystemBackend.entity.TimeSlotStatus;
import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.jwt.JwtUtil;
import com.example.LabSystemBackend.jwt.annotation.AdminLoginToken;
import com.example.LabSystemBackend.jwt.annotation.UserLoginToken;
import com.example.LabSystemBackend.service.AppointmentService;
import com.example.LabSystemBackend.service.NotificationService;
import com.example.LabSystemBackend.service.TimeSlotService;
import com.example.LabSystemBackend.service.UserService;
import com.example.LabSystemBackend.ui.InputMessage;
import com.example.LabSystemBackend.ui.KeyMessage;
import com.example.LabSystemBackend.ui.NotificationTemplate;
import com.example.LabSystemBackend.ui.OutputMessage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @version 1.0
 * @author Siyan Li, Sheyang Li
 *
 * Appointment Controller
 */
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


    @UserLoginToken
    @ApiOperation("get list of all appointments of this user")
    @GetMapping("userGetUserAppointments")
    public Response userGetUserAppointments(@RequestHeader(KeyMessage.TOKEN) String token) {
        String email = JwtUtil.getUserInfo(token, KeyMessage.EMAIL);
        return ResponseGenerator.genSuccessResult(UserController.emailTokens.get(email)
                , getUserAppointments(email));
    }

    @AdminLoginToken
    @ApiOperation("get list of all appointments of this user")
    @GetMapping("adminGetUserAppointments")
    public Response adminGetUserAppointments(@RequestHeader(KeyMessage.TOKEN) String token
            , @ApiParam(name = "userId", value = "userId", required = true)
                                             @RequestBody Map<String, String> body) {
        String opEmail = JwtUtil.getUserInfo(token, KeyMessage.EMAIL);
        String email = body.get(KeyMessage.EMAIL);
        if(userService.emailExists(email)) {
            return ResponseGenerator.genSuccessResult(UserController.emailTokens.get(opEmail)
                    , getUserAppointments(email));
        } else {
            return ResponseGenerator.genFailResult(UserController.emailTokens.get(opEmail)
                    , OutputMessage.USER_NOT_EXISTS);
        }
    }

    private List<TimeSlot> getUserAppointments(String email) {
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
        return timeSlots;
    }

//    @ApiOperation("get list of all appointments of this user, with the given email")
//    @PostMapping("getUserAppointmentsWithEmail")
//    public Response getUserAppointments(@RequestBody Map<String, String> body) {
//        String email = body.get("email");
//        logger.info("email: " + email);
//        User user = userService.getUserByEmail(email);
//        logger.info("user id: " + user.getUserId().toString());
//        List<Appointment> appointments = appointmentService.getUserAppointments(user.getUserId());
//        logger.info("appointments size: " + appointments.size());
//        List<TimeSlot> timeSlots = new ArrayList<>();
//        for (Appointment appointment : appointments) {
//            TimeSlot timeSlot = timeSlotService.getTimeSlotById(appointment.getTimeSlotId());
//            timeSlots.add(timeSlot);
//        }
//        logger.info("timeSlots size:" + timeSlots.size());
//        return ResponseGenerator.genSuccessResult(timeSlots);
//    }


//    @ApiOperation("get list of all appointments")
//    @GetMapping("getAllAppointments")
//    public Response getAllAppointments() {
//        return ResponseGenerator.genSuccessResult(appointmentService.getAllAppointments());
//    }
//
//    @ApiOperation("delete one appointment")
//    @PostMapping("deleteAppointmentByDate")
//    public Response deleteAppointmentById(@RequestBody Map<String, String> body) throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = sdf.parse(body.get("date"));
//        int slot = Integer.parseInt(body.get("slot"));
//        TimeSlot timeSlot = timeSlotService.getTimeSlot(date, slot);
//        timeSlotService.updateTimeSlotStatus(timeSlot.getTimeSlotId(), TimeSlotStatus.FREE);
//        appointmentService.deleteAppointmentByTimeSlotId(timeSlot.getTimeSlotId());
//        return ResponseGenerator.genSuccessResult();
//    }

    @AdminLoginToken
    @ApiOperation("User create one new appointment")
    @PostMapping("adminAddAppointment")
    public Response adminAddAppointment(@RequestHeader(KeyMessage.TOKEN) String token,
                                        @RequestBody Map<String, String> body) throws ParseException, MessagingException {
        String opEmail = JwtUtil.getUserInfo(token, KeyMessage.EMAIL);
        User opUser = userService.getUserByEmail(opEmail);
        String email = body.get(KeyMessage.EMAIL);
        if (!userService.emailExists(email)) {
            return ResponseGenerator.genFailResult(UserController.emailTokens.get(opEmail)
                    , OutputMessage.USER_NOT_EXISTS);
        }
        User user = userService.getUserByEmail(body.get(KeyMessage.EMAIL));
        SimpleDateFormat sdf = new SimpleDateFormat(InputMessage.DATE_FORMAT);
        Date date = sdf.parse(body.get(KeyMessage.DATE));
        int slot = Integer.parseInt(body.get(KeyMessage.SLOT));
        String[] slots = OutputMessage.SLOTS;
        notificationService.sendNotificationAddOrDeleteAppointment(email, NotificationTemplate.APPOINTMENT_BOOKED
                , user.getFullName(),slots[slot], body.get(KeyMessage.DATE),opUser.getFullName());
        Response response = addAppointment(opEmail, email, date, slot);
        return response;

    }

    @UserLoginToken
    @ApiOperation("User create one new appointment")
    @PostMapping("userAddAppointment")
    public Response userAddAppointment(@RequestHeader(KeyMessage.TOKEN) String token,
                                       @RequestBody Map<String, String> body) throws ParseException, MessagingException {
        String email = JwtUtil.getUserInfo(token, KeyMessage.EMAIL);
        User user = userService.getUserByEmail(email);
        SimpleDateFormat sdf = new SimpleDateFormat(InputMessage.DATE_FORMAT);
        Date date = sdf.parse(body.get(KeyMessage.DATE));
        int slot = Integer.parseInt(body.get(KeyMessage.SLOT));
        String[] slots = OutputMessage.SLOTS;
        notificationService.sendNotificationAddOrDeleteAppointment(email, NotificationTemplate.APPOINTMENT_BOOKED
                , user.getFullName(),slots[slot], body.get(KeyMessage.DATE),user.getFullName());
        Response response = addAppointment(email, email, date, slot);
        return response;

    }

    private Response addAppointment(String opEmail, String email, Date date, int slot) {
        TimeSlot timeSlot = timeSlotService.getTimeSlot(date, slot);
        if (timeSlot.getTimeSlotStatus() != TimeSlotStatus.FREE) {
            return ResponseGenerator.genFailResult(UserController.emailTokens.get(opEmail)
                    , OutputMessage.TIME_SLOT_NOT_FREE);
        }
        User user = userService.getUserByEmail(email);
        /*
        user chechen no exists can begin
        if (user == null) {
            return ResponseGenerator.genFailResult(UserController.emailTokens.get(opEmail)
                    , OutputMessage.USER_NOT_EXISTS);
        }*/

        timeSlotService.updateTimeSlotStatus(timeSlot.getTimeSlotId(), TimeSlotStatus.BOOKED);


        appointmentService.addAppointment(user.getUserId(), timeSlot.getTimeSlotId());

        return ResponseGenerator.genSuccessResult(UserController.emailTokens.get(opEmail), OutputMessage.SUCCEED);

    }


    @AdminLoginToken
    @PostMapping("adminDeleteAppointment")
    public Response adminDeleteAppointment(@RequestHeader(KeyMessage.TOKEN) String token,
                                           @RequestBody Map<String, String> body) throws ParseException, MessagingException {
        logger.info("deleteToken: " + token);
        String opEmail = JwtUtil.getUserInfo(token, KeyMessage.EMAIL);
        User opUser = userService.getUserByEmail(opEmail);

        SimpleDateFormat sdf = new SimpleDateFormat(InputMessage.DATE_FORMAT);
        Date date = sdf.parse(body.get(KeyMessage.DATE));
        int slot = Integer.parseInt(body.get(KeyMessage.SLOT));
        String[] slots = OutputMessage.SLOTS;

        TimeSlot timeSlot = timeSlotService.getTimeSlot(date, slot);

        timeSlotService.updateTimeSlotStatus(timeSlot.getTimeSlotId(), TimeSlotStatus.FREE);

        Appointment appointment = appointmentService.getAppointmentByTimeSlotId(timeSlot.getTimeSlotId());
        int userId = appointment.getUserId();
        User user = userService.getUser(userId);
        appointmentService.deleteAppointmentByTimeSlotId(timeSlot.getTimeSlotId());

        notificationService.sendNotificationAddOrDeleteAppointment(user.getEmail(), NotificationTemplate.APPOINTMENT_CANCELLED
                , user.getFullName(),slots[slot], body.get(KeyMessage.DATE), opUser.getFullName());

        return ResponseGenerator.genSuccessResult(UserController.emailTokens.get(opEmail), OutputMessage.SUCCEED);

    }

    @UserLoginToken
    @PostMapping("userDeleteAppointment")
    public Response userDeleteAppointment(@RequestHeader(KeyMessage.TOKEN) String token,
                                          @RequestBody Map<String, String> body) throws ParseException, MessagingException {
        String opEmail = JwtUtil.getUserInfo(token, KeyMessage.EMAIL);
        User opUser = userService.getUserByEmail(opEmail);

        SimpleDateFormat sdf = new SimpleDateFormat(InputMessage.DATE_FORMAT);
        Date date = sdf.parse(body.get(KeyMessage.DATE));
        int slot = Integer.parseInt(body.get(KeyMessage.SLOT));
        String[] slots = OutputMessage.SLOTS;
        TimeSlot timeSlot = timeSlotService.getTimeSlot(date, slot);

        timeSlotService.updateTimeSlotStatus(timeSlot.getTimeSlotId(), TimeSlotStatus.FREE);

        Appointment appointment = appointmentService.getAppointmentByTimeSlotId(timeSlot.getTimeSlotId());
        int userId = appointment.getUserId();
        User user = userService.getUser(userId);
        if (!Objects.equals(opUser.getUserId(), user.getUserId())) {
            return ResponseGenerator.genFailResult(UserController.emailTokens.get(opEmail)
                    , OutputMessage.CANCEL_OTHERS_APPOINTMENT);
        }
        appointmentService.deleteAppointmentByTimeSlotId(timeSlot.getTimeSlotId());

        notificationService.sendNotificationAddOrDeleteAppointment(user.getEmail(), NotificationTemplate.APPOINTMENT_CANCELLED
                , user.getFullName(),slots[slot], body.get(KeyMessage.DATE),opUser.getFullName());

        return ResponseGenerator.genSuccessResult(UserController.emailTokens.get(opEmail), OutputMessage.SUCCEED);

    }

    @UserLoginToken
    @PostMapping("bookedAndFree")
    public Response bookedAndFree(@RequestHeader(KeyMessage.TOKEN) String token,
                                  @RequestBody Map<String, String> body) throws ParseException {
        String email = JwtUtil.getUserInfo(token, KeyMessage.EMAIL);
        SimpleDateFormat sdf = new SimpleDateFormat(InputMessage.DATE_FORMAT);
        Date startDate = sdf.parse(body.get(KeyMessage.START_DATE));
        List<List<TimeSlot>> calender = new ArrayList<List<TimeSlot>>();

        List<TimeSlot> userBooked = timeSlotService.getUserBookedTimeSlot(email);
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
                for (TimeSlot t : oneDay) {
                    if (t.getSlot() == slot && t.getTimeSlotStatus() == TimeSlotStatus.FREE) {
                        toInsert = t;
                    }
                }
                for (TimeSlot t : userBooked) {
                    if (t.getSlot() == slot && Objects.equals(t.getTimeSlotDate(), cl.getTime())) {
                        toInsert = t;
                    }
                }
                if (toInsert.getTimeSlotDate().before(new Date())){
                    toInsert.setTimeSlotStatus(TimeSlotStatus.NA);
                }
                ts.add(toInsert);
            }
            calender.add(ts);
            cl.add(Calendar.DAY_OF_YEAR, 1);
        }
        return ResponseGenerator.genSuccessResult(UserController.emailTokens.get(email), calender);

    }
}
