package com.example.LabSystemBackend.dao;

import com.example.LabSystemBackend.entity.Appointment;
import com.example.LabSystemBackend.entity.TimeSlot;
import com.example.LabSystemBackend.entity.TimeSlotStatus;
import com.example.LabSystemBackend.util.DataGenerate;
import com.github.javafaker.App;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AppointmentDaoTest {

    @Autowired
    AppointmentDao appointmentDao;
    private static final Logger logger = LoggerFactory.getLogger(AppointmentDaoTest.class);

    @Test
    void getUserAppointments() {
        List<Appointment> apps = appointmentDao.getAllAppointments();
        Appointment a = apps.get(0);
        int userId = a.getUserId();

        List<Appointment> appointments = appointmentDao.getUserAppointments(userId);
        assertNotNull(appointments);
    }


    @Test
    void getAllAppointments() {
        List<Appointment> appointments = appointmentDao.getAllAppointments();
        assertNotNull(appointments);
    }


    @Test
    void deleteAppointmentById() throws ParseException {
        Appointment appointment = DataGenerate.generateAppointment();
        appointmentDao.addAppointment(appointment);
        appointmentDao.deleteAppointmentById(appointment.getAppointmentId());
        Appointment get = appointmentDao.getAppointment(appointment.getAppointmentId());
        assertNull(get);

    }

    @Test
    void addAppointment() throws ParseException {
        Appointment appointment = DataGenerate.generateAppointment();
        appointmentDao.addAppointment(appointment);
        assertNotNull(appointment.getAppointmentId());
        appointmentDao.deleteAppointmentById(appointment.getAppointmentId());
    }
    @Test
    void addFakeData() throws ParseException {
        for (int i = 0; i < 10; i++) {
            Appointment appointment = DataGenerate.generateAppointment();
            appointmentDao.addAppointment(appointment);
            assertNotNull(appointment.getAppointmentId());
        }
    }
}