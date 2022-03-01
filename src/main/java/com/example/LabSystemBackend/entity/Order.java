package com.example.LabSystemBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @version 1.0
 * @author Cong Liu, Siyan Li
 *
 * Order
 */
@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {
    @Column(name = "userId", nullable = false)
    private Integer userId;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "orderId", insertable=false, updatable=false, nullable = false)
    private Integer orderId;
    @Column(name = "itemName", length = 64, nullable = false)
    private String itemName;
    @Lob
    @Column(name = "itemLink", nullable = false, columnDefinition = "text")
    private String itemLink;
    @Column(name = "amount", nullable = false)
    private Integer amount;
    @Column(name = "orderStatus", length = 64, nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Column(name = "contactEmail", length = 64, nullable = false)
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
