package com.example.LabSystemBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @version 1.0
 * @author Cong Liu, Siyan Li
 *
 * Order
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {
    private Integer userId;

    private Integer orderId;

    private String itemName;

    private String itemLink;

    private Integer amount;

    private OrderStatus orderStatus;

    private String contactEmail;

    @Override
    public String toString() {
        return "Order{" +
                "user=" + userId +
                ", orderId=" + orderId +
                ", item=" + itemName +
                ", amount=" + amount +
                ", itemLink" + itemLink +
                ", orderStatus=" + orderStatus +
                ", contactEmail" + contactEmail +
                '}';
    }
}
