package com.example.LabSystemBackend.dao;

import com.example.LabSystemBackend.entity.Item;
import com.example.LabSystemBackend.util.DataGenerate;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Random;
@ActiveProfiles("unittest")
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(value = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemDaoTest {
    @Autowired
    ItemDao itemDao;

    private Integer[]amount;
    private String[] itemName;
    private String[] itemDescri;

    @BeforeEach
    public void initArray() {
        itemName = new String[]{"Handy_one","Handy_two"};
        amount = new Integer[]{10,20};
        itemDescri = new String[]{"Huawei is No.1","Iphone is No.2"};
        Assert.assertEquals("Array init error", itemName.length, amount.length, itemDescri.length);
    }

    @Test
    void getAllItems() {
      Assert.assertEquals(2, itemDao.getAllItems().size());
    }

    @Test
    void getItemByName() {
        int id = new Random().nextInt(itemName.length);
        Item testItem = itemDao.getItemByName(itemName[id]);
        Assert.assertTrue(testItem.getItemName().equals(itemName[id]));
        Assert.assertTrue(testItem.getItemDescri().equals(itemDescri[id]));
        Assert.assertEquals(testItem.getAmount(),amount[id]);
    }

    @Test
    void getItemById() {
        Item testItem =  itemDao.getItemById(1);
        Assert.assertTrue(testItem.getItemName().equals(itemName[0]));
        Assert.assertTrue(testItem.getItemDescri().equals(itemDescri[0]));
        Assert.assertEquals(testItem.getAmount(),amount[0]);
    }

    @Test
    void addItem() {
        Item newItem = DataGenerate.generateItem();
        itemDao.addItem(newItem);
        Assert.assertTrue("Fail to insert", itemName.length + 1 == newItem.getItemId());
    }

    @Test
    void deleteItem() {
        int id = new Random().nextInt(itemName.length) + 1;
        Assert.assertTrue("fail to delete", 1 == itemDao.deleteItem(id));
    }

    @Test
    void changeItemAmount() {
        int id = new Random().nextInt(amount.length);
        int testamount = amount[id]+10;
        Assert.assertTrue("fail to change amount ",1== itemDao.changeItemAmount(id+1,testamount));
    }
}