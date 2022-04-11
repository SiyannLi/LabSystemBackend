package com.example.LabSystemBackend.dao;

import com.example.LabSystemBackend.entity.Appointment;
import com.example.LabSystemBackend.entity.TimeSlot;
import com.github.javafaker.App;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 * @author Siyan Li
 *
 * Appointment Dao
 */
@Mapper
public interface AppointmentDao {
    List<Appointment> getUserAppointments(@Param("userId") int userId);

    List<Appointment> getAllAppointments();

    Appointment getAppointment(@Param("appointmentId") int appointmentId);

    Appointment getAppointmentByTimeSlotId(@Param("timeSlotId") int timeSlotId);

    int deleteAppointmentById(@Param("appointmentId") int appointmentId);

    int deleteAppointmentByTimeSlotId(@Param("timeSlotId") int timeSlotId);

    int addAppointment(@Param("appointment") Appointment appointment);

    //Appointment getAppointmentByTimeSlotId(@Param("timeSlotId") int timeSlotId);

}
