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
    public int addItem(String deviceName, int Amount, String links, String description) {
        return itemDao.addItem(new Item());
    }

    @Override
    public int deleteItem(int deviceId) {
        return itemDao.deleteItem(deviceId);    }

    @Override
    public int changeItemAmount(int deviceId, int newAmount) {
        return itemDao.changeItemAmount(deviceId,newAmount);
    }

    @Override
    public int mergeItem(int itemId, int targetItemId) {
        return 0;
    }
}
