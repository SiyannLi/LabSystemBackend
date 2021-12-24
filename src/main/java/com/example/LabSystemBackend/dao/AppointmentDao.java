package com.example.LabSystemBackend.dao;

import com.example.LabSystemBackend.entity.Appointment;
import com.example.LabSystemBackend.entity.TimeSlot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper

public interface AppointmentDao {
    List<Appointment> getUserAppointments(@Param("userId") String userId);

    List<TimeSlot> getAvailableTimeSlots(@Param("startDate")Date startDate);

    List<Appointment> getAllAppointments();

    int setAvailableTimeSlot(@Param("timeSlot") TimeSlot timeSlot);

    int deleteAppointmentById(@Param("appointmentId") int appointmentId);

    int addAppointment(@Param("appointment") Appointment appointment);





}
