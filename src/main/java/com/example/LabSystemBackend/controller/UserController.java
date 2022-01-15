package com.example.LabSystemBackend.controller;


import com.example.LabSystemBackend.common.Response;
import com.example.LabSystemBackend.common.ResponseGenerator;
import com.example.LabSystemBackend.entity.Notification;
import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.entity.UserAccountStatus;
import com.example.LabSystemBackend.entity.UserRole;
import com.example.LabSystemBackend.service.NotificationService;
import com.example.LabSystemBackend.service.UserService;
import com.example.LabSystemBackend.util.DataGenerate;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@CrossOrigin("*")
@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;
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

    @ApiOperation("send verification code")
    @PostMapping("sendVerificationCode")
    public Response sendVerificationCode(@ApiParam(name = "email", value = "email", required = true)
                                             @Param("email") @RequestBody Map<String, String> email) {
        User user = new User();
        user.setUserRole(UserRole.VISITOR);
        user.setUserAccountStatus(UserAccountStatus.CONFIRMING);
        user.setEmail(email.get("email"));
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setUserPassword("abcd");
        Random random = new Random();
        String verificationCode = "";
        for (int i = 0; i< 6; i++) {
            verificationCode += random.nextInt(10);
        }
        user.setVerifyCode(Integer.parseInt(verificationCode));
        userService.insertUser(user);
        Notification notification = new Notification();
        notification.setSenderId(0);
        notification.setRecipientId(user.getUserId());
        notification.setContent(verificationCode);
        notification.setSubject("verification code");
        notificationService.sendNotification(notification);
        logger.info(user.toString());
        logger.info(notification.toString());
        return ResponseGenerator.genSuccessResult();
    }

    @ApiOperation("get one test user")
    @GetMapping("getTestUser")
    //不需要用到数据库
    public Response getUserTest() {
        User user = DataGenerate.generateUser();
        logger.info(user.toString());
        return ResponseGenerator.genSuccessResult(user);

    }

    @ApiOperation("get all users")
    @GetMapping("getAllUsers")
    public Response getAllUsers() {
        List<User> users = userService.getAllUsers();
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

        for (User user : users) {
            logger.info(user.toString());
        }

        return ResponseGenerator.genSuccessResult(users);
    }

    @ApiOperation("insert one user")
    @PostMapping("/insertUser")
    public Response insertUser(@ApiParam(name = "user", value = "user", required = true) @Param("user") @RequestParam User user) {
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
        logger.info(user.toString());

        return ResponseGenerator.genSuccessResult(user);
    }

    @ApiOperation("login")
    @PostMapping("login")
    public Response login(String email, String password, boolean isAdmin) {
        return ResponseGenerator.genSuccessResult();
    }

    @ApiOperation("log out")
    @PostMapping("logout")
    public Response logout(String email) {
        return ResponseGenerator.genSuccessResult();
    }

    @ApiOperation("register one account")
    @PostMapping("register")
    public Response register(String email, String password, String firstName, String lastName, int verificationCode, boolean isAdmin) {
        return ResponseGenerator.genSuccessResult(userService.register(email, password, firstName, lastName, verificationCode, isAdmin));
    }


    @ApiOperation("reset password")
    @PostMapping("resetPassword")
    public Response resetPassword(String email, String newPassword, String verificationCode) {
        return ResponseGenerator.genSuccessResult(userService.resetPassword(email, newPassword));
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
        return ResponseGenerator.genSuccessResult(userService.changeUserName(userId, newFirstName, newLastName));

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
        return ResponseGenerator.genSuccessResult(userService.getAllAdministrators());
    }


}


