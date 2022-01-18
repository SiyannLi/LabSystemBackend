package com.example.LabSystemBackend.dao;

import com.example.LabSystemBackend.entity.TimeSlot;
import com.example.LabSystemBackend.entity.TimeSlotStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface TimeSlotDao {
    List<TimeSlot> getAvailableTimeSlots(@Param("startDate") Date startDate);

    int setAvailableTimeSlot(@Param("timeSlot") TimeSlot timeSlot);

    TimeSlot getLastTimeSlot();

    int updateTimeSlotStatus(@Param("timeSlotId") int timeSlotId, @Param("timeslotStatus")TimeSlotStatus timeSlotStatus );

    TimeSlot getTimeSlotSlotStatus(@Param("timeSlotId")int timeSlotId);
}
