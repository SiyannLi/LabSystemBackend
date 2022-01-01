package com.example.LabSystemBackend.service;

import com.example.LabSystemBackend.entity.Item;

import java.util.List;

public interface ItemService {

    List<Item> getAllItemsAndAmount();

    int addItem(String itemName, int amount, String link, String description);

    int deleteItem(int itemId);

    int changeItemAmount(int itemId, int newAmount);

    int mergeItem(int itemId, int targetItemId);
}
