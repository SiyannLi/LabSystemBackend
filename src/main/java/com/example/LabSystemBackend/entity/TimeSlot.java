package com.example.LabSystemBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlot implements Serializable {

    private int timeSlotId;
    private Date date;

    private Integer slot;

    private TimeSlotStatus timeSlotStatus;

    @Override
    public String toString() {
        return "TimeSlot{" +
                "date=" + date +
                ", frame=" + slot +
                ", timeSlotStatus=" + timeSlotStatus +
                '}';
    }
}
