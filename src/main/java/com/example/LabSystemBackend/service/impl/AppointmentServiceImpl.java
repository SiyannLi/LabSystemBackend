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
    public List<Appointment> getUserAppointments(String userId) {
        return appointmentDao.getUserAppointments(userId);
    }

    @Override
    public List<TimeSlot> getAvailableTimeSlots(Date startDate) {
        return null;
    }

    @Override
    public int setAvailableTimeSlots(Date availableDate, int TimeFrame, int endRepeatAfter) {
        return 0;
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
    public int addAppointment(int userId, TimeSlot timeSlot, String email) {
        return appointmentDao.addAppointment(new Appointment());
    }
}
