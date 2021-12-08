package com.example.LabSystemBackend.service.impl;

import com.example.LabSystemBackend.dao.AppointmentDao;
import com.example.LabSystemBackend.entity.Appointment;
import com.example.LabSystemBackend.entity.TimeFrame;
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
    public List<Appointment> getAllAppointments() {
        return null;
    }

    @Override
    public Appointment deleteAppointment(int appointmentId) {
        return null;
    }

    @Override
    public List<TimeFrame> getAvailableTimeFrames(Date startDate) {
        return null;
    }

    @Override
    public List<TimeFrame> setAvailableTimeFrames(Date availableDate, int TimeFrame, int endRepeatAfter) {
        return null;
    }

    @Override
    public Appointment addAppointment(int userId, TimeFrame TimeFrame, String email) {
        return null;
    }
}
