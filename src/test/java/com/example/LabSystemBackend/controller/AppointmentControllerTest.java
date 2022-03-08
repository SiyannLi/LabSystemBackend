package com.example.LabSystemBackend.controller;

import com.example.LabSystemBackend.entity.*;
import com.example.LabSystemBackend.jwt.AuthenticationInterceptor;
import com.example.LabSystemBackend.jwt.JwtUtil;
import com.example.LabSystemBackend.service.AppointmentService;
import com.example.LabSystemBackend.service.NotificationService;
import com.example.LabSystemBackend.service.TimeSlotService;
import com.example.LabSystemBackend.service.UserService;
import com.example.LabSystemBackend.ui.KeyMessage;
import com.example.LabSystemBackend.ui.NotificationTemplate;
import com.example.LabSystemBackend.ui.OutputMessage;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
/**
 * @version 1.0
 * @author Sheyang Li
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
class AppointmentControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    AuthenticationInterceptor interceptor;
    @MockBean
    UserService userService;
    @MockBean
    AppointmentService appointmentService;
    @MockBean
    TimeSlotService timeSlotService;
    @MockBean
    NotificationService notificationService;

    private String token;
    private String testToken;

    private String email;
    private String testEmail;
    List<Appointment> appointments = new ArrayList<>();
    List<Appointment> userAppointments = new ArrayList<>();
    List<TimeSlot> userTimeSlots = new ArrayList<>();
    List<TimeSlot> timeSlots = new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date date = sdf.parse("2030-01-01");
    String testDate = "2030-01-01";

    //test data
    User user = new User(39,"admin","admin","admin@testpse.com",
            UserRole.ADMIN,"a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3",
            UserAccountStatus.ACTIVE,true);
    User testUser = new User(1,"testFirstName","testLastName","testUser@testpse.com",
            UserRole.USER,"a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3",
            UserAccountStatus.ACTIVE,false);

    Appointment appointment1 = new Appointment(1,39,1);
    Appointment appointment2 = new Appointment(2,39,2);
    Appointment appointment3 = new Appointment(3,1,3);
    TimeSlot timeSlot1 = new TimeSlot(1,date,2,TimeSlotStatus.BOOKED);
    TimeSlot timeSlot2 = new TimeSlot(2,date,4,TimeSlotStatus.BOOKED);
    TimeSlot timeSlot3 = new TimeSlot(3,date,5,TimeSlotStatus.BOOKED);
    TimeSlot timeSlot4 = new TimeSlot(4,date,1,TimeSlotStatus.FREE);

    AppointmentControllerTest() throws ParseException {
    }

    @BeforeEach
    private void setup() {
        token = JwtUtil.createToken(user);
        testToken = JwtUtil.createToken(testUser);
        email = user.getEmail();
        testEmail = testUser.getEmail();
        appointments.add(appointment1);
        appointments.add(appointment2);
        userAppointments.add(appointment3);
        userTimeSlots.add(timeSlot1);
        userTimeSlots.add(timeSlot2);
        timeSlots.add(timeSlot1);
        timeSlots.add(timeSlot2);
        timeSlots.add(timeSlot4);
    }

    @Test
    void userGetUserAppointments() {
        String body = "{}";
        String url = "/appointments/userGetUserAppointments";
        when(userService.getUserByEmail(email)).thenReturn(user);
        when(appointmentService.getUserAppointments(user.getUserId())).thenReturn(appointments);
        when(timeSlotService.getTimeSlotById(Mockito.anyInt())).thenReturn(timeSlot1,timeSlot2);

        try {
            when(interceptor.preHandle(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class),
                    Mockito.any(Object.class))).thenReturn(true);
            mockMvc.perform(MockMvcRequestBuilders
                            .get(url)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(body)
                            .header("Authorization", token))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(jsonPath("$.resultCode").value(200))
                    .andExpect(jsonPath("$..message").value("SUCCESS"))
                    .andReturn();
        } catch (Exception e) {
            Assert.fail("fail to request");
        }
    }

    private void adminGetUserAppointments(int userId, Boolean emailExists, String userEmail, int resultCode,
                                          String message) {
        String url = "/appointments/adminGetUserAppointments";
        String body = "{\""+ KeyMessage.EMAIL+"\":\""+userEmail+"\"}";
        when(userService.getUserByEmail(Mockito.anyString())).thenReturn(user);
        when(appointmentService.getUserAppointments(user.getUserId())).thenReturn(appointments);
        when(timeSlotService.getTimeSlotById(Mockito.anyInt())).thenReturn(timeSlot1,timeSlot2);
        when(userService.emailExists(Mockito.anyString())).thenReturn(emailExists);

        try {
            when(interceptor.preHandle(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class),
                    Mockito.any(Object.class))).thenReturn(true);
            mockMvc.perform(MockMvcRequestBuilders
                            .get(url)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(body)
                            .header("Authorization", token))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(jsonPath("$.resultCode").value(resultCode))
                    .andExpect(jsonPath("$..message").value(message))
                    .andReturn();
        } catch (Exception e) {
            Assert.fail("fail to request");
        }
    }

    @Test
    void adminGetUserAppointmentsSuccess(){
        adminGetUserAppointments(1,true, testUser.getEmail(),200,"SUCCESS");
    }

    @Test
    void adminGetUserAppointmentsFail(){
        adminGetUserAppointments(1,false, testUser.getEmail(),500, OutputMessage.USER_NOT_EXISTS);
    }

    private void addAppointment(Boolean isAdmin, Boolean timeSlotFree, Boolean userExists,String url, String userEmail,
                                String testDate, int slot, int resultCode, String message) throws MessagingException {
        String[] slots = OutputMessage.SLOTS;
        String headerToken;
        String opUserName;
        User Null = new User();

        String body = "{\""+ KeyMessage.EMAIL+"\":\""+userEmail+"\",\"" + KeyMessage.DATE + "\":\"" + testDate +
                "\",\"" + KeyMessage.SLOT + "\":\"" + slot + "\"}";
        if(isAdmin) {
            headerToken= token;
            opUserName = user.getFullName();
            when(userService.getUserByEmail(Mockito.anyString())).thenReturn(user, testUser,testUser);
            when(notificationService.sendNotificationAddOrDeleteAppointment(email, NotificationTemplate.APPOINTMENT_BOOKED
                    , user.getFullName(),slots[slot], testDate,user.getFullName())).thenReturn(1);
        }else {
            headerToken= testToken;
            opUserName = testUser.getFullName();
            when(userService.getUserByEmail(Mockito.anyString())).thenReturn(testUser,testUser);
            when(notificationService.sendNotificationAddOrDeleteAppointment(testEmail, NotificationTemplate.APPOINTMENT_BOOKED
                    , testUser.getFullName(), slots[slot], testDate, testUser.getFullName())).thenReturn(1);
        }
        if (!timeSlotFree){
            when(timeSlotService.getTimeSlot(date, slot)).thenReturn(timeSlot3);
        }
        else {when(timeSlotService.getTimeSlot(date, slot)).thenReturn(timeSlot4);}
        when(userService.emailExists(Mockito.anyString())).thenReturn(userExists);
        when(timeSlotService.updateTimeSlotStatus(timeSlot4.getTimeSlotId(), TimeSlotStatus.BOOKED)).thenReturn(1);
        when(appointmentService.addAppointment(user.getUserId(), timeSlot4.getTimeSlotId())).thenReturn(1);
        try {
            when(interceptor.preHandle(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class),
                    Mockito.any(Object.class))).thenReturn(true);
            mockMvc.perform(MockMvcRequestBuilders
                            .post(url)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(body)
                            .header("Authorization", headerToken))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(jsonPath("$.resultCode").value(resultCode))
                    .andExpect(jsonPath("$..message").value(message))
                    .andReturn();
            // Mockito.verify(notificationService).sendNotificationAddOrDeleteAppointment(userEmail,
            // NotificationTemplate.APPOINTMENT_BOOKED
            //        , testUser.getFullName(),slots[slot], testDate, opUserName);
        } catch (Exception e) {
            Assert.fail("fail to request");
        }
    }


    @Test
    void adminAddAppointmentSuccess() throws MessagingException {
        String url = "/appointments/adminAddAppointment";
        addAppointment(true,true,true,url,testUser.getEmail(), testDate,
                timeSlot4.getSlot(),200,OutputMessage.SUCCEED);
    }
    @Test
    void adminAddAppointmentTimslotnoFreeFail() throws MessagingException {
        String url = "/appointments/adminAddAppointment";
        addAppointment(true,false,true,url,testUser.getEmail(), testDate,
                timeSlot4.getSlot(),500,OutputMessage.TIME_SLOT_NOT_FREE);
    }

    @Test
    void adminAddAppointmentuserNoExistsFail() throws MessagingException {
        String url = "/appointments/adminAddAppointment";
        addAppointment(true,true,false,url,testUser.getEmail(), testDate,
                timeSlot4.getSlot(),500,OutputMessage.USER_NOT_EXISTS);
    }

    @Test
    void userAddAppointmentSuccess() throws MessagingException {
        String url = "/appointments/userAddAppointment";
        addAppointment(false,true,true,url,testUser.getEmail(), testDate,
                timeSlot4.getSlot(),200,OutputMessage.SUCCEED);
    }
    @Test
    void userAddAppointmentTimslotnoFreeFail() throws MessagingException {
        String url = "/appointments/userAddAppointment";
        addAppointment(false,false,true,url,testUser.getEmail(), testDate,
                timeSlot4.getSlot(),500,OutputMessage.TIME_SLOT_NOT_FREE);
    }

    @Test
    void adminDeleteAppointment() throws MessagingException {
        String url = "/appointments/adminDeleteAppointment";
        int slot = 5;
        String[] slots = OutputMessage.SLOTS;
        String body = "{\"" + KeyMessage.DATE + "\":\"" + testDate + "\",\"" + KeyMessage.SLOT + "\":\"" + slot + "\"}";
        when(userService.getUserByEmail(Mockito.anyString())).thenReturn(user);
        when(timeSlotService.getTimeSlot(date, slot)).thenReturn(timeSlot3);
        when(timeSlotService.updateTimeSlotStatus(timeSlot3.getTimeSlotId(), TimeSlotStatus.FREE)).thenReturn(1);
        when(appointmentService.getAppointmentByTimeSlotId(timeSlot3.getTimeSlotId())).thenReturn(appointment3);
        when(userService.getUser(Mockito.anyInt())).thenReturn(testUser);
        when(appointmentService.deleteAppointmentByTimeSlotId(Mockito.anyInt())).thenReturn(1);
        when(notificationService.sendNotificationAddOrDeleteAppointment((testUser.getEmail()),
                NotificationTemplate.APPOINTMENT_CANCELLED
                , testUser.getFullName(), slots[5], testDate, user.getFullName())).thenReturn(1);

        try {
            when(interceptor.preHandle(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class),
                    Mockito.any(Object.class))).thenReturn(true);
            mockMvc.perform(MockMvcRequestBuilders
                            .post(url)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(body)
                            .header("Authorization", token))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(jsonPath("$.resultCode").value(200))
                    .andExpect(jsonPath("$..message").value(OutputMessage.SUCCEED))
                    .andReturn();
            Mockito.verify(notificationService).sendNotificationAddOrDeleteAppointment(testUser.getEmail(),
                    NotificationTemplate.APPOINTMENT_CANCELLED
                    , testUser.getFullName(), slots[5], testDate, user.getFullName());
        } catch (Exception e) {
            Assert.fail("fail to request");
        }
    }

    private void userDeleteAppointment(Boolean sameUser, int resultCode, String message) throws MessagingException {
        String url = "/appointments/userDeleteAppointment";
        int slot = 5;
        String headerToken= token;
        if(sameUser){ headerToken = testToken;
            when(userService.getUserByEmail(Mockito.anyString())).thenReturn(testUser);
        }
        else { headerToken = token;
            when(userService.getUserByEmail(Mockito.anyString())).thenReturn(user);
        }
        String[] slots = OutputMessage.SLOTS;
        String body = "{\"" + KeyMessage.DATE + "\":\"" + testDate + "\",\"" + KeyMessage.SLOT + "\":\"" + slot + "\"}";
        when(timeSlotService.getTimeSlot(date, slot)).thenReturn(timeSlot3);
        when(timeSlotService.updateTimeSlotStatus(timeSlot3.getTimeSlotId(), TimeSlotStatus.FREE)).thenReturn(1);
        when(appointmentService.getAppointmentByTimeSlotId(timeSlot3.getTimeSlotId())).thenReturn(appointment3);
        when(userService.getUser(Mockito.anyInt())).thenReturn(testUser);
        when(appointmentService.deleteAppointmentByTimeSlotId(Mockito.anyInt())).thenReturn(1);
        when(notificationService.sendNotificationAddOrDeleteAppointment((testUser.getEmail()),
                NotificationTemplate.APPOINTMENT_CANCELLED, testUser.getFullName(), slots[5], testDate,
                user.getFullName())).thenReturn(1);

        try {
            when(interceptor.preHandle(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class),
                    Mockito.any(Object.class))).thenReturn(true);
            mockMvc.perform(MockMvcRequestBuilders
                            .post(url)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(body)
                            .header("Authorization", headerToken))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(jsonPath("$.resultCode").value(resultCode))
                    .andExpect(jsonPath("$..message").value(message))
                    .andReturn();
        } catch (Exception e) {
            Assert.fail("fail to request");
        }
    }

    @Test
    void userDeleteAppointmentSuccess() throws MessagingException {
        userDeleteAppointment(true,200, OutputMessage.SUCCEED);
    }

    @Test
    void userDeleteAppointmentFail() throws MessagingException {
        userDeleteAppointment(false,500 ,OutputMessage.CANCEL_OTHERS_APPOINTMENT);
    }

    @Test
    void bookedAndFree() {
        List<TimeSlot> timeSlotsNull = new ArrayList<>();
        String url = "/appointments/bookedAndFree";
        String body = "{\"" + KeyMessage.START_DATE + "\":\"" + testDate + "\"}";

        List<TimeSlot> timeSlots1 = new ArrayList<>(6);

        when(timeSlotService.timeSlotOneDay(Mockito.any())).thenReturn(timeSlots,timeSlotsNull,
                timeSlotsNull,timeSlotsNull,timeSlotsNull,timeSlotsNull,timeSlotsNull);
        when(timeSlotService.getUserBookedTimeSlot(email)).thenReturn(userTimeSlots);
        try {
            when(interceptor.preHandle(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class),
                    Mockito.any(Object.class))).thenReturn(true);
            mockMvc.perform(MockMvcRequestBuilders
                            .post(url)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(body)
                            .header("Authorization", token))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(jsonPath("$.resultCode").value(200))
                    .andExpect(jsonPath("$..message").value("SUCCESS"))
                    .andReturn();
        } catch (Exception e) {
            Assert.fail("fail to request");
        }
    }
}