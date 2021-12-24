package com.example.LabSystemBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Appointment implements Serializable {

    private Integer appointmentId;

    private Integer userId;

    private Integer timeSlotId;


    @Override
    public String toString() {
        return "Appointment{" +
                "user=" + userId +
                ", timeSlot=" + timeSlotId +
                '}';
    }
}
