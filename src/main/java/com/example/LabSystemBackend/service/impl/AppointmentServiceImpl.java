package com.example.LabSystemBackend.service.impl;

import com.example.LabSystemBackend.dao.AppointmentDao;
import com.example.LabSystemBackend.entity.Appointment;
import com.example.LabSystemBackend.entity.TimeSlot;
import com.example.LabSystemBackend.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    private AppointmentDao appointmentDao;

    @Override
    public List<Appointment> getUserAppointments(int userId) {
        return appointmentDao.getUserAppointments(userId);
    }

    @Override
    public Appointment getAppointmentByTimeSlotId(int timeSlotId) {
        return appointmentDao.getAppointmentByTimeSlotId(timeSlotId);
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentDao.getAllAppointments();
    }

    @Override
    public int deleteAppointment(int appointmentId) {
        return appointmentDao.deleteAppointmentById(appointmentId);
    }

    @Override
    public int deleteAppointmentByTimeSlotId(int timeSlotId) {
        return appointmentDao.deleteAppointmentByTimeSlotId(timeSlotId);
    }

    @Override
    public int addAppointment(int userId, int timeSlotId) {
        Appointment appointment = new Appointment();
        appointment.setUserId(userId);
        appointment.setTimeSlotId(timeSlotId);

        return appointmentDao.addAppointment(appointment);
    }
}
