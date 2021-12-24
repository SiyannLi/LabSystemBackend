package com.example.LabSystemBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification implements Serializable {

    private int notificationId;
    private User sender;
    private User recipient;
    private String content;

    @Override
    public String toString() {
        return "Notification{" +
                "sender=" + sender + '\'' +
                ", recipient=" + recipient + '\'' +
                ", content='" + content +
                '}';
    }
}
