package com.example.LabSystemBackend.service.impl;

import com.example.LabSystemBackend.dao.AppointmentDao;
import com.example.LabSystemBackend.entity.*;
import com.example.LabSystemBackend.service.AppointmentService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
/**
 * @version 1.0
 * @author  Sheyang Li
 *
 * AppointmentServiceImpl Test
 */
@ActiveProfiles("unittest")
@SpringBootTest
class AppointmentServiceImplTest {
    @MockBean
    AppointmentDao appointmentDao;
    @Autowired
    AppointmentService appointmentService;

    User user = new User(5,"firstName","lastName","test_user@testpse.com",
            UserRole.ADMIN,"a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3",
            UserAccountStatus.ACTIVE,true);
    Appointment appointment = new Appointment(1,1,1);

    @Test
    void getUserAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment);
        Mockito.when(appointmentDao.getUserAppointments(Mockito.anyInt())).thenReturn(appointments);

        List<Appointment> testAppointments = appointmentService.getUserAppointments(user.getUserId());
        Assert.assertEquals(appointment,testAppointments.get(0));
    }

    @Test
    void getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment);
        Mockito.when(appointmentDao.getAllAppointments()).thenReturn(appointments);
        List<Appointment> testAppointments = appointmentService.getAllAppointments();
        Assert.assertEquals(1, testAppointments.size());
        Assert.assertEquals(appointment, testAppointments.get(0));
    }

    @Test
    @Transactional
    void deleteAppointment() throws ParseException {
        Mockito.when(appointmentDao.deleteAppointmentById(Mockito.anyInt())).thenReturn(2);
        assertEquals(2, appointmentService.deleteAppointment(appointment.getAppointmentId()));
    }

    @Test
    @Transactional
    void deleteAppointmentByTimeSlotId() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2030-01-01");
        TimeSlot timeSlot = new TimeSlot(1,date,1,TimeSlotStatus.BOOKED);
        Mockito.when(appointmentDao.deleteAppointmentByTimeSlotId(Mockito.anyInt())).thenReturn(2);
        int num = appointmentService.deleteAppointmentByTimeSlotId(timeSlot.getTimeSlotId());
       assertEquals(2,num);
    }

    @Test
    @Transactional
    void addAppointment() throws ParseException {
        int userId = 1000;
        int timeSlotId = 1001;
        Mockito.when(appointmentDao.addAppointment(Mockito.any())).thenReturn(2);
        int num = appointmentService.addAppointment(userId,timeSlotId);
       assertEquals(2,num);
    }

    @Test
    void getAppointmentByTimeSlotId() {
        int timeSlotId = 1001;
        Mockito.when(appointmentDao.getAppointmentByTimeSlotId(Mockito.anyInt())).thenReturn(appointment);
        Appointment testAppointment = appointmentService.getAppointmentByTimeSlotId(timeSlotId);
        assertEquals(appointment, testAppointment);
    }
}