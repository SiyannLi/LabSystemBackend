package com.example.LabSystemBackend.controller;


import com.example.LabSystemBackend.common.Response;
import com.example.LabSystemBackend.common.ResponseGenerator;
import com.example.LabSystemBackend.entity.Item;
import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.jwt.JwtUtil;
import com.example.LabSystemBackend.jwt.comment.AdminLoginToken;
import com.example.LabSystemBackend.jwt.comment.UserLoginToken;
import com.example.LabSystemBackend.service.ItemService;
import com.example.LabSystemBackend.ui.KeyMessage;
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
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/stocks")
public class ItemController {
    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private ItemService itemService;

    private List<Map<String, String>> getAllItems() {
        List<Item> items = itemService.getAllItemsAndAmount();
        List<Map<String, String>> itemsInfo = new ArrayList<>();
        for (Item item : items) {
            int idx = items.indexOf(item);
            itemsInfo.add(new HashMap<>());
            itemsInfo.get(idx).put("itemName", item.getItemName());
            itemsInfo.get(idx).put("amount", item.getAmount().toString());
            itemsInfo.get(idx).put("description", item.getItemDescri());

        }
        return itemsInfo;
    }

    @UserLoginToken
    @ApiOperation("get a list of all Items")
    @GetMapping("userGetAllItems")
    public Response userGetAllItems(@RequestHeader(KeyMessage.TOKEN) String token) {
        String email = JwtUtil.getUserInfo(token, KeyMessage.EMAIL);
        return ResponseGenerator.genSuccessResult(UserController.emailTokens.get(email), getAllItems());


    }

    @AdminLoginToken
    @ApiOperation("get a list of all Items")
    @GetMapping("adminGetAllItems")
    public Response adminGetAllItems(@RequestHeader(KeyMessage.TOKEN) String token) {
        String email = JwtUtil.getUserInfo(token, KeyMessage.EMAIL);
        return ResponseGenerator.genSuccessResult(UserController.emailTokens.get(email), getAllItems());


    }


    @AdminLoginToken
    @ApiOperation("add a new device")
    @PostMapping("addItem")
    public Response addItem(@RequestHeader(KeyMessage.TOKEN) String token,
                            @ApiParam(name = "itemName", value = "itemName", required = true)
                            @RequestBody Map<String, String> body) {
        String opEmail = JwtUtil.getUserInfo(token, KeyMessage.EMAIL);
        String itemName = body.get("itemName");
        int amount = Integer.parseInt(body.get("amount"));
        String description = body.get("description");
        if (itemService.itemExists(itemName)) {
            return ResponseGenerator.genFailResult(UserController.emailTokens.get(opEmail),"Item already exists");
        }
        return ResponseGenerator.genSuccessResult(UserController.emailTokens.get(opEmail), itemService.addItem(itemName, amount, description));
    }

    @AdminLoginToken
    @ApiOperation("delete a device from database")
    @PostMapping("deleteItem")
    public Response deleteItem(@RequestHeader(KeyMessage.TOKEN) String token,
                               @ApiParam(name = "itemName", value = "itemName", required = true)
                               @RequestBody Map<String, String> body) {
        String opEmail = JwtUtil.getUserInfo(token, KeyMessage.EMAIL);
        String itemName = body.get("itemName");
        logger.info("itemName: " + itemName);
        if (!itemService.itemExists(itemName)) {
            return ResponseGenerator.genFailResult(UserController.emailTokens.get(opEmail),"Item not exists");
        }
        return ResponseGenerator.genSuccessResult(UserController.emailTokens.get(opEmail)
                , itemService.deleteItem(itemService.getItemByName(itemName).getItemId()));

    }

    @AdminLoginToken
    @ApiOperation("change the amount of one item")
    @PostMapping("changeItemAmount")
    public Response changeItemAmount(@RequestHeader(KeyMessage.TOKEN) String token,
                                     @ApiParam(name = "itemName", value = "itemName", required = true)
                                     @RequestBody Map<String, String> body) {
        String opEmail = JwtUtil.getUserInfo(token, KeyMessage.EMAIL);
        String itemName = body.get("itemName");
        int newAmount = Integer.parseInt(body.get("amount"));
        if (!itemService.itemExists(itemName)) {
            return ResponseGenerator.genFailResult(UserController.emailTokens.get(opEmail)
                    ,"Item not exists");
        }
        return ResponseGenerator.genSuccessResult(UserController.emailTokens.get(opEmail)
                , itemService.changeItemAmount(itemService.getItemByName(itemName).getItemId(), newAmount));

    }

}
