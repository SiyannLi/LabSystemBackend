package com.example.LabSystemBackend.dao;

import com.example.LabSystemBackend.entity.TimeSlot;
import com.example.LabSystemBackend.entity.TimeSlotStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface TimeSlotDao {
    List<TimeSlot> getAvailableTimeSlots(@Param("startDate") Date startDate);

    int addTimeSlot(@Param("timeSlot") TimeSlot timeSlot);

    int updateTimeSlotStatus(@Param("timeSlotId") int timeSlotId, @Param("timeSlotStatus")TimeSlotStatus timeSlotStatus);

    TimeSlot getLastTimeSlot();

    TimeSlot getTimeSlot(@Param("date") Date date, @Param("slot") int slot);

    TimeSlot getTimeSlotById(@Param("timeSlotId") int timeSlotId);

    List<TimeSlot> timeSlotPeriod(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<TimeSlot> timeSlotOneDay(@Param("date") Date date);

    List<Map<String, Object>> getBookedTimeSlot(@Param("today") Date today);

    List<TimeSlot> getUserBookedTimeSlot(@Param("email") String email);
}
