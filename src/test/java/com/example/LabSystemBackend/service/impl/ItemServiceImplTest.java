package com.example.LabSystemBackend.service.impl;

import com.example.LabSystemBackend.dao.ItemDao;
import com.example.LabSystemBackend.entity.Item;
import com.example.LabSystemBackend.service.ItemService;
import com.github.javafaker.Faker;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @version 1.0
 * @author  Sheyang Li
 *
 * ItemServiceImpl Test
 */
@ActiveProfiles("unittest")
@SpringBootTest
class ItemServiceImplTest {
    @Autowired
    ItemService itemService;
    @MockBean
    ItemDao itemDao;

    private static final Faker faker = new Faker();
    Item item = new Item(1,"Handy",20,"Huawei");
    List<Item> items = new ArrayList<>();

    @Test
    void getAllItemsAndAmount() {
        items.add(item);
        Mockito.when(itemDao.getAllItems()).thenReturn(items);
        List<Item> testItems = itemService.getAllItemsAndAmount();
        assertNotNull(testItems);
        Assert.assertEquals(item, items.get(0));
    }

    @Test
    @Transactional
    void addItem() {
        Item item ;
        String itemName = faker.food().spice();
        int amount = faker.random().nextInt(1, 100);
        String itemDescri = faker.ancient().primordial();
        Mockito.when(itemDao.addItem(Mockito.any())).thenReturn(2);
        assertEquals(2,itemService.addItem(itemName,amount,itemDescri));
    }

    @Test
    void deleteItem() {
      Mockito.when(itemDao.deleteItem(Mockito.anyInt())).thenReturn(2);
       int num = itemService.deleteItem(item.getItemId());
       assertEquals(2, num);
    }

    @Test
    @Transactional
    void changeItemAmount() {
        Mockito.when(itemDao.changeItemAmount(Mockito.anyInt(),Mockito.anyInt())).thenReturn(2);
        int num = itemService.changeItemAmount(item.getItemId(),30);
        assertEquals(2, num);
    }

    @Test
    @Transactional
    void itemExists() {
        Mockito.when(itemDao.getItemByName(Mockito.anyString())).thenReturn(item);
        Assert.assertEquals(true, itemService.itemExists(item.getItemName()));
    }

    @Test
    @Transactional
    void getItemByName() {
        Mockito.when(itemDao.getItemByName(Mockito.anyString())).thenReturn(item);
        Item get = itemService.getItemByName(item.getItemName());
        assertEquals(item, get);
    }
}