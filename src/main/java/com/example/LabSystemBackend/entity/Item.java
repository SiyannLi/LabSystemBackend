package com.example.LabSystemBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @version 1.0
 * @author Siyan Li
 *
 * Item
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item implements Serializable {

    private Integer itemId;

    private String itemName;

    private Integer amount;

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


