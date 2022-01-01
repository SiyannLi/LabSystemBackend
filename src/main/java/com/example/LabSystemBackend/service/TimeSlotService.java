package com.example.LabSystemBackend.service;

import com.example.LabSystemBackend.entity.TimeSlot;

import java.util.Date;
import java.util.List;

public interface TimeSlotService {
    //返回从 startDate 开始所有的空闲时间.
    List<TimeSlot> getAvailableTimeSlots(Date startDate);


    //设置可预约时间段
    int setAvailableTimeSlots(Date availableDate, int TimeSlot, int endRepeatAfter);

}
