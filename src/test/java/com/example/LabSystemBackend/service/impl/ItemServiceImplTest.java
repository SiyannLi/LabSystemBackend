package com.example.LabSystemBackend.service.impl;

import com.example.LabSystemBackend.dao.ItemDao;
import com.example.LabSystemBackend.entity.Item;
import com.example.LabSystemBackend.service.ItemService;
import com.example.LabSystemBackend.util.DataGenerate;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @version 1.0
 * @author  Sheyang Li
 *
 * ItemServiceImpl Test
 */

@SpringBootTest
class ItemServiceImplTest {
    @Autowired
    ItemService itemService;
    @Autowired
    ItemDao itemDao;

    private static final Faker faker = new Faker();

    @Test
    void getAllItemsAndAmount() {
        List<Item> items = itemService.getAllItemsAndAmount();
        assertNotNull(items);
    }

    @Test
    @Transactional
    void addItem() {
        Item item ;
        String itemName = faker.food().spice();
        int amount = faker.random().nextInt(1, 100);
        String itemDescri = faker.ancient().primordial();
        itemService.addItem(itemName,amount,itemDescri);
        Item get = itemService.getItemByName(itemName);
        assertAll("item",
                () -> assertEquals(itemName, get.getItemName()) ,
                () -> assertEquals(itemDescri, get.getItemDescri())
        );
    }

    @Test
    void deleteItem() {
        Item item = new Item();
        String itemName = faker.food().spice();
        int amount = faker.random().nextInt(1, 100);
        String itemDescri = faker.ancient().primordial();
        itemService.addItem(itemName,amount,itemDescri);
        int itemId = itemService.getItemByName(itemName).getItemId();
        itemService.deleteItem(itemId);
        Item get = itemDao.getItemById(itemId);
        assertNull(get);
    }

    @Test
    @Transactional
    void changeItemAmount() {
        Item item = DataGenerate.generateItem();
        itemDao.addItem(item);
        int itemId = item.getItemId();
        String itemName = item.getItemName();
        itemService.changeItemAmount(itemId,30);
        Item get = itemService.getItemByName(itemName);
        assertEquals(30, get.getAmount());
    }

    @Test
    @Transactional
    void itemExists() {
        Item item = DataGenerate.generateItem();
        itemDao.addItem(item);
        assertTrue(itemService.itemExists(item.getItemName()));
    }

    @Test
    @Transactional
    void getItemByName() {
        Item item = DataGenerate.generateItem();
        itemDao.addItem(item);
        Item get = itemService.getItemByName(item.getItemName());
        assertEquals(item, get);
    }
}