package com.example.LabSystemBackend.dao;

import com.example.LabSystemBackend.entity.Order;
import com.example.LabSystemBackend.entity.OrderStatus;
import com.example.LabSystemBackend.util.DataGenerate;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderDaoTest {

    @Autowired
    OrderDao orderDao;
    private static final Logger logger = LoggerFactory.getLogger(OrderDaoTest.class);

    @Test
    void getUserOrders() {
        int userId = orderDao.getLastOrder().getUserId();
        List<Order> orders = orderDao.getUserOrders(userId);
        for (Order order : orders) {
            assertEquals(userId, order.getUserId());
        }
    }

    @Test
    void getUserActiveOrders() {
        int userId = orderDao.getLastOrder().getUserId();
        List<Order> orders = orderDao.getUserActiveOrders(userId);
        for (Order order : orders) {
            assertEquals(userId, order.getUserId());
            logger.info(order.getOrderStatus().toString());
            assertTrue(OrderStatus.APPROVED == order.getOrderStatus()
                    || OrderStatus.PENDING == order.getOrderStatus());
        }
    }


    @Test
    void deleteOrder() {
        Order order = DataGenerate.generateOrder();
        orderDao.insertOrder(order);
        orderDao.deleteOrder(order.getOrderId());
        Order get = orderDao.getOrder(order.getOrderId());
        assertNull(get);
    }

    @Test
    void insertOrder() {
        Order order = DataGenerate.generateOrder();
        orderDao.insertOrder(order);
        assertNotNull(order.getOrderId());
    }

    @Test
    void getAllActiveOrders() {
        List<Order> orders = orderDao.getAllActiveOrders();
        for (Order order : orders) {
            assertTrue(OrderStatus.APPROVED == order.getOrderStatus()
                    || OrderStatus.PENDING == order.getOrderStatus());
        }
    }

    @Test
    void changeOrderStatus() {
        Order order = DataGenerate.generateOrder();
        orderDao.insertOrder(order);
        orderDao.changeOrderStatus(order.getOrderId(), OrderStatus.REJECTED);
        Order get = orderDao.getOrder(order.getOrderId());
        assertEquals(OrderStatus.REJECTED, get.getOrderStatus());
    }

//    @Test
//    void addFakeData() {
//        for (int i = 0; i < 10; i++) {
//            Order order = DataGenerate.generateOrder();
//            orderDao.insertOrder(order);
//        }
//
//    }

    @Test
    void getOrder() {
        Order order = DataGenerate.generateOrder();
        orderDao.insertOrder(order);
        Order get = orderDao.getOrder(order.getOrderId());
        assertEquals(order, get);
    }
}