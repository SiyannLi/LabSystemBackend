package com.example.LabSystemBackend.dao;

import com.example.LabSystemBackend.entity.TimeSlot;
import com.example.LabSystemBackend.entity.TimeSlotStatus;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.example.LabSystemBackend.entity.TimeSlotStatus.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * * @Version:1.0
 * @Author: Sheyang Li
 * @Date:2022/02/19
 * TimeSlotDao Test
 */
@ActiveProfiles("unittest")
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(value = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TimeSlotDaoTest {

    @Autowired
    TimeSlotDao timeSlotDao;

    private Date[] timeslotDate;
    private int[] slot;
    private TimeSlotStatus[] status;

    @BeforeEach
    public void initArray() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date starDate = sdf.parse("2022-01-01");

        timeslotDate = new Date[]{sdf.parse("2022-05-01"), sdf.parse("2022-05-01"),
                sdf.parse("2022-05-01"), sdf.parse("2021-05-01")};
        slot = new int[]{1, 2, 3, 4};
        status = new TimeSlotStatus[]{FREE, BOOKED, NA, FREE};
        Assert.assertEquals("Array init error", slot.length, timeslotDate.length, status.length);
    }
    @Test
    void getAvailableTimeSlots() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date starDate = sdf.parse("2022-01-01");
        List<TimeSlot> timeSlots = timeSlotDao.getAvailableTimeSlots(starDate);
        assertNotNull(timeSlots);
        assertEquals(1,timeSlots.size());
    }

    @Test
    @Transactional
    void addTimeSlot() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        TimeSlot newTimeSlot = new TimeSlot();
        newTimeSlot.setSlot(4);
        newTimeSlot.setTimeSlotDate(timeslotDate[1]);
        newTimeSlot.setTimeSlotStatus(FREE);
        timeSlotDao.addTimeSlot(newTimeSlot);
        Assert.assertTrue("Fail to insert", slot.length + 1 == newTimeSlot.getTimeSlotId());
    }
    @Test
    @Transactional
    void updateTimeSlotStatus() throws ParseException {
        int id = new Random().nextInt(timeslotDate.length) + 1;
        int status = new Random().nextInt(TimeSlotStatus.values().length);
        TimeSlotStatus newStatus = TimeSlotStatus.values()[status];
        Assert.assertTrue("fail to update status", 1 == timeSlotDao.updateTimeSlotStatus(id, newStatus));
    }

    @Test
    void getLastTimeSlot(){
        int id = timeslotDate.length;
       TimeSlot testTimeslot = timeSlotDao.getLastTimeSlot();
        assertEquals(slot[id-1], testTimeslot.getSlot());
        assertTrue(status[id-1].equals(testTimeslot.getTimeSlotStatus()));
        assertNotNull(testTimeslot);
    }

    @Test
    @Transactional
    void getTimeSlot() throws ParseException {

        Date date = timeslotDate[0];
        int testSlot = slot[0];
        TimeSlot testTimeSlot = timeSlotDao.getTimeSlot(date,1);
        assertAll("timeSlot",
                () -> assertEquals(testSlot, testTimeSlot.getSlot()) ,
                () -> assertEquals(date, testTimeSlot.getTimeSlotDate())
        );

    }

    @Test
    @Transactional
    void getTimeSlotById() throws ParseException {
        int id = new Random().nextInt(slot.length) + 1;
       TimeSlot testTimeSlot = timeSlotDao.getTimeSlotById(id);
       assertEquals(slot[id-1],testTimeSlot.getSlot());
       assertTrue(status[id-1].equals(testTimeSlot.getTimeSlotStatus()));

    }
    @Test
    void timeSlotPeriod() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date starDate = sdf.parse("2021-01-01");
        Date endDate = sdf.parse("2022-05-30");
        List<TimeSlot> timeSlots = timeSlotDao.timeSlotPeriod(starDate,endDate);
        assertNotNull(timeSlots);
        assertEquals(timeslotDate.length,timeSlots.size());
    }

    @Test
    @Transactional
    void timeSlotOneDay() throws ParseException {
        Date date = timeslotDate[0];
        int sum =0;
        for (int i=0; i<timeslotDate.length; i++) {
            if(timeslotDate[i].equals(date)){
                sum = sum +1;
            }
        }
        List<TimeSlot> timeSlots = timeSlotDao.timeSlotOneDay(date);
        assertEquals(sum,timeSlots.size());
    }

//    @Test
//    void getBookedTimeSlot() throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = new Date();
//        String s = sdf.format(date);
//        Date today = sdf.parse(s);
//        List<Map<String, Object>> timeSlots = timeSlotDao.getBookedTimeSlot(today);
//        assertNotNull(timeSlots);
//    }

    @Test
    void getUserBookedTimeSlot() {
        String email = "cong.liu@outlook.de";//set a test user from userId = 2  and has BOOKED appointment
        List<TimeSlot> timeSlots = timeSlotDao.getUserBookedTimeSlot(email);
        assertNotNull(timeSlots);
    }

}