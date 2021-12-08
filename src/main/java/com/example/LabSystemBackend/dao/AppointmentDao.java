package com.example.LabSystemBackend.dao;

import com.example.LabSystemBackend.entity.Appointment;
import com.example.LabSystemBackend.entity.TimeFrame;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper

public interface AppointmentDao {
    List<Appointment> getUserAppointments(@Param("userId") String userId);

    List<TimeFrame> getAvailableTimeFrames(@Param("startDate")Date startDate);

    List<TimeFrame> setAvailableTimeFrame(@Param("timeFrame") TimeFrame timeFrame);

    List<Appointment> getAllAppointments();

    int deleteAppointmentById(@Param("appointmentId") int appointmentId);

    int addAppointment(@Param("appointment") Appointment appointment);





}
