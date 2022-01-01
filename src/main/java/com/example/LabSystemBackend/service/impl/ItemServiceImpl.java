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
    public int addItem(String itemName, int amount, String link, String description) {
        Item item = new Item();
        item.setItemName(itemName);
        item.setAmount(amount);
        item.setItemDescri(description);
        item.setLink(link);
        return itemDao.addItem(item);
    }

    @Override
    public int deleteItem(int itemId) {
        return itemDao.deleteItem(itemId);
    }

    @Override
    public int changeItemAmount(int itemId, int newAmount) {
        return itemDao.changeItemAmount(itemId, newAmount);
    }

    @Override
    public int mergeItem(int itemId, int targetItemId) {
        Item item = itemDao.getItemById(itemId);
        Item target = itemDao.getItemById(targetItemId);
        int amount = item.getAmount() + target.getAmount();
        if (itemDao.changeItemAmount(targetItemId, amount) == 1 &&
                itemDao.deleteItem(itemId) == 1) {
            return 1;
        } else {
            return 0;
        }

    }
}
