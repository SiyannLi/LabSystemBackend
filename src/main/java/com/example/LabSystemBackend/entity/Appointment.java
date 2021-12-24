package com.example.LabSystemBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Appointment implements Serializable {

    private int appointmentId;

    private User user;

    private TimeSlot timeSlot;


    @Override
    public String toString() {
        return "Appointment{" +
                "user=" + user.getUserId() +
                ", timeSlot=" + timeSlot +
                '}';
    }
}
