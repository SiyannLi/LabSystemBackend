package com.example.LabSystemBackend.dao;

import com.example.LabSystemBackend.entity.TimeSlot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface TimeSlotDao {
    List<TimeSlot> getAvailableTimeSlots(@Param("startDate") Date startDate);

    int setAvailableTimeSlot(@Param("timeSlot") TimeSlot timeSlot);

    TimeSlot getLastTimeSlot();
}
