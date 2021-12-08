package com.example.LabSystemBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeFrame implements Serializable {

    private Date date;

    private Integer frame;

    private TimeFrameStatus timeFrameStatus;

    @Override
    public String toString() {
        return "TimeFrame{" +
                "date=" + date +
                ", frame=" + frame +
                ", timeFrameStatus=" + timeFrameStatus +
                '}';
    }
}
