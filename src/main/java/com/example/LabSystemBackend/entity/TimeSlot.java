package com.example.LabSystemBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.0
 * @author Siyan Li
 *
 * Time slot
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlot implements Serializable {

    private int timeSlotId;
    private Date timeSlotDate;

    private Integer slot;

    private TimeSlotStatus timeSlotStatus;

    @Override
    public String toString() {
        return "TimeSlot{" +
                "date=" + timeSlotDate +
                ", slot=" + slot +
                ", timeSlotStatus=" + timeSlotStatus +
                '}';
    }
}
