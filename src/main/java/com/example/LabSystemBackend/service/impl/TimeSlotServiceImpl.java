package com.example.LabSystemBackend.service.impl;

import com.example.LabSystemBackend.dao.AppointmentDao;
import com.example.LabSystemBackend.dao.TimeSlotDao;
import com.example.LabSystemBackend.entity.TimeSlot;
import com.example.LabSystemBackend.entity.TimeSlotStatus;
import com.example.LabSystemBackend.service.TimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class TimeSlotServiceImpl implements TimeSlotService {
    @Autowired
    private TimeSlotDao timeSlotDao;



    @Override
    public List<TimeSlot> getAvailableTimeSlots(Date startDate) {
        return timeSlotDao.getAvailableTimeSlots(startDate);
    }

    @Override
    public int setAvailableTimeSlots(Date availableDate, int slot, int endRepeatAfter) {
        int resultCounter = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(availableDate);
        for (int i = 0; i < endRepeatAfter; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, i * endRepeatAfter);
            Date date = calendar.getTime();
            TimeSlot timeSlot = new TimeSlot();
            timeSlot.setTimeSlotDate(date);
            timeSlot.setSlot(slot);
            timeSlot.setTimeSlotStatus(TimeSlotStatus.AVAILABLE);
            resultCounter += timeSlotDao.setAvailableTimeSlot(timeSlot);
        }
        return resultCounter == endRepeatAfter ? 1 : 0;
    }

}
