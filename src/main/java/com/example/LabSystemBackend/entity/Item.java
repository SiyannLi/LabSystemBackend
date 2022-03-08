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
 * Item
 */
@Entity
@Table(name = "item")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "itemId", insertable=false, updatable=false, nullable = false)
    private Integer itemId;
    @Column(name = "itemName", length = 64, nullable = false)
    private String itemName;
    @Column(name = "amount", nullable = false)
    private Integer amount;
    @Lob
    @Column(name = "itemDescri", nullable = false, columnDefinition = "text")
    private String itemDescri;


    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", amount=" + amount +
                ", description='" + itemDescri + '\'' +
                '}';
    }
}


