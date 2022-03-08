package com.example.LabSystemBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @version 1.0
 * @author Siyan Li, Cong Liu
 *
 * Appointment
 */
@Entity
@Table(name = "appointment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "appointmentId", insertable=false, updatable=false, nullable = false)
    private Integer appointmentId;
    @Column(name = "userId", nullable = false)
    private Integer userId;
    @Column(name = "timeSlotId", nullable = false)
    private Integer timeSlotId;


    @Override
    public String toString() {
        return "Appointment{" +
                "user=" + userId +
                ", timeSlot=" + timeSlotId +
                '}';
    }
}
