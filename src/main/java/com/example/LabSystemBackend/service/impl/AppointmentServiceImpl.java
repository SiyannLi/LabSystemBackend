package com.example.LabSystemBackend.service.impl;

import com.example.LabSystemBackend.dao.AppointmentDao;
import com.example.LabSystemBackend.dao.TimeSlotDao;
import com.example.LabSystemBackend.entity.Appointment;
import com.example.LabSystemBackend.entity.TimeSlot;
import com.example.LabSystemBackend.service.AppointmentService;
import com.example.LabSystemBackend.service.TimeSlotService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    private AppointmentDao appointmentDao;
    @Autowired
    private TimeSlotService timeSlotService;


    @Override
    public List<Appointment> getUserAppointments(int userId) {
        return appointmentDao.getUserAppointments(userId);
    }


    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentDao.getAllAppointments();
    }

    @Override
    public List<Appointment> getAppointmentbyTimeSlot(int timeSlotId){ return appointmentDao.getAppointmentbyTimeSlot(timeSlotId);}

    @Override
    public int deleteAppointment(int appointmentId) {
        int a=appointmentDao.getAppointment(appointmentId).getTimeSlotId();
        int b= appointmentDao.deleteAppointmentById(appointmentId);
        timeSlotService.activateTimeSlotStatus(a);
        return b;
    }


    @Override
    public int addAppointment(int userId, int timeSlotId) {
        int d = timeSlotId;
        Appointment appointment = new Appointment();
        appointment.setUserId(userId);
        appointment.setTimeSlotId(timeSlotId);
        if (appointmentDao.getAppointmentbyTimeSlot(timeSlotId).isEmpty()){
        int c =appointmentDao.addAppointment(appointment);
        timeSlotService.deactivateTimeSlotStatus(d);
        return c;}
        else return 0;//返回预定不成功
    }
}
