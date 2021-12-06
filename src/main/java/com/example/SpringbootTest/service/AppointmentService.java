package com.example.SpringbootTest.service;

import com.example.SpringbootTest.entity.Appointment;
import com.example.SpringbootTest.entity.TimeSlot;

import java.util.Date;
import java.util.List;

public interface AppointmentService {
    //获取此用户的所有预约
    List<Appointment> getUserAppointments(String userId);

    //返回从 startDate 开始所有的空闲时间.
    List<TimeSlot> getAvailableTimeSlots(Date startDate);

    //设置可预约时间段
    List<TimeSlot> setAvailableTimeSlots(Date availableDate, int TimeSlot, int endRepeatAfter);

    //查询所有预约
    List<Appointment> getAllAppointments();

    //删除一个预约
    Appointment deleteAppointment(int appointmentId);

    //用户新建一个预约
    Appointment addAppointment(int userId, int TimeSlot, String email);
}
