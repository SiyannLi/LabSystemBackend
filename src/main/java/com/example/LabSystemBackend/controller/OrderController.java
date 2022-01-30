package com.example.LabSystemBackend.controller;

import com.example.LabSystemBackend.common.Response;
import com.example.LabSystemBackend.common.ResponseGenerator;
import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.service.OrderService;
import com.example.LabSystemBackend.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @ApiOperation("get a list of all orders of this user")
    @GetMapping("getUserOrders")
    public Response getUserOrders(int userId){
        return ResponseGenerator.genSuccessResult(orderService.getUserOrders(userId));
    }

    @ApiOperation("get all active orders of this user")
    @GetMapping("getUserActiveOrders")
    public Response getUserActiveOrders(int userId){
        return ResponseGenerator.genSuccessResult(orderService.getUserActiveOrders(userId));
    }

    @ApiOperation("delete one order")
    @PostMapping("deleteOrder")
    public Response deleteOrder(int orderId){
        return ResponseGenerator.genSuccessResult(orderService.deleteOrder(orderId));
    }

    @ApiOperation("get all past orders of this user")
    @GetMapping("getUserPastOrders")
    public Response getUserPastOrders(int userId){
        return ResponseGenerator.genSuccessResult(orderService.getUserPastOrders(userId));
    }

    @ApiOperation("submit an order with user account")
    @PostMapping("submitOrder")
    public Response submitOrder(@ApiParam(name = "email", value = "email", required = true)
    @RequestBody Map<String, String> body) throws MessagingException {

        String email = body.get("email");
        int itemId = Integer.parseInt( body.get("itemId"));
        int amount = Integer.parseInt(body.get("amount"));
        User user = userService.getUserByEmail(email);

        return ResponseGenerator.genSuccessResult(orderService.submitOrder(user.getUserId(),itemId,amount));
    }

    @ApiOperation("get all active orders")
    @GetMapping("getAllActiveOrders")
    public Response getAllActiveOrders(){
        return ResponseGenerator.genSuccessResult(orderService.getAllActiveOrders());

    }

    @ApiOperation("confirm order application")
    @PostMapping("confirmOrder")
    public Response confirmOrder(int orderId){
        return ResponseGenerator.genSuccessResult(orderService.confirmOrder(orderId));
    }

    @ApiOperation("reject one order application")
    @PostMapping("rejectOrder")
    public Response rejectOrder(int orderId){
        return ResponseGenerator.genSuccessResult(orderService.rejectOrder(orderId));

    }

}
