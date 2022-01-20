package com.example.LabSystemBackend.controller;


import com.example.LabSystemBackend.common.Response;
import com.example.LabSystemBackend.common.ResponseGenerator;
import com.example.LabSystemBackend.entity.*;
import com.example.LabSystemBackend.service.VerifyCodeService;
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
    @Autowired
    private VerifyCodeService verifyCodeService;


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
    public Response sendVerificationCode(HttpServletRequest request, HttpServletResponse response,
                                         @ApiParam(name = "emailAndInfo", value = "emailAndInfo", required = true)
                                         @Param("email") @RequestBody Map<String, String> body) {
        String info = body.get("info");
        String email = body.get("email");
        boolean userExist = userService.emailExists(body.get("email"));
        boolean send = false;
        String message = "succeed";
        switch (info) {
            case "register": {
                if (!userExist) {
                    logger.info(body.get("email"));
                    send = true;
                } else {
                    message = "This email has been registered.";
                }
                break;
            }
            case "forget password": {
                if (userExist) {
                    logger.info(body.get("email"));
                    send = true;
                } else {
                    message = "This email has been not registered.";
                }
                break;
            }
            default: {
                message = "error";
            }
        }
        if (send) {
            String verCode = verifyCodeService.getRandomVerCode();
            Notification notification = verifyCodeService.sendVerifyCode(email, verCode);
            HttpSession session = request.getSession();
            session.setAttribute("verifyCode", verCode);
            final Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    session.removeAttribute("verifyCode");
                    timer.cancel();
                }
            }, 10 * 60 * 1000);
            logger.info(notification.toString());
            send = false;
            return ResponseGenerator.genSuccessResult(message);
        }
        return ResponseGenerator.genFailResult(message);
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
    @PostMapping("visitorLogin")
    public Response visitorLogin(HttpServletRequest request, HttpServletResponse response,
                                 @RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        logger.debug("visitorLogin");
        logger.debug("email " + email);
        logger.debug("password" + password);
        String result = (String) request.getAttribute("verification result");
        if ("logged in".equals(result)) {
            return ResponseGenerator.genFailResult("logged in");
        } else if ("wrong token".equals(result)) {
            return ResponseGenerator.genFailResult("wrong token");
        } else {
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
            return ResponseGenerator.genSuccessResult(token, JwtUtil.getUserInfo(token, "firstName"));
        }
    }

    @ApiOperation("admin login")
    @PostMapping("adminLogin")
    public Response adminLogin(HttpServletRequest request, HttpServletResponse response,
                               @ApiParam(name = "emailAndPass", value = "emailAndPass", required = true)
                               @RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        logger.info(email);
        logger.info(password);

        String result = (String) request.getAttribute("verification result");
        if ("logged in".equals(result)) {
            return ResponseGenerator.genFailResult("logged in");
        } else if ("wrong token".equals(result)) {
            return ResponseGenerator.genFailResult("wrong token");
        } else {
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
            return ResponseGenerator.genSuccessResult(token, JwtUtil.getUserInfo(token, "firstName"));
        }
    }

    @PostMapping("logout")
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
    public Response register(HttpServletRequest request, HttpServletResponse response,
                             @ApiParam(name = "email", value = "email", required = true)
                             @RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("userPassword");
        String firstName = body.get("firstName");
        String lastName = body.get("lastName");
        String verificationCode = body.get("verifyCode");

        logger.info("email " + email);
        logger.info("password " + password);
        logger.info("firstName " + firstName);
        logger.info("lastName " + lastName);
        logger.info("verificationCode " + verificationCode);

        HttpSession session = request.getSession();
        if (verificationCode.equals(session.getAttribute("verifyCode"))) {

            return ResponseGenerator.genSuccessResult(userService.register(email, password, firstName, lastName, verificationCode));
        } else {
            return ResponseGenerator.genFailResult("Invalid verification code");
        }
    }

    @ApiOperation("change password")
    @PostMapping("changePassword")
    public Response changePassword(HttpServletRequest request, HttpServletResponse response,
                                   @ApiParam(name = "email", value = "email", required = true)
                                   @RequestBody Map<String, String> body) {
        String email = body.get("email");
        String newPassword = body.get("password");
        String verCode = body.get("verifyCode");

        logger.info("email " + email);
        logger.info("password " + newPassword);
        logger.info("verificationCode " + verCode);


        HttpSession session = request.getSession();
        User user = userService.getUserByEmail(email);
        if (verCode.equals(session.getAttribute("verifyCode"))) {
            user.setUserPassword(newPassword);
            return ResponseGenerator.genSuccessResult("Password is changed");
        } else {
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


