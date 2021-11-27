package com.example.SpringbootTest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Device implements Serializable {

    private Integer deviceId;

    private String deviceName;

    private Integer amount;

    private String link;

    @Override
    public String toString() {
        return "Device{" +
                "deviceId=" + deviceId + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", amount=" + amount + '\'' +
                ", link='" + link +
                '}';
    }
}
