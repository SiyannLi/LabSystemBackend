package com.example.LabSystemBackend.dao;

import com.example.LabSystemBackend.entity.Appointment;
import com.example.LabSystemBackend.util.DataGenerate;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @version 1.0
 * @author Siyan Li, Sheyang Li
 *
 * AppointmentDao Test
 */


@DisplayName("Test appointmentDao")
@ActiveProfiles("unittest")
//@RunWith(SpringRunner.class)
@Transactional
@Rollback(value = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class AppointmentDaoTest {
    /*
    * setup two appointments to use test data.
    *
    * */
    @Autowired
    AppointmentDao appointmentDao;

    private int[] userId;
    private int[] timeSlotId;

    @BeforeEach
    public void initArray() {
        userId = new int[]{1,2};
        timeSlotId = new int[]{1,2};
        Assert.assertEquals("Array init error", userId.length, timeSlotId.length);
    }
    @DisplayName("get user's appointments")
    @Test
    void getUserAppointments() {

        int id = new Random().nextInt(timeSlotId.length)+1 ;
        int realUserAppointments = 1;
        List<Appointment> testAppointments = appointmentDao.getUserAppointments(id);
        int listSize = testAppointments.size();
        Assert.assertEquals(realUserAppointments,listSize);
    }

    @DisplayName("get all appointments")
    @Test
    void getAllAppointments() {
        int realAppointmentsListSize = 2;
        List<Appointment> appointments = appointmentDao.getAllAppointments();
        int listSize = appointments.size();
        assertEquals(realAppointmentsListSize, listSize);
    }

    @DisplayName("delete a appointment by timeSlotId")
    @Test
    void deleteAppointmentById()  {

        int id = new Random().nextInt(timeSlotId.length) + 1;
        Assert.assertTrue("fail to delete", 1 == appointmentDao.deleteAppointmentById(id));

    }

    @DisplayName("delete a appointment by appointmentId")
    @Test
    void deleteAppointmentByTimeSlotId()  {
        int id = new Random().nextInt(timeSlotId.length) + 1;
        Assert.assertTrue("fail to delete", 1 == appointmentDao.deleteAppointmentByTimeSlotId(id));
    }

    @DisplayName("insert new appointment")
    @Test
    @Transactional
    void addAppointment() throws ParseException {
        Appointment newAppointment = DataGenerate.generateAppointment();
        appointmentDao.addAppointment(newAppointment);
        Assert.assertTrue("Fail to insert", userId.length + 1 == newAppointment.getAppointmentId());

    }

    @DisplayName("get appointment by timeSlotId")
    @Test
    void getAppointmentByTimeSlotId()  {

        int id = new Random().nextInt(timeSlotId.length) + 1;
        Appointment testAppointment = appointmentDao.getAppointmentByTimeSlotId(id);
        assertEquals(timeSlotId[id-1],testAppointment.getTimeSlotId());
        assertEquals(userId[id-1],testAppointment.getUserId());
    }
}