package com.example.LabSystemBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification implements Serializable {

    private Integer notificationId;
    private Integer senderId;
    private Integer recipientId;
    private String content;

    @Override
    public String toString() {
        return "Notification{" +
                "sender=" + senderId + '\'' +
                ", recipient=" + recipientId + '\'' +
                ", content='" + content +
                '}';
    }
}
