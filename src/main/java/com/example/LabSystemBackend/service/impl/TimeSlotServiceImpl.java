package com.example.LabSystemBackend.service.impl;

import com.example.LabSystemBackend.dao.TimeSlotDao;
import com.example.LabSystemBackend.entity.TimeSlot;
import com.example.LabSystemBackend.entity.TimeSlotStatus;
import com.example.LabSystemBackend.service.TimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * @version 1.0
 * @author Siyan Li
 *
 * Implement of Time slot servise
 */
@Service
public class TimeSlotServiceImpl implements TimeSlotService {
    @Autowired
    private TimeSlotDao timeSlotDao;


    @Override
    public List<TimeSlot> getAvailableTimeSlots(Date startDate) {
        return timeSlotDao.getAvailableTimeSlots(startDate);
    }

    @Override
    public int setPeriodTimeSlotsFREE(Date availableDate, int slot, int endRepeatAfter) {
        int resultCounter = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(availableDate);
        for (int i = 0; i < endRepeatAfter; i++) {
            Date date = calendar.getTime();
            TimeSlot timeSlot = new TimeSlot();
            timeSlot.setTimeSlotDate(date);
            timeSlot.setSlot(slot);
            timeSlot.setTimeSlotStatus(TimeSlotStatus.FREE);

            TimeSlot oldSlot =timeSlotDao.getTimeSlot(date,slot);
            if ( oldSlot == null) {
                resultCounter += timeSlotDao.addTimeSlot(timeSlot);
            } else if (oldSlot.getTimeSlotStatus() == TimeSlotStatus.NA){
                resultCounter +=  timeSlotDao.updateTimeSlotStatus(oldSlot.getTimeSlotId(), TimeSlotStatus.FREE);
            }

            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }
        return resultCounter;
    }

    @Override
    public int setPeriodTimeSlotsNA(Date availableDate, int slot, int endRepeatAfter) {
        int resultCounter = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(availableDate);
        for (int i = 0; i < endRepeatAfter; i++) {
            Date date = calendar.getTime();

            TimeSlot oldSlot =timeSlotDao.getTimeSlot(date,slot);
            if ( oldSlot == null) {
                resultCounter += 1;
            } else if (oldSlot.getTimeSlotStatus() == TimeSlotStatus.FREE){
                resultCounter +=  timeSlotDao.updateTimeSlotStatus(oldSlot.getTimeSlotId(), TimeSlotStatus.NA);
            }

            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }
        return resultCounter;
    }

    @Override
    public int updateTimeSlotStatus(int timeSlotId, TimeSlotStatus timeSlotStatus) {
        return timeSlotDao.updateTimeSlotStatus(timeSlotId, timeSlotStatus);
    }

    @Override
    public List<TimeSlot> timeSlotPeriod(Date startDate, Date endDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<TimeSlot> timeSlots = timeSlotDao.timeSlotPeriod(startDate, endDate);
        return timeSlots;
    }

    @Override
    public List<TimeSlot> timeSlotOneDay(Date date) {
        return timeSlotDao.timeSlotOneDay(date);
    }

    @Override
    public List<Map<String, Object>> getBookedTimeSlot(Date today) {
        List<Map<String, Object>> books = timeSlotDao.getBookedTimeSlot(today);
        return books;
    }

    @Override
    public TimeSlot getTimeSlot(Date date, int slot) {
        return timeSlotDao.getTimeSlot(date, slot);
    }

    @Override
    public TimeSlot getTimeSlotById(int timeSlotId) {
        return timeSlotDao.getTimeSlotById(timeSlotId);
    }

    @Override
    public boolean TimeSlotExists(Date date, int slot) {
        return getTimeSlot(date, slot) != null;
    }

    @Override
    public List<TimeSlot> getUserBookedTimeSlot(String email){
        return timeSlotDao.getUserBookedTimeSlot(email);
    }

}
