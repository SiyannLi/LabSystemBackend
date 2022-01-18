package com.example.LabSystemBackend.service;

import com.example.LabSystemBackend.entity.Appointment;
import com.example.LabSystemBackend.entity.TimeSlot;

import java.util.Date;
import java.util.List;

public interface AppointmentService {
    //获取此用户的所有预约
    List<Appointment> getUserAppointments(int userId);

    //查询所有预约
    List<Appointment> getAllAppointments();

    //删除一个预约
    int deleteAppointment(int appointmentId);

    //用户新建一个预约
    int addAppointment(int userId, int timeSlot);
}
