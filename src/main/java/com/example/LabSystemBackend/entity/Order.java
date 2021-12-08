package com.example.LabSystemBackend.entity;

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

    private Item item;

    private Integer amount;

    private OrderStatus orderStatus;

    @Override
    public String toString() {
        return "Order{" +
                "user=" + user.getFirstName() +
                ", orderId=" + orderId +
                ", item=" + item +
                ", amount=" + amount +
                ", orderStatus=" + orderStatus +
                '}';
    }
}
