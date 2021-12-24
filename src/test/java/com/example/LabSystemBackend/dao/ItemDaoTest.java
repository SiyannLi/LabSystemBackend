package com.example.LabSystemBackend.dao;

import com.example.LabSystemBackend.entity.Item;
import com.example.LabSystemBackend.util.DataGenerate;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemDaoTest {

    @Autowired
    ItemDao itemDao;
    private static final Logger logger = LoggerFactory.getLogger(ItemDaoTest.class);


    @Test
    void getAllItems() {
        List<Item> items = itemDao.getAllItems();
        assertNotNull(items);
    }

    @Test
    void getItemByName() {
        Item item = DataGenerate.generateItem();
        itemDao.addItem(item);
        Item get = itemDao.getItemByName(item.getItemName());
        assertEquals(item, get);
        itemDao.deleteItem(item.getItemId());
    }

    @Test
    void getItemById() {
        Item item = DataGenerate.generateItem();
        itemDao.addItem(item);
        Item get = itemDao.getItemById(item.getItemId());
        assertEquals(item, get);
        itemDao.deleteItem(item.getItemId());

    }

    @Test
    void addItem() {
        Item item = DataGenerate.generateItem();
        itemDao.addItem(item);
        Item get = itemDao.getItemById(item.getItemId());
        assertEquals(item, get);
        itemDao.deleteItem(item.getItemId());

    }

    @Test
    void deleteItem() {
        Item item = DataGenerate.generateItem();
        itemDao.addItem(item);
        itemDao.deleteItem(item.getItemId());
        Item get = itemDao.getItemById(item.getItemId());
        assertNull(get);
    }

    @Test
    void changeItemAmount() {
        Item item = DataGenerate.generateItem();
        itemDao.addItem(item);
        itemDao.changeItemAmount(item.getItemId(),30);
        Item get = itemDao.getItemById(item.getItemId());
        assertEquals(30, get.getAmount());
        itemDao.deleteItem(item.getItemId());
    }
}