package com.example.SpringbootTest.dao;

import com.example.SpringbootTest.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderDao {
    List<Order> getUserOrders(@Param("userId") int userId);

    List<Order> getUserActiveOrders(@Param("userId") int userId);

    List<Order> getUserPastOrders(@Param("userId") int userId);

    Order deleteOrder(@Param("orderId") int orderId);

    Order submitOrder(@Param("order") Order order);

    List<Order> getAllActiveOrders();

    int changeOrderStatus();

}
