package com.example.LabSystemBackend.controller;

import com.example.LabSystemBackend.common.Response;
import com.example.LabSystemBackend.common.ResponseGenerator;
import com.example.LabSystemBackend.entity.Item;
import com.example.LabSystemBackend.entity.Order;
import com.example.LabSystemBackend.entity.OrderStatus;
import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.jwt.JwtUtil;
import com.example.LabSystemBackend.jwt.comment.AdminLoginToken;
import com.example.LabSystemBackend.jwt.comment.UserLoginToken;
import com.example.LabSystemBackend.service.ItemService;
import com.example.LabSystemBackend.service.NotificationService;
import com.example.LabSystemBackend.service.OrderService;
import com.example.LabSystemBackend.service.UserService;
import com.example.LabSystemBackend.ui.KeyMessage;
import com.example.LabSystemBackend.ui.NotificationTemplate;
import com.example.LabSystemBackend.ui.OutputMessage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
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

    @UserLoginToken
    @ApiOperation("get a list of all orders of this user")
    @GetMapping("getUserOrders")
    public Response getUserOrders(@RequestHeader(KeyMessage.TOKEN) String token) {
        String email = JwtUtil.getUserInfo(token, KeyMessage.EMAIL);
        User user = userService.getUserByEmail(email);
        return ResponseGenerator.genSuccessResult(UserController.emailTokens.get(email), orderService.getUserOrders(user.getUserId()));

    }

    @UserLoginToken
    @ApiOperation("get all active orders of this user")
    @GetMapping("getUserActiveOrders")
    public Response getUserActiveOrders(@RequestHeader(KeyMessage.TOKEN) String token) {
        String email = JwtUtil.getUserInfo(token, KeyMessage.EMAIL);
        logger.info("email: " + email);
        User user = userService.getUserByEmail(email);
        return ResponseGenerator.genSuccessResult(UserController.emailTokens.get(email), orderService.getUserActiveOrders(user.getUserId()));

    }

    @UserLoginToken
    @ApiOperation("delete one order")
    @PostMapping("deleteOrder")
    public Response deleteOrder(@RequestHeader(KeyMessage.TOKEN) String token,
                                @ApiParam(name = "email", value = "email", required = true)
                                @RequestBody Map<String, String> body) throws MessagingException {
        String email = JwtUtil.getUserInfo(token, KeyMessage.EMAIL);
        int orderId = Integer.parseInt(body.get(KeyMessage.ORDER_ID));
        if (orderService.orderExist(orderId)) {
            Order order = orderService.getOrderById(orderId);
            if (!order.getUserId().equals(userService.getUserByEmail(email).getUserId())) {
                return ResponseGenerator.genFailResult(UserController.emailTokens.get(email)
                        , OutputMessage.ORDER_NOT_EXIST);
            }
            if (!OrderStatus.PENDING.equals(order.getOrderStatus())) {
                return ResponseGenerator.genFailResult(UserController.emailTokens.get(email)
                        , OutputMessage.ORDER_NOT_PENDING);
            }
            orderService.deleteOrder(orderId);
            String contactEmail = order.getContactEmail();
            User user = userService.getUserByEmail(email);
            notificationService.sendNotificationByTemplate(contactEmail
                    , NotificationTemplate.ORDER_CANCELLED, user.getFullName(), order);
            return ResponseGenerator.genSuccessResult(UserController.emailTokens.get(email)
                    , OutputMessage.SUCCEED);
        } else {
            return ResponseGenerator.genFailResult(UserController.emailTokens.get(email)
                    , OutputMessage.ORDER_NOT_EXIST);
        }

    }

    @UserLoginToken
    @ApiOperation("get all past orders of this user")
    @GetMapping("getUserPastOrders")
    public Response getUserPastOrders(@RequestHeader(KeyMessage.TOKEN) String token) {
        String email = JwtUtil.getUserInfo(token, KeyMessage.EMAIL);
        User user = userService.getUserByEmail(email);
        return ResponseGenerator.genSuccessResult(UserController.emailTokens.get(email)
                , orderService.getUserPastOrders(user.getUserId()));

    }

    @UserLoginToken
    @ApiOperation("submit an order with user account")
    @PostMapping("submitOrder")
    public Response submitOrder(@RequestHeader(KeyMessage.TOKEN) String token,
                                @ApiParam(name = "itemInfo", value = "itemInfo", required = true)
                                @RequestBody Map<String, String> body) throws MessagingException {
        int amount = Integer.parseInt(body.get(KeyMessage.AMOUNT));
        String itemName = body.get(KeyMessage.ITEM_NAME);
        String link = body.get(KeyMessage.ITEM_LINK);
        String contactEmail = body.get(KeyMessage.CONTACT_EMAIL);
        String email = JwtUtil.getUserInfo(token, KeyMessage.EMAIL);

        logger.info("amount: " + amount);
        logger.info("itemName: " + itemName);
        logger.info("email: " + email);
        logger.info("link: " + link);
        logger.info("contact: " + contactEmail);
        User user = userService.getUserByEmail(email);
        Order order = new Order();
        order.setUserId(user.getUserId());
        order.setAmount(amount);
        order.setContactEmail(contactEmail);
        order.setItemName(itemName);
        order.setItemLink(link);
        order.setOrderStatus(OrderStatus.PENDING);
        orderService.submitOrder(order);
        notificationService.sendNotificationByTemplate(contactEmail
                , NotificationTemplate.ORDER_RECEIVED, user.getFullName(), order);
        List<User> admins = userService.getAllAdminReceiveBulkEmail();
        for (User admin : admins) {
            String adminEmail = admin.getEmail();
            String adminName = admin.getFullName();
            notificationService.sendNotificationByTemplate(adminEmail, NotificationTemplate.NEW_ORDER_REQUEST
                    , adminName);
        }
        return ResponseGenerator.genSuccessResult(UserController.emailTokens.get(email)
                , OutputMessage.SUCCEED);


    }

    @AdminLoginToken
    @ApiOperation("get all active orders")
    @GetMapping("getAllActiveOrders")
    public Response getAllActiveOrders(@RequestHeader(KeyMessage.TOKEN) String token) {
        String email = JwtUtil.getUserInfo(token, KeyMessage.EMAIL);
        List<Order> activeOrders = orderService.getAllActiveOrders();
        if (activeOrders.isEmpty()) {
            return ResponseGenerator.genSuccessResult(UserController.emailTokens.get(email)
                    , OutputMessage.NO_ACTIVE_ORDERS);
        }
        List<Map<String, String>> ordersInfo = new ArrayList<>();
        logger.info("size: " + activeOrders.size());
        for (Order order : activeOrders) {
            int idx = activeOrders.indexOf(order);
            logger.info("UserId: " + order.getUserId());
            User user = userService.getUser(order.getUserId());
            ordersInfo.add(new HashMap<>());
            ordersInfo.get(idx).put(KeyMessage.USER_EMAIL, user.getEmail());
            ordersInfo.get(idx).put(KeyMessage.USER_NAME, user.getFullName());
            ordersInfo.get(idx).put(KeyMessage.AMOUNT, order.getAmount().toString());
            ordersInfo.get(idx).put(KeyMessage.ITEM_NAME, order.getItemName());
            ordersInfo.get(idx).put(KeyMessage.ORDER_STATUS, order.getOrderStatus().name());
            ordersInfo.get(idx).put(KeyMessage.ITEM_LINK, order.getItemLink());
            ordersInfo.get(idx).put(KeyMessage.ORDER_ID, order.getOrderId().toString());


        }
        return ResponseGenerator.genSuccessResult(UserController.emailTokens.get(email), ordersInfo);

    }

    @AdminLoginToken
    @ApiOperation("get all past orders")
    @GetMapping("getAllPastOrders")
    public Response getAllPastOrders(@RequestHeader(KeyMessage.TOKEN) String token) {
        String email = JwtUtil.getUserInfo(token, KeyMessage.EMAIL);
        List<Order> pastOrders = orderService.getAllPastOrders();
        if (pastOrders.isEmpty()) {
            return ResponseGenerator.genSuccessResult(UserController.emailTokens.get(email)
                    , OutputMessage.NO_PAST_ORDERS);
        }
        List<Map<String, String>> ordersInfo = new ArrayList<>();
        for (Order order : pastOrders) {
            int idx = pastOrders.indexOf(order);
            User user = userService.getUser(order.getUserId());
            ordersInfo.add(new HashMap<>());
            ordersInfo.get(idx).put(KeyMessage.USER_EMAIL, user.getEmail());
            ordersInfo.get(idx).put(KeyMessage.USER_NAME, user.getFullName());
            ordersInfo.get(idx).put(KeyMessage.AMOUNT, order.getAmount().toString());
            ordersInfo.get(idx).put(KeyMessage.ITEM_NAME, order.getItemName());
            ordersInfo.get(idx).put(KeyMessage.ITEM_LINK, order.getItemLink());
            ordersInfo.get(idx).put(KeyMessage.ORDER_ID, order.getOrderId().toString());

        }
        return ResponseGenerator.genSuccessResult(UserController.emailTokens.get(email), ordersInfo);


    }

    @AdminLoginToken
    @ApiOperation("confirm order application")
    @PostMapping("confirmOrder")
    public Response confirmOrder(@RequestHeader(KeyMessage.TOKEN) String token,
                                 @ApiParam(name = "orderId", value = "orderId", required = true)
                                 @RequestBody Map<String, String> body) throws MessagingException {
        String opEmail = JwtUtil.getUserInfo(token, KeyMessage.EMAIL);
        int orderId = Integer.parseInt(body.get(KeyMessage.ORDER_ID));
        if (orderService.orderExist(orderId)) {
            Order order = orderService.getOrderById(orderId);
            if (!order.getOrderStatus().equals(OrderStatus.PENDING)) {
                return ResponseGenerator.genFailResult(UserController.emailTokens.get(opEmail)
                        , OutputMessage.ORDER_NOT_PENDING);
            }
            orderService.confirmOrder(orderId);
            User user = userService.getUser(order.getUserId());
            notificationService.sendNotificationByTemplate(order.getContactEmail()
                    , NotificationTemplate.ORDER_CONFIRMED
                    , user.getFullName(), order);
            return ResponseGenerator.genSuccessResult(UserController.emailTokens.get(opEmail), OutputMessage.SUCCEED);
        }
        return ResponseGenerator.genFailResult(UserController.emailTokens.get(opEmail), OutputMessage.ORDER_NOT_EXIST);
    }


    @AdminLoginToken
    @ApiOperation("reject one order application")
    @PostMapping("rejectOrder")
    public Response rejectOrder(@RequestHeader(KeyMessage.TOKEN) String token,
                                @ApiParam(name = "orderId", value = "orderId", required = true)
                                @RequestBody Map<String, String> body) throws MessagingException {
        String opEmail = JwtUtil.getUserInfo(token, KeyMessage.EMAIL);
        int orderId = Integer.parseInt(body.get(KeyMessage.ORDER_ID));
        if (orderService.orderExist(orderId)) {
            Order order = orderService.getOrderById(orderId);
            if (!order.getOrderStatus().equals(OrderStatus.PENDING)) {
                return ResponseGenerator.genFailResult(UserController.emailTokens.get(opEmail)
                        , OutputMessage.ORDER_NOT_PENDING);
            }
            orderService.rejectOrder(orderId);
            User user = userService.getUser(order.getUserId());
            notificationService.sendNotificationByTemplate(order.getContactEmail()
                    , NotificationTemplate.ORDER_REJECTED
                    , user.getFullName(), opEmail, order);
            return ResponseGenerator.genSuccessResult(UserController.emailTokens.get(opEmail)
                    , OutputMessage.SUCCEED);
        }
        return ResponseGenerator.genFailResult(UserController.emailTokens.get(opEmail)
                , OutputMessage.ORDER_NOT_EXIST);


    }

    @AdminLoginToken
    @ApiOperation("in stock")
    @PostMapping("inStock")
    public Response inStock(@RequestHeader(KeyMessage.TOKEN) String token,
                            @ApiParam(name = "orderId", value = "orderId", required = true)
                            @RequestBody Map<String, String> body) throws MessagingException {
        String opEmail = JwtUtil.getUserInfo(token, KeyMessage.EMAIL);
        int orderId = Integer.parseInt(body.get(KeyMessage.ORDER_ID));
        String itemName = body.get(KeyMessage.ITEM_NAME);
        if (orderService.orderExist(orderId)) {
            Order order = orderService.getOrderById(orderId);
            if (!order.getOrderStatus().equals(OrderStatus.CONFIRMED)) {
                return ResponseGenerator.genFailResult(UserController.emailTokens.get(opEmail)
                        , OutputMessage.NOT_CONFIRMED_ORDER);
            }
            int amount = order.getAmount();
            if (itemService.itemExists(itemName)) {
                Item item = itemService.getItemByName(itemName);
                itemService.changeItemAmount(item.getItemId(), item.getAmount() + amount);
                User user = userService.getUser(order.getUserId());
                notificationService.sendNotificationByTemplate(order.getContactEmail()
                        , NotificationTemplate.ORDER_ARRIVED, user.getFullName(), order);
                orderService.inStock(orderId);
                return ResponseGenerator.genSuccessResult(UserController.emailTokens.get(opEmail)
                        , OutputMessage.SUCCEED);
            } else {
                return ResponseGenerator.genFailResult(UserController.emailTokens.get(opEmail)
                        , OutputMessage.IN_STOCK_ITEM_NAME_NOT_EXIST);
            }


        } else {
            return ResponseGenerator.genFailResult(UserController.emailTokens.get(opEmail)
                    , OutputMessage.ORDER_NOT_EXIST);
        }


    }


}
