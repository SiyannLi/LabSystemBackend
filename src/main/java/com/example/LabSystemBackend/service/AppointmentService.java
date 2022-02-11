package com.example.LabSystemBackend.service;

import com.example.LabSystemBackend.entity.Appointment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version 1.0
 * @author Siyan Li
 *
 * Appointment Service
 */
public interface AppointmentService {
    List<Appointment> getUserAppointments(int userId);
    List<Appointment> getAllAppointments();
    int deleteAppointment(int appointmentId);
    int deleteAppointmentByTimeSlotId(int timeSlotId);
    int addAppointment(int userId, int timeSlotId);
    Appointment getAppointmentByTimeSlotId(int timeSlotId);
}
