package com.example.LabSystemBackend.service.impl;

import com.example.LabSystemBackend.dao.TimeSlotDao;
import com.example.LabSystemBackend.entity.TimeSlot;
import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.entity.UserAccountStatus;
import com.example.LabSystemBackend.entity.UserRole;
import com.example.LabSystemBackend.service.TimeSlotService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.LabSystemBackend.entity.TimeSlotStatus.*;
import static org.junit.jupiter.api.Assertions.*;
/**
 * * @Version:1.0
 * @Author: Sheyang Li
 * @Date:2022/02/19
 * TimeSlotServiceImpl Test
 */
@ActiveProfiles("unittest")
@SpringBootTest
class TimeSlotServiceImplTest {

    @Autowired
    TimeSlotService timeSlotService;
    @MockBean
    TimeSlotDao timeSlotDao;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date starDate = sdf.parse("2030-01-01");
    TimeSlot timeSlot = new TimeSlot(1,starDate,2,FREE);
    TimeSlot timeSlot1 = new TimeSlot(2,starDate,2,NA);
    TimeSlot timeSlotNull =null;// new TimeSlot();
    List<TimeSlot> timeSlots = new ArrayList<>();

    TimeSlotServiceImplTest() throws ParseException {
    }

    @Test
    void getAvailableTimeSlots() throws ParseException {
        timeSlots.add(timeSlot);
        Mockito.when(timeSlotDao.getAvailableTimeSlots(Mockito.any())).thenReturn(timeSlots);
        List<TimeSlot> testTimeSlots = timeSlotService.getAvailableTimeSlots(starDate);
        Assert.assertEquals(timeSlots, testTimeSlots);
    }

    private void setPeriodTimeSlotsFREE(Boolean oldTimeSlot) throws InterruptedException, ParseException {
        if (oldTimeSlot) {
            Mockito.when(timeSlotDao.getTimeSlot(Mockito.any(), Mockito.anyInt())).thenReturn(timeSlot1);
        }else {Mockito.when(timeSlotDao.getTimeSlot(Mockito.any(), Mockito.anyInt())).thenReturn(timeSlotNull);
        }
        Mockito.when(timeSlotDao.updateTimeSlotStatus(Mockito.anyInt(), Mockito.any())).thenReturn(1);
        Mockito.when(timeSlotDao.addTimeSlot(Mockito.any())).thenReturn(1);
        int after = timeSlotService.setPeriodTimeSlotsFREE(starDate,2,1);
        assertEquals(1,after);
    }
    @Test
    void  setPeriodTimeSlotsFREE() throws ParseException, InterruptedException {
        setPeriodTimeSlotsFREE(true);
    }
    @Test
    void  setPeriodTimeSlotsFREEByNA() throws ParseException, InterruptedException {
        setPeriodTimeSlotsFREE(false);
    }

    private void setPeriodTimeSlotsNA(Boolean oldTimeSlot, Boolean isStatusFree) throws InterruptedException, ParseException {
        if (oldTimeSlot) {
            if (isStatusFree){
            Mockito.when(timeSlotDao.getTimeSlot(Mockito.any(), Mockito.anyInt())).thenReturn(timeSlot);
            }else {Mockito.when(timeSlotDao.getTimeSlot(Mockito.any(), Mockito.anyInt())).thenReturn(timeSlot1);
            }
        }else {Mockito.when(timeSlotDao.getTimeSlot(Mockito.any(), Mockito.anyInt())).thenReturn(timeSlotNull);
        }
        Mockito.when(timeSlotDao.updateTimeSlotStatus(Mockito.anyInt(), Mockito.any())).thenReturn(1);
        Mockito.when(timeSlotDao.addTimeSlot(Mockito.any())).thenReturn(1);
        int after = timeSlotService.setPeriodTimeSlotsNA(starDate,2,2);
        if (isStatusFree){
        assertEquals(2,after);
        }else{assertEquals(0,after);}
    }
    @Test
    void  setPeriodTimeSlotsNA() throws ParseException, InterruptedException {
        setPeriodTimeSlotsNA(true, true);
    }
    @Test
    void  setPeriodTimeSlotsNAByFree() throws ParseException, InterruptedException {
        setPeriodTimeSlotsNA(false, true);
    }
    @Test
    void  setPeriodTimeSlotsNAByNoFree() throws ParseException, InterruptedException {
        setPeriodTimeSlotsNA(true, false);
    }


