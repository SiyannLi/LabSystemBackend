package com.example.LabSystemBackend.service.impl;

import com.example.LabSystemBackend.dao.TimeSlotDao;
import com.example.LabSystemBackend.entity.TimeSlot;
import com.example.LabSystemBackend.service.TimeSlotService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class TimeSlotServiceImplTest {
    @Autowired
    TimeSlotService timeSlotService;
    @Autowired
    TimeSlotDao timeSlotDao;
    @Test
    void getAvailableTimeSlots() {
        Date date = timeSlotDao.getLastTimeSlot().getTimeSlotDate();
        List<TimeSlot> timeSlots = timeSlotService.getAvailableTimeSlots(date);
        assertNotNull(timeSlots);
        for (TimeSlot timeSlot: timeSlots){
            System.out.println(timeSlot);

        }
    }

    @Test
    void setAvailableTimeSlots() {
        TimeSlot timeSlot = timeSlotDao.getLastTimeSlot();
        Date date = timeSlot.getTimeSlotDate();
        int before = timeSlotService.getAvailableTimeSlots(date).size();
        Random random = new Random();
        int slot = random.nextInt(6);
        int repeat = random.nextInt(10);
        timeSlotService.setAvailableTimeSlots(date,slot,repeat);
        int after = timeSlotService.getAvailableTimeSlots(date).size();
        assertEquals(repeat,after-before+1);
    }
}