package com.example.LabSystemBackend.service;

import com.example.LabSystemBackend.entity.Appointment;
import com.example.LabSystemBackend.entity.TimeSlot;

import java.util.Date;
import java.util.List;

public interface AppointmentService {
    //获取此用户的所有预约
    List<Appointment> getUserAppointments(String userId);

    //返回从 startDate 开始所有的空闲时间.
    List<TimeSlot> getAvailableTimeSlots(Date startDate);

    //设置可预约时间段
    int setAvailableTimeSlots(Date availableDate, int TimeFrame, int endRepeatAfter);

    //查询所有预约
    List<Appointment> getAllAppointments();

    //删除一个预约
    int deleteAppointment(int appointmentId);

    //用户新建一个预约
    int addAppointment(int userId, TimeSlot timeSlot, String email);
}
