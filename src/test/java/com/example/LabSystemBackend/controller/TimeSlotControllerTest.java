package com.example.LabSystemBackend.controller;

import com.example.LabSystemBackend.entity.*;
import com.example.LabSystemBackend.jwt.AuthenticationInterceptor;
import com.example.LabSystemBackend.jwt.JwtUtil;
import com.example.LabSystemBackend.service.TimeSlotService;
import com.example.LabSystemBackend.ui.KeyMessage;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
/**
 * @version 1.0
 * @author  Sheyang Li
 *
 * TimeSlotController Test
 */
@SpringBootTest
@AutoConfigureMockMvc
class TimeSlotControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    AuthenticationInterceptor interceptor;
    @MockBean
    TimeSlotService timeSlotService;

    private String token;
    private String opEmail;
    //test data
    User user = new User(39,"testfFirstName","testLastName","test_user@testpse.com",
            UserRole.ADMIN,"a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3",
            UserAccountStatus.ACTIVE,true);

    List<TimeSlot> timeSlots = new ArrayList<>(6);
    List<TimeSlot> timeSlotsNull = new ArrayList<>(6);
    Map<String ,Object> testMap = new HashMap<String,Object>();
    List<Map<String, Object>> bookedTimeSlots = new ArrayList<>();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String testDate = "2030-01-01";
    Date date = sdf.parse("2030-01-01");
    TimeSlot testTimeSlot1 = new TimeSlot(1,date,1, TimeSlotStatus.FREE);
    TimeSlot testTimeSlot2 = new TimeSlot(2,date,2, TimeSlotStatus.BOOKED);
    TimeSlot testTimeSlot3 = new TimeSlot(3,date,3, TimeSlotStatus.NA);

    TimeSlotControllerTest() throws ParseException {
    }

    @BeforeEach
    private void setup() {
        token = JwtUtil.createToken(user);
        opEmail = user.getEmail();

        timeSlots.add(testTimeSlot1);
        timeSlots.add(testTimeSlot2);
        timeSlots.add(testTimeSlot3);
        testMap.put("timeSlotId","2");
        testMap.put("slot","2");
        testMap.put("timeSlotStatus","BOOKED");
        bookedTimeSlots.add(testMap);

    }

    private void setPeriodTimeSlotsNA(Boolean isOk, String startDate, int slot, int endRepeatAfter,
                                      int resultCode, String message) {
        String url = "/timeslots/setPeriodTimeSlotsNA";
        String body = "{\"" + KeyMessage.START_DATE + "\":\"" + startDate + "\",\"" + KeyMessage.SLOT + "\":\""
                + slot + "\",\"" + KeyMessage.END_REPEAT_AFTER + "\":\"" + endRepeatAfter + "\" }";
        if (isOk) {
            when(timeSlotService.setPeriodTimeSlotsNA(Mockito.any(), Mockito.anyInt(),
                    Mockito.anyInt())).thenReturn(endRepeatAfter);
        } else {
            when(timeSlotService.setPeriodTimeSlotsNA(Mockito.any(), Mockito.anyInt(),
                    Mockito.anyInt())).thenReturn(endRepeatAfter - 1);
        }

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
                    .andExpect(jsonPath("$.resultCode").value(resultCode))
                    .andExpect(jsonPath("$..message").value(message))
                    .andReturn();
        } catch (Exception e) {
            Assert.fail("fail to request");
        }
    }

    @Test
    void setPeriodTimeSlotsNASucceed() {
        setPeriodTimeSlotsNA(true, "2030-01-01", 1,3,200, OutputMessage.SUCCEED);
    }

    @Test
    void setPeriodTimeSlotsNAFail() {
        setPeriodTimeSlotsNA(false, "2030-01-01", 1,3,500,
                "only 2 time slot set to not available.");
    }

    @Test
    void setPeriodTimeSlotsFREE() {
    }
    private void setPeriodTimeSlotsFREE(Boolean isOk, String startDate, int slot, int endRepeatAfter, int resultCode, String message) {
        String url = "/timeslots/setPeriodTimeSlotsFREE";
        String body = "{\"" + KeyMessage.START_DATE + "\":\"" + startDate + "\",\"" + KeyMessage.SLOT + "\":\"" + slot + "\"," +
                "\"" + KeyMessage.END_REPEAT_AFTER + "\":\"" + endRepeatAfter + "\" }";
        if (isOk) {
            when(timeSlotService.setPeriodTimeSlotsFREE(Mockito.any(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(endRepeatAfter);
        } else {
            when(timeSlotService.setPeriodTimeSlotsFREE(Mockito.any(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(endRepeatAfter - 1);
        }

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
                    .andExpect(jsonPath("$.resultCode").value(resultCode))
                    .andExpect(jsonPath("$..message").value(message))
                    .andReturn();
        } catch (Exception e) {
            Assert.fail("fail to request");
        }
    }

    @Test
    void setPeriodTimeSlotsFREESucceed() {
        setPeriodTimeSlotsFREE(true, "2030-01-01", 1,
                3,200, OutputMessage.SUCCEED);
    }

    @Test
    void setPeriodTimeSlotsFREEFail() {
        setPeriodTimeSlotsFREE(false, "2030-01-01", 1,3,500, "only 2 time slot set to free.");
    }

    @Test
    void timeSlotCalender() {
        List<TimeSlot> timeSlots1 = new ArrayList<>(6);
        String url = "/timeslots/timeSlotCalender";
        String body = "{\"" + KeyMessage.START_DATE + "\":\"" + testDate + "\"}";
        when(timeSlotService.timeSlotOneDay(Mockito.any())).thenReturn(timeSlots,timeSlotsNull,timeSlotsNull,timeSlotsNull,timeSlotsNull,timeSlotsNull,timeSlotsNull);

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

    @Test
    void getBookedTimeSlot() {
        List<TimeSlot> timeSlots1 = new ArrayList<>();
        String url = "/timeslots/getBookedTimeSlot";
        String body = "{\"" + KeyMessage.START_DATE + "\":\"" + testDate + "\"}";
        when(timeSlotService.getBookedTimeSlot(Mockito.any())).thenReturn(bookedTimeSlots);

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
}