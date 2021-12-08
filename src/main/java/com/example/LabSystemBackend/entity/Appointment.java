package com.example.LabSystemBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment implements Serializable {

    private User user;

    private TimeFrame timeFrame;


    @Override
    public String toString() {
        return "Appointment{" +
                "user=" + user.getUserId() +
                ", timeFrame=" + timeFrame +
                '}';
    }
}
