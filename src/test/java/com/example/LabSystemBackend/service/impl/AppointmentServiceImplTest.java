package com.example.LabSystemBackend.service.impl;

import com.example.LabSystemBackend.dao.AppointmentDao;
import com.example.LabSystemBackend.entity.Appointment;
import com.example.LabSystemBackend.service.AppointmentService;
import com.example.LabSystemBackend.util.DataGenerate;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/**
 * @version 1.0
 * @author  Sheyang Li
 *
 * AppointmentServiceImpl Test
 */
@ActiveProfiles("unittest")
@Transactional
@Rollback(value = true)
@SpringBootTest
class AppointmentServiceImplTest {
    @Autowired
    AppointmentDao appointmentDao;
    @Autowired
    AppointmentService appointmentService;
    private static final Logger logger = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    @Test
    void getUserAppointments() {
        List<Appointment> apps = appointmentDao.getAllAppointments();
        System.out.println(apps);
        Appointment a = apps.get(0);
        int userId = a.getUserId();

        List<Appointment> appointments = appointmentService.getUserAppointments(userId);
        assertNotNull(appointments);
    }

    @Test
    void getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        assertNotNull(appointments);
    }

    @Test
    void deleteAppointment() throws ParseException {
        Appointment appointment = DataGenerate.generateAppointment();
        appointmentDao.addAppointment(appointment);
        Assert.assertNotNull("no new appointment find",appointment);
        int appointmentId = appointment.getAppointmentId();
        appointmentService.deleteAppointment(appointmentId);
        Appointment get = appointmentDao.getAppointment(appointmentId);
        assertNull(get);
    }

    @Test
    void deleteAppointmentByTimeSlotId() throws ParseException {
        Appointment appointment = DataGenerate.generateAppointment();
        appointmentDao.addAppointment(appointment);
        Assert.assertNotNull("no new appointment find",appointment);
        int timeSlotId = appointment.getTimeSlotId();
        int appointmentId = appointment.getAppointmentId();
        appointmentService.deleteAppointmentByTimeSlotId(timeSlotId);
        Appointment get = appointmentDao.getAppointment(appointmentId);
        assertNull(get);
    }

    @Test
    void addAppointment() throws ParseException {
        int userId = 1000;
        int timeSlotId = 1001;
        appointmentService.addAppointment(userId,timeSlotId);
        Appointment appointmentTest;
        appointmentTest = appointmentDao.getAppointmentByTimeSlotId(timeSlotId);
        assertAll("appointment",
                () -> assertEquals(1001, appointmentTest.getTimeSlotId()) ,
                () -> assertEquals(1000, appointmentTest.getUserId())
        );
    }

    @Test
    void getAppointmentByTimeSlotId() {
        List<Appointment> apps = appointmentDao.getAllAppointments();
        Appointment a = apps.get(0);
        int timeSlotId = a.getTimeSlotId();
        Appointment appointment = appointmentService.getAppointmentByTimeSlotId(timeSlotId);
        assertEquals(a,appointment);
    }
}