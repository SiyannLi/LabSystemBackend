package com.example.LabSystemBackend.controller;

import com.example.LabSystemBackend.entity.*;
import com.example.LabSystemBackend.jwt.AuthenticationInterceptor;
import com.example.LabSystemBackend.jwt.JwtUtil;
import com.example.LabSystemBackend.service.ItemService;
import com.example.LabSystemBackend.service.NotificationService;
import com.example.LabSystemBackend.service.OrderService;
import com.example.LabSystemBackend.service.UserService;
import com.example.LabSystemBackend.ui.KeyMessage;
import com.example.LabSystemBackend.ui.NotificationTemplate;
import com.example.LabSystemBackend.ui.OutputMessage;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * @version 1.0
 * @author  Sheyang Li
 *
 * OrderController Test
 */

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    AuthenticationInterceptor interceptor;
    @Mock
    UserController userController;
    @MockBean
    OrderService orderService;
    @MockBean
    UserService userService;
    @MockBean
    NotificationService notificationService;
    @MockBean
    ItemService itemService;

    // set Test Data
    private String token;
    private String email;
    //user Data
    User user = new User(39,"firstName","lastName","test_user@testpse.com",
            UserRole.ADMIN,"a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3",
            UserAccountStatus.ACTIVE,true);
    //order data
    Order oder1 = new Order(39,1,"Laptop","www.ebay.de",
            20, OrderStatus.PENDING,"test@testpse.com");
    Order oder2 = new Order(39,2,"Laptop","www.ebay.de",
            20, OrderStatus.FINISHED,"test@testpse.com");
    Order oder3 = new Order(1000,3,"Laptop","www.ebay.de",
            20, OrderStatus.FINISHED,"test@testpse.com");
    Order confirmedOder = new Order(39,4,"Laptop","www.ebay.de",
            20, OrderStatus.CONFIRMED,"test@testpse.com");
    List<Order> orders = new ArrayList<>();
    List<Order> ordersFinished = new ArrayList<>();
    List<Order> orders1 = new ArrayList<>();

    Item item = new Item(1,"Laptop",10,"Huawei");

    @BeforeEach
    private void login() {

        token = JwtUtil.createToken(user);
        email = user.getEmail();
        orders.add(oder1);
        orders1.add(oder3);
        ordersFinished.add(oder2);
    }

    @AfterEach
    private void reset(){
        orders.remove(oder1);
        orders1.remove(oder3);
        ordersFinished.remove(oder2);
    }

    @Test
    void getUserOrders() throws Exception {

        when(interceptor.preHandle(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class),
                Mockito.any(Object.class))).thenReturn(true);
        when(orderService.getUserOrders(Mockito.anyInt())).thenReturn(orders);
        when(userService.getUserByEmail(anyString())).thenReturn(user);

        String url = "/orders/getUserOrders";

        mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.resultCode").value(200))

                .andReturn();

    }

    @Test
    void getUserActiveOrders() throws Exception {
        when(interceptor.preHandle(Mockito.any(HttpServletRequest.class),Mockito.any(HttpServletResponse.class),
                Mockito.any(Object.class))).thenReturn(true);
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(orderService.getUserActiveOrders(user.getUserId())).thenReturn(orders);

        String url= "/orders/getUserActiveOrders";
        mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization",token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.resultCode").value(200))
                .andExpect(jsonPath("$..orderId").value(1))
                .andReturn();
    }

    @Test
    void deleteOrderFailnoExist() throws Exception {
        String url = "/orders/deleteOrder";
        String body = "{\"orderId\":\"1\"}";
        when(orderService.orderExist(Mockito.anyInt())).thenReturn(false);
        try {
            when(interceptor.preHandle(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class),
                    Mockito.any(Object.class))).thenReturn(true);
            mockMvc.perform(MockMvcRequestBuilders
                            .post(url)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(body)
                            .header("Authorization", token))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(jsonPath("$.resultCode").value(500))
                    .andExpect(jsonPath("$..message").value("This order does not exist"))
                    .andReturn();
        } catch (Exception e) {
            Assert.fail("fail to request");
        }
    }

    @Test
    void deleteOrderFailnoPending() throws Exception {
        int isDelete = 0;
        String url = "/orders/deleteOrder";
        String body = "{\"orderId\":\"2\"}";
        when(orderService.orderExist(Mockito.anyInt())).thenReturn(true);
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(orderService.getOrderById(Mockito.anyInt())).thenReturn(ordersFinished.get(0));

        try {
            when(interceptor.preHandle(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class),
                    Mockito.any(Object.class))).thenReturn(true);
            mockMvc.perform(MockMvcRequestBuilders
                            .post(url)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(body)
                            .header("Authorization", token))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(jsonPath("$.resultCode").value(500))
                    .andExpect(jsonPath("$..message").value("This order has been confirmed or the order has ended"))
                    .andReturn();
        } catch (Exception e) {
            Assert.fail("fail to request");
        }
    }

    @Test
    void deleteOrderFailnosameUser() throws Exception {
        int isDelete = 0;
        String url = "/orders/deleteOrder";
        String body = "{\"orderId\":\"2\"}";
        when(orderService.orderExist(Mockito.anyInt())).thenReturn(true);
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(orderService.getOrderById(Mockito.anyInt())).thenReturn(orders1.get(0));

        try {
            when(interceptor.preHandle(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class),
                    Mockito.any(Object.class))).thenReturn(true);
            mockMvc.perform(MockMvcRequestBuilders
                            .post(url)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(body)
                            .header("Authorization", token))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(jsonPath("$.resultCode").value(500))
                    .andExpect(jsonPath("$..message").value("This order does not exist"))
                    .andReturn();
        } catch (Exception e) {
            Assert.fail("fail to request");
        }
    }

    @Test
    void deleteOrder() throws Exception {
        String url= "/orders/deleteOrder";String body = "{\"orderId\":\"1\"}";
        when(orderService.orderExist(Mockito.anyInt())).thenReturn(true);
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(orderService.getOrderById(Mockito.anyInt())).thenReturn(orders.get(0));
        when(notificationService.sendNotificationByTemplate(oder1.getContactEmail(), NotificationTemplate.ORDER_CANCELLED,
                user.getFullName(), orders.get(0))).thenReturn(1);
        when(orderService.deleteOrder(Mockito.anyInt())).thenReturn(1);

        try {
            when(interceptor.preHandle(Mockito.any(HttpServletRequest.class),Mockito.any(HttpServletResponse.class),
                    Mockito.any(Object.class))).thenReturn(true);
            mockMvc.perform(MockMvcRequestBuilders
                            .post(url)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(body)
                            .header("Authorization",token))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(jsonPath("$.resultCode").value(200))
                    .andExpect(jsonPath("$..message").value("succeed"))
                    .andReturn();
            Mockito.verify(notificationService).sendNotificationByTemplate(oder1.getContactEmail(),
                    NotificationTemplate.ORDER_CANCELLED,
                    user.getFullName(), orders.get(0));
            Mockito.verify(orderService).deleteOrder(Mockito.anyInt());
        } catch (Exception e) {
            Assert.fail("fail to request");
        }
    }

    @Test
    void getUserPastOrders() throws Exception {

        when(interceptor.preHandle(Mockito.any(HttpServletRequest.class),Mockito.any(HttpServletResponse.class),
                Mockito.any(Object.class))).thenReturn(true);
        when(orderService.getUserPastOrders(Mockito.anyInt())).thenReturn(ordersFinished);
        when(userService.getUserByEmail(anyString())).thenReturn(user);

        String url= "/orders/getUserPastOrders";

        mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization",token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.resultCode").value(200))
                .andReturn();
        Mockito.verify(orderService).getUserPastOrders(user.getUserId());
    }

    @Test
    void submitOrder() throws MessagingException {
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(orderService.submitOrder(Mockito.any())).thenReturn(1);
        when(notificationService.sendNotificationByTemplate(orders.get(0).getContactEmail()
                , NotificationTemplate.ORDER_RECEIVED, user.getFullName(), orders.get(0))).thenReturn(1);
        when(notificationService.sendToAllAdmin(NotificationTemplate.NEW_ORDER_REQUEST)).thenReturn(1);

        String url = "/orders/submitOrder";
        String body = "{\""+KeyMessage.AMOUNT+"\":\"20\",\"" + KeyMessage.EMAIL + "\":\"handy\"," +
                "\""+KeyMessage.ITEM_LINK+"\":\"www.ebay.de\",\""+KeyMessage.CONTACT_EMAIL+"\":\"test@testpse.com\"}";
        try {
            when(interceptor.preHandle(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class),
                    Mockito.any(Object.class))).thenReturn(true);
            mockMvc.perform(MockMvcRequestBuilders
                            .post(url)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(body)
                            .header("Authorization", token))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(jsonPath("$.resultCode").value(200))
                    .andExpect(jsonPath("$..message").value("succeed"))
                    .andReturn();
            Mockito.verify(orderService).submitOrder(Mockito.any());
            Mockito.verify(notificationService).sendToAllAdmin(NotificationTemplate.NEW_ORDER_REQUEST);

        } catch (Exception e) {
            Assert.fail("fail to request");
        }
    }

    private void getAllActiveOrders(Boolean noEmpty, int resultCode, String message) {
        List<Order> emptyOrders = new ArrayList<>();
        String url = "/orders/getAllActiveOrders";
        String body = "{}";
        if (noEmpty){
            when(orderService.getAllActiveOrders()).thenReturn(orders);
        }
        else {
            when(orderService.getAllActiveOrders()).thenReturn(emptyOrders);
        }
        when(userService.getUser(anyInt())).thenReturn(user);
        try {
            when(interceptor.preHandle(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class),
                    Mockito.any(Object.class))).thenReturn(true);
            mockMvc.perform(MockMvcRequestBuilders
                            .get(url)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization",token)
                            .content(body))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(jsonPath("$.resultCode").value(resultCode))
                    .andExpect(jsonPath("$..message").value(message))
                    .andReturn();
        }  catch (Exception e) {
            Assert.fail("fail to request");
        }
    }

    @Test
    void getAllActiveOrdersSucceed(){
        getAllActiveOrders(true,200, "SUCCESS");
    }

    @Test
    void getAllActiveOrdersFail(){
        getAllActiveOrders(false, 200, OutputMessage.NO_ACTIVE_ORDERS);
    }


    private void getAllPastOrders(Boolean noEmpty, int resultCode, String message) {

        List<Order> emptyOrders = new ArrayList<>();
        String url = "/orders/getAllPastOrders";
        String body = "{}";
        if (noEmpty){
            when( orderService.getAllPastOrders()).thenReturn(ordersFinished);
        }
        else {
            when(orderService.getAllActiveOrders()).thenReturn(emptyOrders);
        }
        when(userService.getUser(anyInt())).thenReturn(user);
        try {
            when(interceptor.preHandle(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class),
                    Mockito.any(Object.class))).thenReturn(true);
            mockMvc.perform(MockMvcRequestBuilders
                            .get(url)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization",token)
                            .content(body))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(jsonPath("$.resultCode").value(resultCode))
                    .andExpect(jsonPath("$..message").value(message))
                    .andReturn();
        }  catch (Exception e) {
            Assert.fail("fail to request");
        }
    }

    @Test
    void getAllPastOrdersSucceed(){
        getAllPastOrders(true,200, "SUCCESS");
    }

    @Test
    void getAllPastOrdersFail(){
        getAllPastOrders(false, 200, OutputMessage.NO_PAST_ORDERS);
    }


    private void confirmOrder(int orderId, Boolean orderExist, Boolean isPending ,int resultCode
            , String message ) throws MessagingException {

        String url = "/orders/confirmOrder";
        String body = "{\""+KeyMessage.ORDER_ID+"\":\""+orderId+"\"}";
        when(userService.getUser(anyInt())).thenReturn(user);
        when(orderService.orderExist(anyInt())).thenReturn(orderExist);
        if(isPending) {
            when(orderService.getOrderById(anyInt())).thenReturn(oder1);
        }
        else {when(orderService.getOrderById(anyInt())).thenReturn(oder2);
        }
        when(orderService.confirmOrder(anyInt())).thenReturn(1);
        when(notificationService.sendNotificationByTemplate(email
                , NotificationTemplate.ORDER_CONFIRMED
                , user.getFullName(), oder1)).thenReturn(1);

        try {
            when(interceptor.preHandle(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class),
                    Mockito.any(Object.class))).thenReturn(true);
            mockMvc.perform(MockMvcRequestBuilders
                            .post(url)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(body)
                            .header("Authorization", token))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(jsonPath("$.resultCode").value(resultCode))
                    .andExpect(jsonPath("$..message").value(message))
                    .andReturn();
        } catch (Exception e) {
            Assert.fail("fail to request");
        }

    }

    @Test
    void confirmOrderSucceed() throws MessagingException {
        confirmOrder(1,true,true,200, OutputMessage.SUCCEED);
    }

    @Test
    void confirmOrderOrderFail() throws MessagingException {
        confirmOrder(5,false,true,500, OutputMessage.ORDER_NOT_EXIST);
    }

    @Test
    void confirmOrderPendingFail() throws MessagingException {
        confirmOrder(2,true,false,500, OutputMessage.ORDER_NOT_PENDING);
    }


    private void rejectOrder(int orderId, Boolean orderExist, Boolean isPending ,int resultCode
            , String message) throws MessagingException {

        String url = "/orders/rejectOrder";
        String body = "{\""+KeyMessage.ORDER_ID+"\":\""+orderId+"\"}";

        when(userService.getUser(anyInt())).thenReturn(user);
        when(orderService.orderExist(anyInt())).thenReturn(orderExist);
        if(isPending) {
            when(orderService.getOrderById(anyInt())).thenReturn(oder1);
        }
        else {when(orderService.getOrderById(anyInt())).thenReturn(oder2);
        }
        when(orderService.rejectOrder(1)).thenReturn(1);
        when(notificationService.sendNotificationByTemplate(email
                , NotificationTemplate.ORDER_REJECTED
                , user.getFullName(), email, oder1)).thenReturn(1);
        try {
            when(interceptor.preHandle(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class),
                    Mockito.any(Object.class))).thenReturn(true);
            mockMvc.perform(MockMvcRequestBuilders
                            .post(url)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(body)
                            .header("Authorization", token))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(jsonPath("$.resultCode").value(resultCode))
                    .andExpect(jsonPath("$..message").value(message))
                    .andReturn();
        } catch (Exception e) {
            Assert.fail("fail to request");
        }
    }

    @Test
    void rejectOrderSucceed() throws MessagingException {
        rejectOrder(1,true,true,200, OutputMessage.SUCCEED);
    }

    @Test
    void rejectOrderOrderFail() throws MessagingException {
        rejectOrder(5,false,true,500, OutputMessage.ORDER_NOT_EXIST);
    }

    @Test
    void rejectOrderPendingFail() throws MessagingException {
        rejectOrder(2,true,false,500, OutputMessage.ORDER_NOT_PENDING);
    }

    void inStock (int orderId, Boolean orderExist, Boolean isConfirmed, Boolean itemExist,
                  int resultCode, String message) throws MessagingException {
        String url = "/orders/inStock";
        String body = "{\""+KeyMessage.ORDER_ID+"\":\""+orderId+"\",\""+KeyMessage.ITEM_NAME+"\":\""+item.getItemName()+"\"}";
        when(userService.getUser(anyInt())).thenReturn(user);
        when(orderService.orderExist(anyInt())).thenReturn(orderExist);
        if(isConfirmed) {
            when(orderService.getOrderById(anyInt())).thenReturn(confirmedOder);
        }
        else {when(orderService.getOrderById(anyInt())).thenReturn(oder1);
        }
        when(itemService.getItemByName(anyString())).thenReturn(item);
        when(itemService.changeItemAmount(1, 10 + 20)).thenReturn(30);
        when(notificationService.sendNotificationByTemplate(confirmedOder.getContactEmail()
                , NotificationTemplate.ORDER_ARRIVED, user.getFullName(), confirmedOder)).thenReturn(1);
        when(orderService.inStock(orderId)).thenReturn(1);
        when(itemService.itemExists(anyString())).thenReturn(itemExist);

        try {
            when(interceptor.preHandle(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class),
                    Mockito.any(Object.class))).thenReturn(true);
            mockMvc.perform(MockMvcRequestBuilders
                            .post(url)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(body)
                            .header("Authorization", token))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(jsonPath("$.resultCode").value(resultCode))
                    .andExpect(jsonPath("$..message").value(message))
                    .andReturn();
        } catch (Exception e) {
            Assert.fail("fail to request");
        }

    }
    @Test
    void inStockSucceed() throws MessagingException {
        inStock(4,true,true,true,200, OutputMessage.SUCCEED );
    }
    @Test
    void inStocknotOrderFail() throws MessagingException {
        inStock(5,false,true,true,500, OutputMessage.ORDER_NOT_EXIST );
    }

    @Test
    void inStocknotConfirmedFail() throws MessagingException {
        inStock(2,true,false,false,500, OutputMessage.NOT_CONFIRMED_ORDER );
    }

    @Test
    void inStocknoItemFail() throws MessagingException {
        inStock(3,true,true,false,500, OutputMessage.IN_STOCK_ITEM_NAME_NOT_EXIST );
    }
}