package com.example.LabSystemBackend.service;

import com.example.LabSystemBackend.entity.Item;

import java.util.List;
/**
 * @version 1.0
 * @author Cong Liu, Siyan Li
 *
 * Item Service
 */

public interface ItemService {

    List<Item> getAllItemsAndAmount();

    int addItem(String itemName, int amount, String description);

    int deleteItem(int itemId);

    int changeItemAmount(int itemId, int newAmount);

    Boolean itemExists(String itemName);

    Item getItemByName(String itemName);
}
