package com.example.LabSystemBackend.dao;

import com.example.LabSystemBackend.entity.Order;
import com.example.LabSystemBackend.entity.OrderStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version 1.0
 * @author Cong Liu, Siyan Li
 *
 * Order Dao
 */
@Mapper
public interface OrderDao {
    List<Order> getUserOrders(@Param("userId") int userId);

    List<Order> getUserActiveOrders(@Param("userId") int userId);

    List<Order> getUserPastOrders(@Param("userId") int userId);

    List<Order> getAllPastOrders();

    int deleteOrder(@Param("orderId") int orderId);

    int insertOrder(@Param("order") Order order);

    List<Order> getAllActiveOrders();

    int changeOrderStatus(@Param("orderId") int orderId, @Param("orderStatus") OrderStatus orderStatus);

    Order getOrder(@Param("orderId") int orderId);
}
