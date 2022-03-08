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
 * Notification
 */
@Entity
@Data
@Table(name = "noti")
@NoArgsConstructor
@AllArgsConstructor
public class Notification implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "notiId", insertable=false, updatable=false, nullable = false)
    private Integer notificationId;
    @Column(name = "senderId", nullable = false)
    private Integer senderId;
    @Column(name = "recipientId", nullable = false)
    private Integer recipientId;
    @Lob
    @Column(name = "subject", columnDefinition = "text")
    private String subject;
    @Lob
    @Column(name = "content", columnDefinition = "text")
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
