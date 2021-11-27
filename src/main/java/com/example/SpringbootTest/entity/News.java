package com.example.SpringbootTest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class News implements Serializable {

    private User sender;
    private User recipient;
    private String content;

    @Override
    public String toString() {
        return "News{" +
                "sender=" + sender + '\'' +
                ", recipient=" + recipient + '\'' +
                ", content='" + content +
                '}';
    }
}
