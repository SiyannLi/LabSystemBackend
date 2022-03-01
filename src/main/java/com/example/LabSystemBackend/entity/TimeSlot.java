package com.example.LabSystemBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.0
 * @author Siyan Li
 *
 * Time slot
 */
@Entity
@Table(name = "timeSlot")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlot implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "timeSlotId", insertable=false, updatable=false, nullable = false)
    private int timeSlotId;
    @Column(name = "timeSlotDate", nullable = false, columnDefinition = "date")
    private Date timeSlotDate;
    @Column(name = "slot", nullable = false)
    private Integer slot;
    @Column(name = "timeSlotStatus", length = 64, nullable = false)
    @Enumerated(EnumType.STRING)
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
