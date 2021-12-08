package com.example.LabSystemBackend.service.impl;

import com.example.LabSystemBackend.dao.OrderDao;
import com.example.LabSystemBackend.entity.Order;
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
        return null;
    }

    @Override
    public Order deleteOrder(int orderId) {
        return null;
    }

    @Override
    public List<Order> getUserPastOrders(int userId) {
        return null;
    }

    @Override
    public Order submitOrder(int userId, String item, int amount, String link) {
        return null;
    }

    @Override
    public List<Order> getAllActiveOrders() {
        return null;
    }

    @Override
    public Order confirmOrder(int orderId) {
        return null;
    }

    @Override
    public Order rejectOrder(int orderId) {
        return null;
    }
}
