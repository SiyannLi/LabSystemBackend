package com.example.SpringbootTest.dao;

import com.example.SpringbootTest.entity.Appointment;
import com.example.SpringbootTest.entity.TimeSlot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper

public interface AppointmentDao {
    List<Appointment> getUserAppointments(@Param("userId") String userId);

    List<TimeSlot> getAvailableTimeSlots();

    List<TimeSlot> setAvailableTimeSlots(@Param("timeSlot") TimeSlot timeSlot);

    List<Appointment> getAllAppointments();

    int deleteAppointmentById(@Param("appointmentId") int appointmentId);

    int addAppointment(@Param("appointment") Appointment appointment);





}
