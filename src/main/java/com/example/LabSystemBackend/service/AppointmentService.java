package com.example.LabSystemBackend.service;

import com.example.LabSystemBackend.entity.Appointment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppointmentService {
    //获取此用户的所有预约
    List<Appointment> getUserAppointments(int userId);

    //查询所有预约
    List<Appointment> getAllAppointments();
    //通过日期时间查预约
    Appointment getAppointmentByTimeSlotId(int timeSlotId);
    //删除一个预约
    int deleteAppointment(int appointmentId);

    int deleteAppointmentByTimeSlotId(int timeSlotId);

    //用户新建一个预约
    int addAppointment(int userId, int timeSlotId);
}
