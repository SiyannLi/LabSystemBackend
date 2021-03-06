package com.example.LabSystemBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @version 1.0
 * @author Siyan Li
 *
 * Notification
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification implements Serializable {

    private Integer notificationId;
    private Integer senderId;
    private Integer recipientId;
    private String subject;
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
