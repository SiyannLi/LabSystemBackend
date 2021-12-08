package com.example.LabSystemBackend.controller;


import com.example.LabSystemBackend.common.Response;
import com.example.LabSystemBackend.common.ResponseGenerator;
import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.entity.UserRole;
import com.example.LabSystemBackend.service.UserService;
import com.example.LabSystemBackend.util.DataGenerate;
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
        System.out.println("\n");
        System.out.println(new Date());
        return ResponseGenerator.genSuccessResult(user);

    }

    @ApiOperation("get all users")
    @GetMapping("getAllUsers")
    public Response<Object> getAllUsers() {
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
        System.out.println("\n");
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
        System.out.println("\n");
        System.out.println(new Date());

        return ResponseGenerator.genSuccessResult(user);
    }

    @ApiOperation("login")
    @PostMapping("login")
    public Response<Object> login(String email, String password, boolean isAdmin) {
        return null;
    }

    @ApiOperation("log out")
    @PostMapping("logout")
    public Response<Object> logout(String email) {
        return null;
    }

    @ApiOperation("register one account")
    @PostMapping("register")
    public Response<Object> register(String email, String password, String firstName, String lastName, String vertificationCode){
        return null;
    }


    @ApiOperation("reset password")
    @PostMapping("resetPassword")
    public  Response<Object> resetPassword(String email, String newPassword, String vertificationCode){
        return null;
    }


    @ApiOperation("confirm the registration of user to create a new account")
    @PostMapping("confirmUserRegistration")
    public Response<Object> confirmUserRegistration(int userId){
        return null;
    }

    @ApiOperation("reject the application of user to create a new account")
    @PostMapping("rejectUserRegistration")
    public Response<Object> rejectUserRegistration(int userId){
        return null;
    }

    @ApiOperation("change username")
    @PostMapping("changeUserName")
    public Response<Object> changeUserName(int userId, String newFirstName,String newLastName){
        return null;
    }

    @ApiOperation("deactivate account")
    @PostMapping("deactivateUser")
    public Response<Object> deactiveUser(int userId){
        return null;
    }
    @ApiOperation("activate account")
    @PostMapping("activateUser")
    public Response<Object> activeUser(int userId){
        return null;
    }


    @ApiOperation("get a list of all admins")
    @GetMapping("getAllAdministrator")
    public Response<Object> getAllAdministrator(){
        return null;
    }


}


