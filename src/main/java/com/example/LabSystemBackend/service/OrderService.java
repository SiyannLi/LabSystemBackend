package com.example.LabSystemBackend.service;

import com.example.LabSystemBackend.entity.Order;

import java.util.List;

public interface OrderService {

    List<Order> getUserOrders(int userId);


    List<Order> getUserActiveOrders(int userId);


    int deleteOrder(int orderId);


    List<Order> getUserPastOrders(int userId);


    int submitOrder(Order order);


    List<Order> getAllActiveOrders();

    List<Order> getAllPastOrders();


    int confirmOrder(int orderId);


    int rejectOrder(int orderId);

    Order getOrderById(int orderId);

    boolean orderExist(int orderId);

    int inStock(int orderId);
}
