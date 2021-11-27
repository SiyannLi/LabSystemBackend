package com.example.SpringbootTest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {
    private Integer userId;

    private Integer orderId;

    private Integer deviceId;

    private Integer amount;

    private OrderStatus orderStatus;

    @Override
    public String toString() {
        return "Order{" +
                "user=" + userId + '\'' +
                ", orderId=" + orderId + '\'' +
                ", device=" + deviceId + '\'' +
                ", amount=" + amount + '\'' +
                ", orderStatus=" + orderStatus +
                '}';
    }
}
