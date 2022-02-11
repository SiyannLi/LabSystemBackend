package com.example.LabSystemBackend.service;

import com.example.LabSystemBackend.entity.TimeSlot;
import com.example.LabSystemBackend.entity.TimeSlotStatus;
import org.apache.ibatis.annotations.Param;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @author Siyan Li
 *
 * Time slot servise
 */
public interface TimeSlotService {
    List<TimeSlot> getAvailableTimeSlots(Date startDate);

    int setPeriodTimeSlotsFREE(Date availableDate, int slot, int endRepeatAfter);

    int setPeriodTimeSlotsNA(Date availableDate, int slot, int endRepeatAfter);

    int updateTimeSlotStatus(int timeSlotId, TimeSlotStatus timeSlotStatus);

    List<TimeSlot> timeSlotPeriod(Date startDate, Date endDate) throws ParseException;

    List<TimeSlot> timeSlotOneDay(Date date);

    List<Map<String, Object>> getBookedTimeSlot(Date today);

    TimeSlot getTimeSlot(Date date, int slot);

    TimeSlot getTimeSlotById(int timeSlotId);

    boolean TimeSlotExists(Date date, int slot);

    List<TimeSlot> getUserBookedTimeSlot(String email);

}
