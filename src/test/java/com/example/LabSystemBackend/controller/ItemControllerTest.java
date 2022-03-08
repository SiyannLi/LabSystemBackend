package com.example.LabSystemBackend.controller;

import com.example.LabSystemBackend.entity.Item;
import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.entity.UserAccountStatus;
import com.example.LabSystemBackend.entity.UserRole;
import com.example.LabSystemBackend.jwt.AuthenticationInterceptor;
import com.example.LabSystemBackend.jwt.JwtUtil;
import com.example.LabSystemBackend.service.ItemService;
import com.example.LabSystemBackend.service.OrderService;
import com.example.LabSystemBackend.ui.KeyMessage;
import com.example.LabSystemBackend.ui.OutputMessage;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
/**
 * @version 1.0
 * @author Sheyang Li
 *
 */
@ActiveProfiles("unittest")
@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    AuthenticationInterceptor interceptor;
    @MockBean
    OrderService orderService;
    @MockBean
    ItemService itemService;
    private String token;
    private String email;
    private String testItemName;

    //test data
    User user = new User(39,"firstName","lastName","test_user@testpse.com",
            UserRole.ADMIN,"a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3",
            UserAccountStatus.ACTIVE,true);

    private final Item testItem = new Item(1,"Loptop",10,"Huawei");
    private final Item testItem1 = new Item(2,"Handy",10,"Iphone");
    private final List<Item> testItems = new ArrayList<>();

    @BeforeEach
    private void setup() {
        token = JwtUtil.createToken(user);
        email = user.getEmail();
        testItems.add(testItem);
        testItems.add(testItem1);
    }

    void GetAllItems(String  url) throws Exception {
        String body = "{}";
        when(itemService.getAllItemsAndAmount()).thenReturn(testItems);
        try {
            when(interceptor.preHandle(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class),
                    Mockito.any(Object.class))).thenReturn(true);
            mockMvc.perform(MockMvcRequestBuilders
                            .get(url)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(body)
                            .header("Authorization", token))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(jsonPath("$.resultCode").value(200))
                    .andExpect(jsonPath("$..message").value("SUCCESS"))
                    .andReturn();
        } catch (Exception e) {
            Assert.fail("fail to request");
        }

    }
    @Test
    void userGetAllItems() throws Exception {
        GetAllItems("/stocks/userGetAllItems");
    }
    @Test
    void adminGetAllItems() throws Exception {
        GetAllItems("/stocks/adminGetAllItems");
    }

    private void addItem(Boolean itemExists, String testItemName, int amount, String description, int resultCode,
                         String message) {
        String url = "/stocks/addItem";
        String body = "{\""+KeyMessage.ITEM_NAME+"\":\""+testItemName+"\"," +
                "\""+KeyMessage.AMOUNT+"\":\""+amount+"\"," +
                "\""+KeyMessage.DESCRIPTION+"\":\""+description+"\"}";
        when(itemService.itemExists(testItemName)).thenReturn(itemExists);
        when(itemService.addItem(testItemName, amount, description)).thenReturn(1);

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
    void addItemFail() {
        addItem(true, testItem.getItemName(),10,testItem.getItemDescri(),500,
                OutputMessage.ITEM_EXIST );
    }

    @Test
    void addItemSuccess(){
        addItem(false, testItem1.getItemName(),10,testItem1.getItemDescri(),
                200, "SUCCESS");
    }

    private void deleteItem(Boolean itemExists, String testItemName, int resultCode, String message) {
        String url = "/stocks/deleteItem";
        String body = "{\""+KeyMessage.ITEM_NAME+"\":\""+testItemName+"\"}";
        when(itemService.itemExists(testItemName)).thenReturn(itemExists);
        when(itemService.getItemByName(testItemName)).thenReturn(testItem);
        when(itemService.deleteItem(itemService.getItemByName(testItemName).getItemId())).thenReturn(1);

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
                    //.andExpect(jsonPath("$..data").value(1))
                    .andReturn();
        } catch (Exception e) {
            Assert.fail("fail to request");
        }
    }
    @Test
    void deleteItemFail() {
        deleteItem(false, testItem1.getItemName(),500,
                OutputMessage.ITEM_NOT_EXIST );
    }
    @Test
    void deleteItemSuccess(){
        deleteItem(true, testItem.getItemName(),200, "SUCCESS");
    }

    private void changeItemAmount(Boolean itemExists, String testItemName, int amount, int resultCode, String message) {
        String url = "/stocks/changeItemAmount";
        String body = "{\""+KeyMessage.ITEM_NAME+"\":\""+testItemName+"\",\""+KeyMessage.AMOUNT+"\":\""+amount+"\" }";
        when(itemService.itemExists(testItemName)).thenReturn(itemExists);
        when(itemService.getItemByName(testItemName)).thenReturn(testItem);
        when(itemService.changeItemAmount(itemService.getItemByName(testItemName).getItemId(), amount)).thenReturn(1);

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
                    //.andExpect(jsonPath("$..data").value(1))
                    .andReturn();
        } catch (Exception e) {
            Assert.fail("fail to request");
        }
    }

    @Test
    void changeItemAmountFail() {
        changeItemAmount(false, testItem1.getItemName(), testItem1.getAmount(),500,
                OutputMessage.ITEM_NOT_EXIST );
    }

    @Test
    void changeItemAmountSuccess(){
        changeItemAmount(true, testItem.getItemName(), testItem.getAmount(),200, "SUCCESS");
    }

}