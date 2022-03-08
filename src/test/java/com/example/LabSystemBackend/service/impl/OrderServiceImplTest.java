package com.example.LabSystemBackend.service.impl;

import com.example.LabSystemBackend.dao.OrderDao;
import com.example.LabSystemBackend.entity.Order;
import com.example.LabSystemBackend.entity.OrderStatus;
import com.example.LabSystemBackend.service.OrderService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@ActiveProfiles("unittest")
@Transactional
@Rollback(value = true)
@RunWith(SpringRunner.class)
@SpringBootTest
class OrderServiceImplTest {
    @Autowired
  private OrderService orderService;
    @MockBean
   private OrderDao orderDao;
   private final List<Order> orders = new ArrayList<>();
 private final List<Order> pastOrders = new ArrayList<>();
   private final Order oder = new Order(1,1,"handy","www.ebay.de",10, OrderStatus.PENDING,
            "testOrder@testorder.com");
 private final Order pastOder = new Order(1,2,"handy","www.ebay.de",10, OrderStatus.FINISHED,
         "testOrder@testorder.com");
   @BeforeEach
   private void setup() {
    orders.add(oder);
    pastOrders.add(pastOder);
   }
    @Test
    void getUserOrders() {

     Mockito.when(orderDao.getUserOrders(Mockito.anyInt())).thenReturn(orders);
     List<Order> testOrders = orderService.getUserOrders(1);
     Assert.assertNotNull(testOrders);
     Assert.assertEquals(orders, testOrders);
    }

    @Test
    void getUserActiveOrders() {
     Mockito.when(orderDao.getUserActiveOrders(Mockito.anyInt())).thenReturn(orders);
     List<Order> testOrders = orderService.getUserActiveOrders(1);
     Assert.assertNotNull(testOrders);
     Assert.assertEquals(orders, testOrders);
    }

    @Test
    void deleteOrder() {
    Mockito.when(orderDao.deleteOrder(Mockito.anyInt())).thenReturn(1);
    int resultTest = orderService.deleteOrder(1);
     Assert.assertNotNull(resultTest);
     Assert.assertEquals(1, resultTest);
    }

    @Test
    void getUserPastOrders() {
     Mockito.when(orderDao.getUserPastOrders(Mockito.anyInt())).thenReturn(pastOrders);
     List<Order> testOrders = orderService.getUserPastOrders(1);
     Assert.assertNotNull(testOrders);
     Assert.assertEquals(pastOrders, testOrders);
    }

    @Test
    void submitOrder() {
    Order randOrder = new Order(1,3,"handy","www.ebay.de",10, OrderStatus.PENDING,
            "testOrder@testorder.com");
     Mockito.when(orderDao.insertOrder(Mockito.any())).thenReturn(2);
     int resultTest = orderService.submitOrder(oder);
     Assert.assertNotNull(resultTest);
     Assert.assertEquals(2, resultTest);
    }

    @Test
    void getAllActiveOrders() {
    Mockito.when(orderDao.getAllActiveOrders()).thenReturn(orders);
    List<Order> resulOrderList = orderService.getAllActiveOrders();
     Assert.assertNotNull(resulOrderList);
     Assert.assertEquals(orders, resulOrderList);
    }

    @Test
    void getAllPastOrders() {
     Mockito.when(orderDao.getAllPastOrders()).thenReturn(pastOrders);
     List<Order> resulOrderList = orderService.getAllPastOrders();
     Assert.assertNotNull(resulOrderList);
     Assert.assertEquals(pastOrders, resulOrderList);
    }

    @Test
    void confirmOrder() {
     Mockito.when(orderDao.changeOrderStatus(1, OrderStatus.CONFIRMED)).thenReturn(2);
     int resultTest = orderService.confirmOrder(1);
     Assert.assertNotNull(resultTest);
     Assert.assertEquals(2, resultTest);
    }

    @Test
    void rejectOrder() {
     Mockito.when(orderDao.deleteOrder(Mockito.anyInt())).thenReturn(2);
     int resultTest = orderService.rejectOrder(1);
     Assert.assertNotNull(resultTest);
     Assert.assertEquals(2, resultTest);
    }

    @Test
    void getOrderById() {
    Mockito.when(orderDao.getOrder(Mockito.anyInt())).thenReturn(oder);
    Order testOrder = orderService.getOrderById(1);
     Assert.assertNotNull(testOrder);
     Assert.assertEquals(oder, testOrder);
     }

    @Test
    void orderExist() {
    Mockito.when(orderDao.getOrder(Mockito.anyInt())).thenReturn(oder);
    Boolean orderExist = orderService.orderExist(5);
    Assert.assertTrue(orderExist);

    }

    @Test
    void inStock() {
     Mockito.when(orderDao.changeOrderStatus(1,OrderStatus.FINISHED)).thenReturn(2);
     int resultTest = orderService.inStock(1);
     Assert.assertNotNull(resultTest);
     Assert.assertEquals(2, resultTest);
    }
}