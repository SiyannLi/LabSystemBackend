package com.example.LabSystemBackend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlot implements Serializable {

    private int timeSlotId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+3")
    private Date timeSlotDate;

    private Integer slot;

    private TimeSlotStatus timeSlotStatus;

    @Override
    public String toString() {
        return "TimeSlot{" +
                "date=" + timeSlotDate +
                ", frame=" + slot +
                ", timeSlotStatus=" + timeSlotStatus +
                '}';
    }
}
