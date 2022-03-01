package com.example.LabSystemBackend.dao;

import com.example.LabSystemBackend.entity.TimeSlot;
import com.example.LabSystemBackend.entity.TimeSlotStatus;
import com.example.LabSystemBackend.util.DataGenerate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class TimeSlotDaoTest {

    @Autowired
    TimeSlotDao timeSlotDao;
    @Test
    void getAvailableTimeSlots() {
        List<TimeSlot> timeSlots = timeSlotDao.getAvailableTimeSlots(new Date(100,1,1));
        assertNotNull(timeSlots);
    }
    @Test
    void setAvailableTimeSlot() throws ParseException {
        TimeSlot timeSlot = DataGenerate.generateTimeSlot();
        timeSlot.setTimeSlotStatus(TimeSlotStatus.AVAILABLE);

        timeSlotDao.setAvailableTimeSlot(timeSlot);
        assertNotNull(timeSlot.getTimeSlotId());
    }
    @Test
    void addFakeData() throws ParseException {
        for (int i = 0; i < 10; i++) {
            TimeSlot timeSlot = DataGenerate.generateTimeSlot();
            timeSlot.setTimeSlotStatus(TimeSlotStatus.AVAILABLE);

            timeSlotDao.setAvailableTimeSlot(timeSlot);
        }
    }


    @Test
    void getLastTimeSlot() {
        TimeSlot timeSlot = timeSlotDao.getLastTimeSlot();
        assertNotNull(timeSlot);
    }
}