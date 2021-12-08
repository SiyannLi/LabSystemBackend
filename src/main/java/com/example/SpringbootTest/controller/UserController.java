package com.example.SpringbootTest.controller;


import com.example.SpringbootTest.common.Response;
import com.example.SpringbootTest.common.ResponseGenerator;
import com.example.SpringbootTest.entity.User;
import com.example.SpringbootTest.entity.UserRole;
import com.example.SpringbootTest.service.UserService;
import com.example.SpringbootTest.util.DataGenerate;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@CrossOrigin("*")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("get one user")
    @GetMapping("get/{userId}")
    public Response<Object> getUser(@ApiParam(name = "userId", value = "userId", required = true) @PathVariable int userId) {
        User user = userService.getUser(userId);
        if (null != user) {
            return ResponseGenerator.genSuccessResult(user);
        } else {
            return ResponseGenerator.genFailResult("user dont exist");
        }
    }

    @ApiOperation("get one test user")
    @GetMapping("getTestUser")
    //不需要用到数据库
    public Response<Object> getUserTest() {
        User user = DataGenerate.generateUser();

        System.out.println(user);
        System.out.println(new Date());
        return ResponseGenerator.genSuccessResult(user);

    }

    @ApiOperation("get all users")
    @GetMapping("getAll")
    public Response<Object> getAll() {
        List<User> users = userService.getAllUser();
        if (!users.isEmpty()) {
            return ResponseGenerator.genSuccessResult(users);
        } else {
            return ResponseGenerator.genFailResult("no user exists");

        }
    }

    @ApiOperation("get all test users")
    @GetMapping("getAllTest")
    //不需要用到数据库
    public Response<Object> getAllTest() {
        List<User> users = new ArrayList<>();
        users.add(DataGenerate.generateUser());
        users.add(DataGenerate.generateUser());
        users.add(DataGenerate.generateUser());

        System.out.println(users);
        System.out.println(new Date());

        return ResponseGenerator.genSuccessResult(users);
    }

    @ApiOperation("insert one user")
    @PostMapping("/insertUser")
    public Response<Object> insertUser(@ApiParam(name = "user", value = "user", required = true) @Param("user") @RequestBody User user) {
        if (userService.insertUser(user) > 0) {
            return ResponseGenerator.genSuccessResult();
        } else {
            return ResponseGenerator.genFailResult("fail to insert user");
        }
    }

    @ApiOperation("insert one user")
    @PostMapping("/insertUserTest")
    //不需要用到数据库
    public Response<Object> insertUserTest(@ApiParam(name = "user", value = "user", required = true) @Param("user") @RequestBody User user) {
        user.setUserRole(UserRole.VISITOR);
        System.out.println(user);
        System.out.println(new Date());

        return ResponseGenerator.genSuccessResult(user);
    }


}


