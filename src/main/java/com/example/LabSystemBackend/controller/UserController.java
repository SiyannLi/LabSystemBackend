package com.example.LabSystemBackend.controller;


import com.example.LabSystemBackend.common.Response;
import com.example.LabSystemBackend.common.ResponseGenerator;
import com.example.LabSystemBackend.entity.*;
import com.example.LabSystemBackend.jwt.JwtUtil;
import com.example.LabSystemBackend.service.NotificationService;
import com.example.LabSystemBackend.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

    private String getRandomVerCode() {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < 6; i++) {

            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    @ApiOperation("send verification code")
    @PostMapping("sendVerificationCode")
    public Response sendVerificationCode(@ApiParam(name = "email", value = "email", required = true)

                                         @Param("email") @RequestBody Map<String, String> email) {
        if (!userService.emailExists(email.get("email"))) {
            logger.info(email.get("email"));
            User user = new User();
            user.setUserRole(UserRole.VISITOR);
            user.setUserAccountStatus(UserAccountStatus.CONFIRMING);
            user.setEmail(email.get("email"));
            user.setFirstName("firstName");
            user.setLastName("lastName");
            user.setUserPassword("abcd");
            String verificationCode = getRandomVerCode();
            user.setVerifyCode(verificationCode);
            userService.insertUser(user);
            Notification notification = new Notification();
            notification.setSenderId(0);
            notification.setRecipientId(user.getUserId());
            notification.setContent(String.format(NotificationTemplate.VERIFICATION_CODE.getContent(), verificationCode));
            notification.setSubject(NotificationTemplate.VERIFICATION_CODE.getSubject());
            logger.info(user.toString());
            logger.info(notification.toString());
            notificationService.sendNotification(notification);
            return ResponseGenerator.genSuccessResult();
        } else {
            return ResponseGenerator.genFailResult("This email has been registered.");
        }

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


    @ApiOperation("insert one user")
    @PostMapping("/insertUser")
    public Response insertUser(@ApiParam(name = "user", value = "user", required = true) @Param("user") @RequestParam User user) {
        if (userService.insertUser(user) > 0) {
            return ResponseGenerator.genSuccessResult();
        } else {
            return ResponseGenerator.genFailResult("fail to insert user");
        }
    }


    @ApiOperation("visitor login")
    @RequestMapping("visitorLogin")
    public Response visitorLogin(HttpServletRequest request, HttpServletResponse response,
                                 String email, String password) {
        String result = (String) request.getAttribute("verification result");
        switch (result) {
            case "logged in":
                return ResponseGenerator.genFailResult("logged in");
            case "wrong token":
                return ResponseGenerator.genFailResult("wrong token");
            case "not logged in": {
                if (!userService.emailExists(email)) {
                    return ResponseGenerator.genFailResult("User does not exist");
                }
                User user = userService.getUserByEmail(email);
                if (!password.equals(user.getUserPassword())) {
                    return ResponseGenerator.genFailResult("Incorrect password");
                }
                String token = JwtUtil.createToken(user);
                HttpSession session = request.getSession();
                session.setAttribute("token", token);
                return ResponseGenerator.genSuccessResult(token);
            }
            default:
                return ResponseGenerator.genFailResult("error");
        }

    }

    @ApiOperation("admin login")
    @RequestMapping("adminLogin")
    public Response adminLogin(HttpServletRequest request, HttpServletResponse response,
                               String email, String password) {
        String result = (String) request.getAttribute("verification result");
        switch (result) {
            case "logged in":
                return ResponseGenerator.genFailResult("logged in");
            case "wrong token":
                return ResponseGenerator.genFailResult("wrong token");
            case "not logged in": {
                if (!userService.emailExists(email)) {
                    return ResponseGenerator.genFailResult("User does not exist");
                }
                User user = userService.getUserByEmail(email);
                if (!password.equals(user.getUserPassword())) {
                    return ResponseGenerator.genFailResult("Incorrect password");
                }
                if (!user.getUserRole().getRoleValue().equals("admin")) {
                    return ResponseGenerator.genFailResult("Not an administrator account");
                }
                String token = JwtUtil.createToken(user);
                HttpSession session = request.getSession();
                session.setAttribute("token", token);
                return ResponseGenerator.genSuccessResult(token);
            }
            default:
                return ResponseGenerator.genFailResult("error");


        }
    }

    @RequestMapping("logout")
    public Response logout(HttpServletRequest request, HttpServletResponse response) {

        String result = (String) request.getAttribute("verification result");

        if (!"logged in".equals(result)) {
            return ResponseGenerator.genFailResult("No user login or token error, can't log out");
        }

        HttpSession session = request.getSession();

        session.setAttribute("token", null);
        session.invalidate();
        return ResponseGenerator.genSuccessResult("logout");

    }

    @ApiOperation("register one account")
    @PostMapping("register")
    public Response register(String email, String password, String firstName, String lastName, String verificationCode) {
        User user = userService.getUserByEmail(email);
        logger.info(user.toString());
        if (user.getVerifyCode().equals(verificationCode)) {

            return ResponseGenerator.genSuccessResult(userService.register(email, password, firstName, lastName, verificationCode));
        } else {
            userService.deleteUser(user.getUserId());
            return ResponseGenerator.genFailResult("Invalid verification code");
        }
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


