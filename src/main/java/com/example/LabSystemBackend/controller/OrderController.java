package com.example.LabSystemBackend.controller;

import com.example.LabSystemBackend.common.Response;
import com.example.LabSystemBackend.common.ResponseGenerator;
import com.example.LabSystemBackend.entity.Item;
import com.example.LabSystemBackend.entity.Order;
import com.example.LabSystemBackend.entity.OrderStatus;
import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.jwt.JwtUtil;
import com.example.LabSystemBackend.service.ItemService;
import com.example.LabSystemBackend.service.NotificationService;
import com.example.LabSystemBackend.service.OrderService;
import com.example.LabSystemBackend.service.UserService;
import com.example.LabSystemBackend.ui.NotificationTemplate;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/orders")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private OrderService orderService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;

    @ApiOperation("get a list of all orders of this user")
    @PostMapping("getUserOrders")
    public Response getUserOrders(/*@RequestHeader("Authorization") String token*/
            @ApiParam(name = "email", value = "email", required = true)
            @RequestBody Map<String, String> body) {
        String email = body.get("email");
//        String tokenServer = UserController.emailTokens.get(email);
//        if (token == null) {
//            if (tokenServer == null) {
//                return ResponseGenerator.genFailResult("not logged in");
//            } else {
//                emailTokens.remove(email);
//                emailVerifyCodes.remove(email);
//                return ResponseGenerator.genFailResult("wrong token");
//            }
//        }
//        if (!token.equals(tokenServer) || !JwtUtil.verify(token)) {
//            emailTokens.remove(email);
//            emailVerifyCodes.remove(email);
//            return ResponseGenerator.genFailResult("wrong token");
//        }
        //String email = JwtUtil.getUserInfo(token, "email");
        if (userService.emailExists(email)) {
            User user = userService.getUserByEmail(email);
            return ResponseGenerator.genSuccessResult(orderService.getUserOrders(user.getUserId()));
        } else {
            return ResponseGenerator.genFailResult("User does not exist");
        }
    }

    @ApiOperation("get all active orders of this user")
    @PostMapping("getUserActiveOrders")
    public Response getUserActiveOrders(/*@RequestHeader("Authorization") String token*/
            @ApiParam(name = "email", value = "email", required = true)
            @RequestBody Map<String, String> body) {
        //String email = JwtUtil.getUserInfo(token, "email");
        String email = body.get("email");
        logger.info("email: " + email);
        if (userService.emailExists(email)) {
            User user = userService.getUserByEmail(email);
            return ResponseGenerator.genSuccessResult(orderService.getUserActiveOrders(user.getUserId()));
        } else {
            return ResponseGenerator.genFailResult("User does not exist");
        }
    }

    @ApiOperation("delete one order")
    @PostMapping("deleteOrder")
    public Response deleteOrder(/*@RequestHeader("Authorization") String token,*/
            @ApiParam(name = "email", value = "email", required = true)
            @RequestBody Map<String, String> body) {
        String orderId = body.get("orderId");
        return ResponseGenerator.genSuccessResult(orderService.deleteOrder(Integer.parseInt(orderId)));
    }

    @ApiOperation("get all past orders of this user")
    @PostMapping("getUserPastOrders")
    public Response getUserPastOrders(/*@RequestHeader("Authorization") String token*/
            @ApiParam(name = "email", value = "email", required = true)
            @RequestBody Map<String, String> body) {
        //String email = JwtUtil.getUserInfo(token, "email");
        String email = body.get("email");
        if (userService.emailExists(email)) {
            User user = userService.getUserByEmail(email);
            return ResponseGenerator.genSuccessResult(orderService.getUserPastOrders(user.getUserId()));
        } else {
            return ResponseGenerator.genFailResult("User does not exist");
        }
    }

    @ApiOperation("submit an order with user account")
    @PostMapping("submitOrder")
    public Response submitOrder(/*@RequestHeader("Authorization") String token,*/
            @ApiParam(name = "itemInfo", value = "itemInfo", required = true)
            @RequestBody Map<String, String> body) {
        int amount = Integer.parseInt(body.get("amount"));
        String itemName = body.get("itemName");
        String email = body.get("email");
        String link = body.get("itemLink");
        String contactEmail = body.get("contactEmail");
        //String email = JwtUtil.getUserInfo(token, "email");

        logger.info("amount: " + amount);
        logger.info("itemName: " + itemName);
        logger.info("email: " + email);
        logger.info("link: " + link);
        logger.info("contact: " + contactEmail);

        if (userService.emailExists(email)) {
            User user = userService.getUserByEmail(email);
            Order order = new Order();
            order.setUserId(user.getUserId());
            order.setAmount(amount);
            order.setContactEmail(contactEmail);
            order.setItemName(itemName);
            order.setItemLink(link);
            order.setOrderStatus(OrderStatus.PENDING);
            orderService.submitOrder(order);
            notificationService.sendNotificationByTemplateWithOrder(contactEmail
                    , NotificationTemplate.ORDER_CONFIRMING, user.getFullName(), order.getOrderId());
            return ResponseGenerator.genSuccessResult(/*token,*/ "SUCCESS");

        } else {
            return ResponseGenerator.genFailResult(/*token,*/ "User does not exist");
        }
    }

    @ApiOperation("get all active orders")
    @GetMapping("getAllActiveOrders")
    public Response getAllActiveOrders() {
        List<Order> activeOrders = orderService.getAllActiveOrders();
        if (activeOrders.isEmpty()) {
            return ResponseGenerator.genSuccessResult("no active order");
        }
        List<Map<String, String>> ordersInfo = new ArrayList<>();
        logger.info("size: " + activeOrders.size());
        for (Order order : activeOrders) {
            int idx = activeOrders.indexOf(order);
            User user = userService.getUser(order.getUserId());
            ordersInfo.add(new HashMap<>());
            ordersInfo.get(idx).put("userEmail", user.getEmail());
            ordersInfo.get(idx).put("userName", user.getFullName());
            ordersInfo.get(idx).put("amount", order.getAmount().toString());
            ordersInfo.get(idx).put("itemName", order.getItemName());
            ordersInfo.get(idx).put("orderStatus", order.getOrderStatus().name());
            ordersInfo.get(idx).put("itemLink", order.getItemLink());
            ordersInfo.get(idx).put("orderId", order.getOrderId().toString());


        }
        return ResponseGenerator.genSuccessResult(ordersInfo);

    }

    @ApiOperation("get all past orders")
    @GetMapping("getAllPastOrders")
    public Response getAllPastOrders() {
        List<Order> pastOrders = orderService.getAllPastOrders();
        if (pastOrders.isEmpty()) {
            return ResponseGenerator.genSuccessResult("no past order");
        }
        List<Map<String, String>> ordersInfo = new ArrayList<>();
        for (Order order : pastOrders) {
            int idx = pastOrders.indexOf(order);
            User user = userService.getUser(order.getUserId());
            ordersInfo.add(new HashMap<>());
            ordersInfo.get(idx).put("userEmail", user.getEmail());
            ordersInfo.get(idx).put("userName", user.getFullName());
            ordersInfo.get(idx).put("amount", order.getAmount().toString());
            ordersInfo.get(idx).put("itemName", order.getItemName());
            ordersInfo.get(idx).put("itemLink", order.getItemLink());
            ordersInfo.get(idx).put("orderId", order.getOrderId().toString());

        }
        return ResponseGenerator.genSuccessResult(ordersInfo);


    }

    @ApiOperation("confirm order application")
    @PostMapping("confirmOrder")
    public Response confirmOrder(@ApiParam(name = "orderId", value = "orderId", required = true)
                                 @RequestBody Map<String, String> body) {
        int orderId = Integer.parseInt(body.get("orderId"));
        if (orderService.orderExist(orderId)) {
            Order order = orderService.getOrderById(orderId);
            if (!order.getOrderStatus().equals(OrderStatus.PENDING)) {
                return ResponseGenerator.genFailResult("This order cannot be confirmed");
            }
            User user = userService.getUser(order.getUserId());
            notificationService.sendNotificationByTemplateWithOrder(order.getContactEmail()
                    , NotificationTemplate.ORDER_CONFIRMED
                    , user.getFullName(), orderId);
            return ResponseGenerator.genSuccessResult(orderService.confirmOrder(orderId));
        }
        return ResponseGenerator.genFailResult("Order does not exist");
    }

    @ApiOperation("reject one order application")
    @PostMapping("rejectOrder")
    public Response rejectOrder(@ApiParam(name = "orderId", value = "orderId", required = true)
                                @RequestBody Map<String, String> body) {
        int orderId = Integer.parseInt(body.get("orderId"));
        if (orderService.orderExist(orderId)) {
            Order order = orderService.getOrderById(orderId);
            if (!order.getOrderStatus().equals(OrderStatus.PENDING)) {
                return ResponseGenerator.genFailResult("This order cannot be rejected");
            }
            User user = userService.getUser(order.getUserId());
            notificationService.sendNotificationByTemplateWithOrder(order.getContactEmail()
                    , NotificationTemplate.ORDER_REJECTED
                    , user.getFullName(), orderId);
            return ResponseGenerator.genSuccessResult(orderService.rejectOrder(orderId));
        }
        return ResponseGenerator.genFailResult("Order does not exist");


    }

    @ApiOperation("reject one order application")
    @PostMapping("inStock")
    public Response inStock(@ApiParam(name = "orderId", value = "orderId", required = true)
                            @RequestBody Map<String, String> body) {
        int orderId = Integer.parseInt(body.get("orderId"));
        String itemName = body.get("itemName");
        if (orderService.orderExist(orderId)) {
            Order order = orderService.getOrderById(orderId);
            if (!order.getOrderStatus().equals(OrderStatus.CONFIRMED)) {
                return ResponseGenerator.genFailResult("Order status error");
            }
            int amount = order.getAmount();
            User user = userService.getUser(order.getUserId());
            notificationService.sendNotificationByTemplateWithOrder(order.getContactEmail()
                    , NotificationTemplate.IN_STOCK, user.getFullName(), orderId);
            if (itemService.itemExists(itemName)) {
                Item item = itemService.getItemByName(itemName);
                itemService.changeItemAmount(item.getItemId(), item.getAmount() + amount);
                return ResponseGenerator.genSuccessResult(orderService.inStock(orderId));
            } else {
                return ResponseGenerator.genFailResult("Item Name does not exist, lease create this item first.");
            }

        } else {
            return ResponseGenerator.genFailResult("Order does not exist");
        }


    }


}
