package com.example.SpringbootTest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {
    private User user;

    private Integer orderId;

    private Device device;

    private Integer amount;

    private OrderStatus orderStatus;

    @Override
    public String toString() {
        return "Order{" +
                "user=" + user.getUserId() + '\'' +
                ", orderId=" + orderId + '\'' +
                ", device=" + device.getDeviceId() + '\'' +
                ", amount=" + amount + '\'' +
                ", orderStatus=" + orderStatus +
                '}';
    }
}
