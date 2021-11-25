package com.example.SpringbootTest.controller;


import com.example.SpringbootTest.common.Result;
import com.example.SpringbootTest.common.ResultGenerator;
import com.example.SpringbootTest.entity.User;
import com.example.SpringbootTest.service.UserService;
import com.example.SpringbootTest.util.DataGenerate;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("get one user")
    @GetMapping("get/{userId}")
    public Result<Object> getUser(@ApiParam(name = "userId", value = "userId", required = true) @PathVariable int userId) {
        User user = userService.getUser(userId);
        if (null != user) {
            return ResultGenerator.genSuccessResult(user);
        } else {
            return ResultGenerator.genFailResult("user dont exist");
        }
    }

    @ApiOperation("get one test user")
    @GetMapping("getTestUser")
    //不需要用到数据库
    public Result<Object> getUserTest() {
        User user = DataGenerate.generateUser();
        return ResultGenerator.genSuccessResult(user);

    }

    @ApiOperation("get all users")
    @GetMapping("getAll")
    public Result<Object> getAll() {
        List<User> users = userService.getAllUser();
        if (!users.isEmpty()) {
            return ResultGenerator.genSuccessResult(users);
        } else {
            return ResultGenerator.genFailResult("no user exists");

        }
    }

    @ApiOperation("get all test users")
    @GetMapping("getAllTest")
    //不需要用到数据库
    public Result<Object> getAllTest() {
        List<User> users = new ArrayList<>();
        users.add(DataGenerate.generateUser());
        users.add(DataGenerate.generateUser());
        users.add(DataGenerate.generateUser());

        return ResultGenerator.genSuccessResult(users);
    }

    @ApiOperation("insert one user")
    @PostMapping("/insertUser")
    public Result<Object> insertUser(@ApiParam(name = "user", value = "user", required = true) @Param("user") @RequestBody User user) {
        if (userService.insertUser(user) > 0) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("fail to insert user");
        }
    }

    @ApiOperation("insert one user")
    @PostMapping("/insertUserTest")
    //不需要用到数据库
    public Result<Object> insertUserTest(@ApiParam(name = "user", value = "user", required = true) @Param("user") @RequestBody User user) {
        return ResultGenerator.genSuccessResult(user);
    }


}


