package com.example.SpringbootTest.controller;

import com.example.SpringbootTest.common.Result;
import com.example.SpringbootTest.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @ApiOperation("get a list of all orders of this user")
    @GetMapping("getUserOrders")
    public Result getUserOrders(int userId){
        return null;
    }

    @ApiOperation("get all active orders of this user")
    @GetMapping("getUserActiveOrders")
    public Result getUserActiveOrders(int userId){
        return null;
    }

    @ApiOperation("delete one order")
    @PostMapping("deleteOrder")
    public Result deleteOrder(int orderId){
        return null;
    }

    @ApiOperation("get all past orders of this user")
    @GetMapping("getUserPastOrders")
    public Result getUserPastOrders(int userId){
        return null;
    }

    @ApiOperation("submit an order with user account")
    @PostMapping("submitOrder")
    public Result submitOrder(int userId, String article, int amount, String link, String contact){
        return null;
    }

    @ApiOperation("get all active orders")
    @GetMapping("getAllActiveOrders")
    public Result getAllActiveOrders(){
        return null;
    }

    @ApiOperation("confirm order application")
    @PostMapping("confirmOrder")
    public Result confirmOrder(int orderId){
        return null;
    }

    @ApiOperation("reject one order application")
    @PostMapping("rejectOrder")
    public Result rejectOrder(int orderId){
        return null;
    }

}
