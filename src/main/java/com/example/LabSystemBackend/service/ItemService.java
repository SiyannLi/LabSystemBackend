package com.example.LabSystemBackend.service;

import com.example.LabSystemBackend.entity.Item;

import java.util.List;

public interface ItemService {

    List<Item> getAllItemsAndAmount();

    Item addItem(String deviceName, int Amount, String link, String description);

    Item deleteItem(int deviceId);

    Item changeItemAmount(int itemId, int newAmount);

    List<Item> mergeItem(int itemId, int targetItemId);
}
