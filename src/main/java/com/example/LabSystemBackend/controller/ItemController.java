package com.example.LabSystemBackend.controller;


import com.example.LabSystemBackend.common.Response;
import com.example.LabSystemBackend.common.ResponseGenerator;
import com.example.LabSystemBackend.service.ItemService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/devices")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @ApiOperation("get a list of all devices and their amounts")
    @GetMapping("getAllDevicesAndAmount")
    public Response getAllItemsAndAmount() {
        return ResponseGenerator.genSuccessResult(itemService.getAllItemsAndAmount());

    }


    @ApiOperation("add a new device")
    @PostMapping("addItem")
    public Response addItem(String itemName, int Amount, String link, String description) {
        return null;
    }

    @ApiOperation("delete a device from database")
    @DeleteMapping("deleteItem")
    public Response deleteItem(int itemId) {
        return null;
    }

    @ApiOperation("change the amount of one item")
    @PostMapping("changeItemAmount")
    public Response changeItemAmount(int itemId, int newAmount) {
        return null;
    }

    @ApiOperation("merge two Items and sum the amounts")
    @PostMapping("mergeItem")
    public Response mergeItem(int itemId, int targetItemId) {
        return null;
    }
}
