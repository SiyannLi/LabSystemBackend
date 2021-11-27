package com.example.SpringbootTest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment implements Serializable {

    private User user;

    private TimeSlot startTime;

    private TimeSlot entTime;

    @Override
    public String toString() {
        return "Appointment{" +
                "user=" + user.getUserId() + "\n" +
                ", startTime=" + startTime + "\n" +
                ", entTime=" + entTime + "\n" +
                '}';
    }
}
