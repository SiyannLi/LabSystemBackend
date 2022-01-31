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
    public int submitOrder(Order order) {
        return orderDao.insertOrder(order);
    }

    @Override
    public List<Order> getAllActiveOrders() {
        return orderDao.getAllActiveOrders();
    }

    @Override
    public List<Order> getAllPastOrders() {
        return orderDao.getAllPastOrders();
    }

    @Override
    public int confirmOrder(int orderId) {
        return orderDao.changeOrderStatus(orderId, OrderStatus.CONFIRMED);
    }

    @Override
    public int rejectOrder(int orderId) {
        return orderDao.deleteOrder(orderId);
    }

    @Override
    public Order getOrderById(int orderId) {
        return orderDao.getOrder(orderId);
    }

    @Override
    public boolean orderExist(int orderId) {
        return orderDao.getOrder(orderId) != null;
    }

    @Override
    public int inStock(int orderId) {
        return orderDao.changeOrderStatus(orderId, OrderStatus.FINISHED);
    }
}
