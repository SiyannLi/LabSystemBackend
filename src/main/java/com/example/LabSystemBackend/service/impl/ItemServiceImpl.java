package com.example.LabSystemBackend.service.impl;

import com.example.LabSystemBackend.dao.ItemDao;
import com.example.LabSystemBackend.entity.Item;
import com.example.LabSystemBackend.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * @version 1.0
 * @author Cong Liu, Siyan Li
 *
 * Implement of Item Service
 */
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemDao itemDao;

    @Override
    public List<Item> getAllItemsAndAmount() {
        return itemDao.getAllItems();
    }

    @Override
    public int addItem(String itemName, int amount, String description) {
        Item item = new Item();
        item.setItemName(itemName);
        item.setAmount(amount);
        item.setItemDescri(description);
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
    public Boolean itemExists(String itemName) {
        return itemDao.getItemByName(itemName) != null;
    }

    @Override
    public Item getItemByName(String itemName) {
        return itemDao.getItemByName(itemName);
    }

}
