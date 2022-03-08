package com.example.LabSystemBackend.service.impl;

import com.example.LabSystemBackend.dao.TimeSlotDao;
import com.example.LabSystemBackend.entity.TimeSlot;
import com.example.LabSystemBackend.service.TimeSlotService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

@SpringBootTest
class TimeSlotServiceImplTest {

    @Autowired
    TimeSlotService timeSlotService;
    @Autowired
    TimeSlotDao timeSlotDao;
    @Test
    void getAvailableTimeSlots() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date starDate = sdf.parse("2022-01-01");
        List<TimeSlot> timeSlots = timeSlotService.getAvailableTimeSlots(starDate);
        assertNotNull(timeSlots);
    }

    @Test
    @Transactional
    void setPeriodTimeSlotsFREE() throws InterruptedException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2030-01-01");
        int before = timeSlotService.getAvailableTimeSlots(date).size();
        Random random = new Random();
        int slot = random.nextInt(6);
        int repeat = random.nextInt(10);
        timeSlotService.setPeriodTimeSlotsFREE(date,slot,repeat);
        int after = timeSlotService.getAvailableTimeSlots(date).size();
        assertEquals(repeat,after-before+1);
    }
    @Test
    @Transactional
    void setPeriodTimeSlotsFREEbyNA() throws InterruptedException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2030-01-01");
        int before = timeSlotService.getAvailableTimeSlots(date).size();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int slot = 5;
        int repeat = 1;
        TimeSlot timeSlot1 = new TimeSlot();
        timeSlot1.setTimeSlotStatus(NA);
        timeSlot1.setTimeSlotDate(calendar.getTime());
        timeSlot1.setSlot(5);
        timeSlotDao.addTimeSlot(timeSlot1);
        timeSlotService.setPeriodTimeSlotsFREE(date,slot,repeat);
        int after = timeSlotService.getAvailableTimeSlots(date).size();
        assertEquals(repeat,after-before+1);
    }

    @Test
    @Transactional
    void setPeriodTimeSlotsNA() {
        TimeSlot timeSlot = timeSlotDao.getLastTimeSlot();
        Date date = timeSlot.getTimeSlotDate();
        Random random = new Random();
        int slot = random.nextInt(6);
        int repeat = random.nextInt(10);
        int resultCounter = timeSlotService.setPeriodTimeSlotsNA(date,slot,repeat);
        assertEquals(repeat,resultCounter);
    }

    @Test
    @Transactional
    void setPeriodTimeSlotsNAbyFREE() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2030-01-01");
        int before = timeSlotService.getAvailableTimeSlots(date).size();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int slot = 5;
        int repeat = 1;
        TimeSlot timeSlot1 = new TimeSlot();
        timeSlot1.setTimeSlotStatus(FREE);
        timeSlot1.setTimeSlotDate(calendar.getTime());
        timeSlot1.setSlot(slot);
        timeSlotDao.addTimeSlot(timeSlot1);
        int resultCounter = timeSlotService.setPeriodTimeSlotsNA(date,slot,repeat);
        int after = timeSlotService.getAvailableTimeSlots(date).size();
        assertEquals(repeat,after-before+1);
    }

    @Test
    @Transactional
    void updateTimeSlotStatus() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2030-01-01");

        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setSlot(2);
        timeSlot.setTimeSlotDate(date);
        timeSlot.setTimeSlotStatus(FREE);
        timeSlotDao.addTimeSlot(timeSlot);
        TimeSlot testTimeSlot = timeSlotDao.getTimeSlot(date,2);
        timeSlotService.updateTimeSlotStatus(testTimeSlot.getTimeSlotId(), BOOKED);
        testTimeSlot = timeSlotDao.getTimeSlot(date,2);
        assertEquals(BOOKED,testTimeSlot.getTimeSlotStatus());
    }

    @Test
    @Transactional
    void timeSlotPeriod() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date starDate = sdf.parse("2022-01-01");
        Date endDate = sdf.parse("2022-02-30");
        List<TimeSlot> timeSlots = timeSlotService.timeSlotPeriod(starDate, endDate);
        assertNotNull(timeSlots);
    }

    @Test
    @Transactional
    void timeSlotOneDay() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2030-01-01");
        for (int i=0; i<4; i++) {
            TimeSlot timeSlot = new TimeSlot();
            timeSlot.setSlot(i);
            timeSlot.setTimeSlotDate(date);
            timeSlot.setTimeSlotStatus(FREE);
            timeSlotDao.addTimeSlot(timeSlot);
        }
        List<TimeSlot> timeSlots = timeSlotService.timeSlotOneDay(date);
        assertEquals(4,timeSlots.size());
    }

    @Test
    void getBookedTimeSlot() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String s = sdf.format(date);
        Date today = sdf.parse(s);
        List<Map<String, Object>> timeSlots = timeSlotService.getBookedTimeSlot(today);
        assertNotNull(timeSlots);
    }

    @Test
    @Transactional
    void getTimeSlot() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setSlot(1);
        Date date = sdf.parse("2030-01-01");
        timeSlot.setTimeSlotDate(date);
        timeSlot.setTimeSlotStatus(FREE);
        timeSlotDao.addTimeSlot(timeSlot);

        TimeSlot testTimeSlot = timeSlotService.getTimeSlot(date,1);
        assertAll("timeSlot",
                () -> assertEquals(1, testTimeSlot.getSlot()) ,
                () -> assertEquals(date, testTimeSlot.getTimeSlotDate())
        );
    }

    @Test
    @Transactional
    void  getTimeSlotById() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setSlot(1);
        Date date = sdf.parse("2030-01-01");
        timeSlot.setTimeSlotDate(date);
        timeSlot.setTimeSlotStatus(FREE);
        timeSlotDao.addTimeSlot(timeSlot);
        TimeSlot testTimeSlot = timeSlotService.getTimeSlot(date, 1);
        TimeSlot testTimeSlot1 = timeSlotService.getTimeSlotById(testTimeSlot.getTimeSlotId());

        assertAll("timeSlot",
                () -> assertEquals(1, testTimeSlot1.getSlot()),
                () -> assertEquals(date, testTimeSlot1.getTimeSlotDate())
        );
    }

    @Test
    void getUserBookedTimeSlot() {
        String email = "test@hotmail.com";//set a test user and his BOOKED appointment
        List<TimeSlot> timeSlots = timeSlotService.getUserBookedTimeSlot(email);
        assertNotNull(timeSlots);
    }

    @Test
    @Transactional
    void TimeSlotExists() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setSlot(1);
        Date date = sdf.parse("2030-01-01");
        timeSlot.setTimeSlotDate(date);
        timeSlot.setTimeSlotStatus(FREE);
        timeSlotDao.addTimeSlot(timeSlot);

        assertTrue(timeSlotService.TimeSlotExists(date,1));
    }
}
