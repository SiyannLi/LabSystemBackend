package com.example.LabSystemBackend.dao;

import com.example.LabSystemBackend.entity.Order;
import com.example.LabSystemBackend.entity.OrderStatus;
import com.example.LabSystemBackend.util.DataGenerate;
import com.github.javafaker.Faker;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.example.LabSystemBackend.entity.OrderStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @version 1.0
 * @author  Sheyang Li
 *
 * ItemServiceImpl Test
 */
@ActiveProfiles("unittest")
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(value = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderDaoTest {
    // setup 6 orders in data.sql als test orders
    private Integer[]userId;
    private String[] itemName;
    private String[] itemLink;
    private Integer[] amount;
    private OrderStatus[] status;
    private String[] contactEmail;

    @BeforeAll
    public void initArray() {
        userId = new Integer[]{1,1,1,2,2,2,};
        itemName= new String[]{"Handy_one","Handy_two","Handy_three","Handy_four","Handy_five","Handy_six"};
        itemLink =new String[]{"www.ebay.de","www.ebay.de","www.ebay.de","www.ebay.de","www.ebay.de","www.ebay.de"};
        amount = new Integer[]{10,20,30,40,50,60};
        status = new OrderStatus[]{PENDING,CONFIRMED,FINISHED,PENDING,CONFIRMED,FINISHED};
        contactEmail = new String[]{"sheyang-li@hotmail.com","sheyang-li@hotmail.com","sheyang-li@hotmail.com",
                "cong.liu@outlook.de","cong.liu@outlook.de","cong.liu@outlook.de"};
        Assert.assertEquals("Array init error", userId.length, itemName.length, itemLink.length);
        Assert.assertEquals("Array init error", amount.length, status.length, contactEmail.length);
    }

    private void assertAllDataInList(List<Order> orders, List<Integer> idx) {
        Assert.assertEquals(idx.size(), orders.size());
        for(int i = 0; i < orders.size() && idx.contains(i); i++) {
            Assert.assertEquals("oder's data incorrect", orders.get(i).getUserId(), userId[i]);
            Assert.assertEquals("oder's data incorrect", orders.get(i).getItemName(), itemName[i]);
            Assert.assertEquals("oder's data incorrect", orders.get(i).getItemLink(), itemLink[i]);
            Assert.assertEquals("oder's data incorrect", orders.get(i).getAmount(), amount[i]);
            Assert.assertEquals("oder's data incorrect", orders.get(i).getOrderStatus(), status[i]);
            Assert.assertEquals("oder's data incorrect", orders.get(i).getContactEmail(), contactEmail[i]);
        }

    }

    private void assertAllDataUser(Order oder) {
        Assert.assertTrue(oder.getOrderId() <= itemName.length);
        int idx = oder.getOrderId() - 1;
        Assert.assertEquals("user's data incorrect", oder.getUserId(), userId[idx]);
        Assert.assertEquals("user's data incorrect", oder.getItemName(), itemName[idx]);
        Assert.assertEquals("user's data incorrect", oder.getItemLink(), itemLink[idx]);
        Assert.assertEquals("user's data incorrect", oder.getAmount(), amount[idx]);
        Assert.assertEquals("user's data incorrect", oder.getOrderStatus(), status[idx]);
        Assert.assertEquals("user's data incorrect", oder.getContactEmail(), contactEmail[idx]);


    }
    @Autowired
    OrderDao orderDao;

    private static final Faker faker = new Faker();

    @Test
    void getUserOrders() {
        List<Order> orders = new ArrayList<>();
        int num = orderDao.getUserOrders(1).size();
        assertEquals(3,num);
    }

    @Test
    void getUserActiveOrders() {
        List<Order> orders = new ArrayList<>();
       int num = orderDao.getUserActiveOrders(1).size();
        assertEquals(2,num);
    }

    @Test
    void getUserPastOrders() {
        int num = orderDao.getUserPastOrders(1).size();
        assertEquals(1,num);
    }

    @Test
    void getAllPastOrders() {
        int num = orderDao.getAllPastOrders().size();
        assertEquals(2,num);
    }

    @Test
    void deleteOrder() {
        int num = 0;
        for(int i = 0; i < status.length; i++) {
            if(!status[i].equals(PENDING) ) {
                 num = i;
                break;
            }
        }
       Assert.assertTrue("fail to delete", 1 == orderDao.deleteOrder(num));
    }

    @Test
    void insertOrder() {
        Order newOrder = DataGenerate.generateOrder();
        System.out.println(newOrder.toString());
        orderDao.insertOrder(newOrder);
        Assert.assertTrue("Fail to insert", 6 + 1 == newOrder.getOrderId());
    }

    @Test
    void getAllActiveOrders() {
        int num = orderDao.getAllActiveOrders().size();
        assertEquals(4,num);
    }

    @Test
    void changeOrderStatus() {
        Order testOrder = orderDao.getOrder(1);
        orderDao.changeOrderStatus(1, CONFIRMED);
        Assert.assertNotEquals(testOrder,orderDao.getOrder(1));
    }

    @Test
    void getOrder() {

      Order testOrder = orderDao.getOrder(2);
        assertAllDataUser(testOrder);

    }
}
