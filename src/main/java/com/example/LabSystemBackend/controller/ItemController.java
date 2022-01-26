package com.example.LabSystemBackend.controller;


import com.example.LabSystemBackend.common.Response;
import com.example.LabSystemBackend.common.ResponseGenerator;
import com.example.LabSystemBackend.entity.Item;
import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.service.ItemService;
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

    @ApiOperation("get a list of all Items")
    @GetMapping("getAllItems")
    public Response getAllItemsAndAmount() {
        List<Item> items = itemService.getAllItemsAndAmount();
        List<Map<String, String>> itemsInfo = new ArrayList<>();
        for (Item item : items) {
            int idx = items.indexOf(item);
            itemsInfo.add(new HashMap<>());
            itemsInfo.get(idx).put("itemName", item.getItemName());
            itemsInfo.get(idx).put("amount", item.getAmount().toString());
            itemsInfo.get(idx).put("link", item.getLink());

        }
        return ResponseGenerator.genSuccessResult(itemsInfo);


    }


    @ApiOperation("add a new device")
    @PostMapping("addItem")
    public Response addItem(@ApiParam(name = "itemName", value = "itemName", required = true)
                            @RequestBody Map<String, String> body) {
        String itemName = body.get("itemName");
        int amount = Integer.parseInt(body.get("amount"));
        String link = body.get("link");
        String description = body.get("description");
        if (itemService.itemExists(itemName)) {
            return ResponseGenerator.genFailResult("Item already exists");
        }
        return ResponseGenerator.genSuccessResult(itemService.addItem(itemName, amount, link, description));
    }

    @ApiOperation("delete a device from database")
    @PostMapping("deleteItem")
    public Response deleteItem(@ApiParam(name = "itemName", value = "itemName", required = true)
                               @RequestBody Map<String, String> body) {
        String itemName = body.get("itemName");
        logger.info("itemName: " + itemName);
        if (!itemService.itemExists(itemName)) {
            return ResponseGenerator.genFailResult("Item not exists");
        }
        return ResponseGenerator.genSuccessResult(itemService.deleteItem(itemService.getItemByName(itemName).getItemId()));

    }

    @ApiOperation("change the amount of one item")
    @PostMapping("changeItemAmount")
    public Response changeItemAmount(@ApiParam(name = "itemName", value = "itemName", required = true)
                                     @RequestBody Map<String, String> body) {
        String itemName = body.get("itemName");
        int newAmount = Integer.parseInt(body.get("amount"));
        if (!itemService.itemExists(itemName)) {
            return ResponseGenerator.genFailResult("Item not exists");
        }
        return ResponseGenerator.genSuccessResult(itemService.changeItemAmount(itemService.getItemByName(itemName).getItemId()
                , newAmount));

    }

}
