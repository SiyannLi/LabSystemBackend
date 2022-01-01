package com.example.LabSystemBackend.service.impl;

import com.example.LabSystemBackend.dao.OrderDao;
import com.example.LabSystemBackend.entity.Order;
import com.example.LabSystemBackend.entity.OrderStatus;
import com.example.LabSystemBackend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;

    @Override
    public List<Order> getUserOrders(int userId) {
        return orderDao.getUserOrders(userId);
    }

    @Override
    public List<Order> getUserActiveOrders(int userId) {
        return orderDao.getUserActiveOrders(userId);
    }

    @Override
    public int deleteOrder(int orderId) {
        return orderDao.deleteOrder(orderId);
    }

    @Override
    public List<Order> getUserPastOrders(int userId) {
        return orderDao.getUserPastOrders(userId);
    }

    @Override
    public int submitOrder(int userId, int itemId, int amount) {
        Order order = new Order();
        order.setUserId(userId);
        order.setItemId(itemId);
        order.setAmount(amount);
        order.setOrderStatus(OrderStatus.PENDING);
        //TODO 添加消息发送功能
        return orderDao.insertOrder(order);
    }

    @Override
    public List<Order> getAllActiveOrders() {
        return orderDao.getAllActiveOrders();
    }

    @Override
    public int confirmOrder(int orderId) {
        return orderDao.changeOrderStatus(orderId, OrderStatus.APPROVED);
    }

    @Override
    public int rejectOrder(int orderId) {
        return orderDao.changeOrderStatus(orderId, OrderStatus.REJECTED);
    }
}