    @Test
    void updateTimeSlotStatus() throws ParseException {
        Mockito.when(timeSlotDao.updateTimeSlotStatus(Mockito.anyInt(), Mockito.any())).thenReturn(2);
        assertEquals(2, timeSlotService.updateTimeSlotStatus(1, BOOKED));
    }

    @Test
    void timeSlotPeriod() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date starDate = sdf.parse("2022-01-01");
        Date endDate = sdf.parse("2022-02-30");
        timeSlots.add(timeSlot);
        Mockito.when(timeSlotDao.timeSlotPeriod(Mockito.any(), Mockito.any())).thenReturn(timeSlots);
        List<TimeSlot> testTimeSlots = timeSlotService.timeSlotPeriod(starDate, endDate);
        Assert.assertEquals(timeSlot, testTimeSlots.get(0));
    }

    @Test
    void timeSlotOneDay() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2030-01-01");
        timeSlots.add(timeSlot);
        Mockito.when(timeSlotDao.timeSlotOneDay(Mockito.any())).thenReturn(timeSlots);
        List<TimeSlot> testTimeSlots = timeSlotService.timeSlotOneDay(date);
        assertEquals(timeSlots,testTimeSlots);
    }

    @Test
    void getBookedTimeSlot() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String s = sdf.format(date);
        Date today = sdf.parse(s);
        User user = new User(1,"pse","pse","pse", UserRole.USER,
                "123456", UserAccountStatus.ACTIVE,true);
        Map<String, Object> testMap  = new HashMap<String ,Object>();
        testMap.put("1", user);
        List<Map<String, Object>> testList = new ArrayList<Map<String, Object>>();
        testList.add(testMap);
        Mockito.when(timeSlotDao.getBookedTimeSlot(today)).thenReturn(testList);

        List<Map<String, Object>> timeSlots = timeSlotService.getBookedTimeSlot(today);
        assertNotNull(timeSlots);
    }

    @Test
    @Transactional
    void getTimeSlot() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2030-01-01");
        Mockito.when(timeSlotDao.getTimeSlot(Mockito.any(), Mockito.anyInt())).thenReturn(timeSlot);
        TimeSlot testTimeSlot = timeSlotService.getTimeSlot(date,1);
        Assert.assertEquals(timeSlot, testTimeSlot);
    }

    @Test
    void  getTimeSlotById() throws ParseException {
        Mockito.when(timeSlotDao.getTimeSlotById(Mockito.anyInt())).thenReturn(timeSlot);
        TimeSlot testTimeSlot = timeSlotService.getTimeSlotById(1);
        Assert.assertEquals(timeSlot, testTimeSlot);
    }

    @Test
    void getUserBookedTimeSlot() {
        String email = "test@hotmail.com";//set a test user and his BOOKED appointment
        timeSlots.add(timeSlot);
        Mockito.when(timeSlotDao.getUserBookedTimeSlot(Mockito.anyString())).thenReturn(timeSlots);
        List<TimeSlot> testTimeSlots = timeSlotService.getUserBookedTimeSlot(email);
        Assert.assertEquals(timeSlots, testTimeSlots);
    }


    private void TimeSlotExists(Boolean isTure) throws ParseException {
        if(isTure){
        Mockito.when(timeSlotDao.getTimeSlot(Mockito.any(), Mockito.anyInt())).thenReturn(timeSlot);
        }else {
            Mockito.when(timeSlotDao.getTimeSlot(Mockito.any(), Mockito.anyInt())).thenReturn(timeSlotNull);
            }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2030-01-01");
        int slot = 1;
        timeSlotService.TimeSlotExists(date,slot);
        if (isTure){
        assertTrue(timeSlotService.TimeSlotExists(date,1));
        }else {
            assertFalse(timeSlotService.TimeSlotExists(date,1));}
    }
    @Test
    void TimeSlotExists() throws ParseException {
        TimeSlotExists(true);
    }
    @Test
    void TimeSlotExistsNo() throws ParseException {
        TimeSlotExists(false);
    }
}
