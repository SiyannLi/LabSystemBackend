package com.example.SpringbootTest.service.impl;

import com.example.SpringbootTest.dao.OrderDao;
import com.example.SpringbootTest.entity.Order;
import com.example.SpringbootTest.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;

    @Override
    public List<Order> getUserOrders(int userId) {
        return null;
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
    public Order submitOrder(String article, int amount, String link, String contact) {
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
