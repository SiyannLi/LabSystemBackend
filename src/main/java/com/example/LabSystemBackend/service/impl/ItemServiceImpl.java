package com.example.LabSystemBackend.service.impl;

import com.example.LabSystemBackend.dao.ItemDao;
import com.example.LabSystemBackend.entity.Item;
import com.example.LabSystemBackend.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemDao itemDao;
    @Override
    public List<Item> getAllItemsAndAmount() {
        return itemDao.getAllItems();
    }

    @Override
    public Item addItem(String deviceName, int Amount, String links, String description) {
        return null;
    }

    @Override
    public Item deleteItem(int deviceId) {
        return null;
    }

    @Override
    public Item changeItemAmount(int deviceId, int newAmount) {
        return null;
    }

    @Override
    public List<Item> mergeItem(int itemId, int targetItemId) {
        return null;
    }
}
