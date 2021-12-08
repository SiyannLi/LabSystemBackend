package com.example.SpringbootTest.service.impl;

import com.example.SpringbootTest.dao.AppointmentDao;
import com.example.SpringbootTest.entity.Appointment;
import com.example.SpringbootTest.entity.TimeSlot;
import com.example.SpringbootTest.service.AppointmentService;
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
    public List<TimeSlot> setAvailableTimeSlots(Date availableDate, int TimeSlot, int endRepeatAfter) {
        return null;
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return null;
    }

    @Override
    public Appointment deleteAppointment(int appointmentId) {
        return null;
    }

    @Override
    public Appointment addAppointment(int userId, int TimeSlot, String email) {
        return null;
    }
}
