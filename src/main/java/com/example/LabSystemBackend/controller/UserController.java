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
    public Response getUser(@ApiParam(name = "userId", value = "userId", required = true) @PathVariable int userId) {
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
    public Response getUserTest() {
        User user = DataGenerate.generateUser();

        System.out.println(user);
        System.out.println("\n");
        System.out.println(new Date());
        return ResponseGenerator.genSuccessResult(user);

    }

    @ApiOperation("get all users")
    @GetMapping("getAllUsers")
    public Response getAllUsers() {
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
    public Response getAllTest() {
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
    public Response insertUser(@ApiParam(name = "user", value = "user", required = true) @Param("user") @RequestBody User user) {
        if (userService.insertUser(user) > 0) {
            return ResponseGenerator.genSuccessResult();
        } else {
            return ResponseGenerator.genFailResult("fail to insert user");
        }
    }

    @ApiOperation("insert one user")
    @PostMapping("/insertUserTest")
    //不需要用到数据库
    public Response insertUserTest(@ApiParam(name = "user", value = "user", required = true) @Param("user") @RequestBody User user) {
        user.setUserRole(UserRole.VISITOR);
        System.out.println(user);
        System.out.println("\n");
        System.out.println(new Date());

        return ResponseGenerator.genSuccessResult(user);
    }

    @ApiOperation("login")
    @PostMapping("login")
    public Response login(String email, String password, boolean isAdmin) {
        return ResponseGenerator.genSuccessResult(userService.login(email,password,isAdmin));
    }

    @ApiOperation("log out")
    @PostMapping("logout")
    public Response logout(String email) {
        return ResponseGenerator.genSuccessResult(userService.logout(email));
    }

    @ApiOperation("register one account")
    @PostMapping("register")
    public Response register(String email, String password, String firstName, String lastName, String verificationCode) {
        return ResponseGenerator.genSuccessResult(userService.register(email,password,firstName,lastName,verificationCode));    }


    @ApiOperation("reset password")
    @PostMapping("resetPassword")
    public Response resetPassword(String email, String newPassword, String verificationCode) {
        return ResponseGenerator.genSuccessResult(userService.resetPassword(email, newPassword, verificationCode));
    }


    @ApiOperation("confirm the registration of user to create a new account")
    @PostMapping("confirmUserRegistration")
    public Response confirmUserRegistration(int userId) {
        return ResponseGenerator.genSuccessResult(userService.confirmUserRegistration(userId));
    }

    @ApiOperation("reject the application of user to create a new account")
    @PostMapping("rejectUserRegistration")
    public Response rejectUserRegistration(int userId) {
        return ResponseGenerator.genSuccessResult(userService.rejectUserRegistration(userId));

    }

    @ApiOperation("change username")
    @PostMapping("changeUserName")
    public Response changeUserName(int userId, String newFirstName, String newLastName) {
        return ResponseGenerator.genSuccessResult(userService.changeUserName(userId,newFirstName,newLastName));

    }

    @ApiOperation("deactivate account")
    @PostMapping("deactivateUser")
    public Response deactivateUser(int userId) {
        return ResponseGenerator.genSuccessResult(userService.deactivateUser(userId));
    }

    @ApiOperation("activate account")
    @PostMapping("activateUser")
    public Response activeUser(int userId) {
        return ResponseGenerator.genSuccessResult(userService.activateUser(userId));
    }


    @ApiOperation("get a list of all admins")
    @GetMapping("getAllAdministrator")
    public Response getAllAdministrator() {
        return ResponseGenerator.genSuccessResult(userService.getAllAdministrator());
    }


}

