package com.example.SpringbootTest.entity;

import java.io.Serializable;
import java.util.Date;

public class TimeSlot implements Serializable {

    private Date date;

    private Integer slot;

    private TimeSlotStatus timeSlotStatus;

    @Override
    public String toString() {
        return "TimeSlot{" +
                "date=" + date + '\'' +
                ", slot=" + slot + '\'' +
                ", timeSlotStatus=" + timeSlotStatus +
                '}';
    }
}
