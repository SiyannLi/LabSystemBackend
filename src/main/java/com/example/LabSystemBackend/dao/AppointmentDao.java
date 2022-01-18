package com.example.LabSystemBackend.dao;

import com.example.LabSystemBackend.entity.Appointment;
import com.example.LabSystemBackend.entity.TimeSlot;
import com.github.javafaker.App;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper

public interface AppointmentDao {
    List<Appointment> getUserAppointments(@Param("userId") int userId);

    List<Appointment> getAllAppointments();

    Appointment getAppointment(@Param("appointmentId") int appointmentId);

    int deleteAppointmentById(@Param("appointmentId") int appointmentId);

    int addAppointment(@Param("appointment") Appointment appointment);

    List<Appointment> getAppointmentbyTimeSlot(@Param("timeSlotId") int timeSlotId);



}
