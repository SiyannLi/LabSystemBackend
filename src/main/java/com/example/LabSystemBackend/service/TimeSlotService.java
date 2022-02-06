package com.example.LabSystemBackend.service;

import com.example.LabSystemBackend.entity.TimeSlot;
import com.example.LabSystemBackend.entity.TimeSlotStatus;
import org.apache.ibatis.annotations.Param;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TimeSlotService {
    //返回从 startDate 开始所有的空闲时间.
    List<TimeSlot> getAvailableTimeSlots(Date startDate);


    //设置可预约时间段
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
