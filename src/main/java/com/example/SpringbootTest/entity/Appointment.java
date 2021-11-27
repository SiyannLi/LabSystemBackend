package com.example.SpringbootTest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment implements Serializable {

    private Integer userId;

    private TimeSlot startTime;

    private TimeSlot entTime;

    @Override
    public String toString() {
        return "Appointment{" +
                "user=" + userId + "\n" +
                ", startTime=" + startTime + "\n" +
                ", entTime=" + entTime +
                '}';
    }
}
