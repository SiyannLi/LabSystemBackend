package com.example.LabSystemBackend.controller;


import com.example.LabSystemBackend.common.Response;
import com.example.LabSystemBackend.common.ResponseGenerator;
import com.example.LabSystemBackend.entity.*;
import com.example.LabSystemBackend.service.VerifyCodeService;
import com.example.LabSystemBackend.jwt.JwtUtil;
import com.example.LabSystemBackend.service.NotificationService;
import com.example.LabSystemBackend.service.UserService;
import com.example.LabSystemBackend.ui.NotificationTemplate;
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
            logger.info(notification.toString());
            HttpSession session = request.getSession();
            session.setAttribute("verifyCode", verCode);
            final Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    session.removeAttribute("verifyCode");
                    timer.cancel();
                }
            }, 10 * 60 * 1000);
            send = false;
            return ResponseGenerator.genSuccessResult(message);
        }
        return ResponseGenerator.genFailResult(message);
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
            if (!user.getUserAccountStatus().equals(UserAccountStatus.ACTIVE)) {
                return ResponseGenerator.genFailResult("Not an active account");
            }
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
            if (user.getUserRole().getRoleValue().equals("visitor")) {
                return ResponseGenerator.genFailResult("Not an administrator account");
            }
            if (!password.equals(user.getUserPassword())) {
                return ResponseGenerator.genFailResult("Incorrect password");
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
            return ResponseGenerator.genFailResult(result);
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
            String userName = firstName + " " + lastName;
            notificationService.sendNotificationByTemplate(email, NotificationTemplate.RESISTER_CONFIRMING
                    , userName);
            return ResponseGenerator.genSuccessResult(userService.register(email, password, firstName, lastName
                    , verificationCode));
        } else {
            return ResponseGenerator.genFailResult("Invalid verification code");
        }
    }

    @ApiOperation("reset password")
    @PostMapping("resetPassword")
    public Response resetPassword(HttpServletRequest request, HttpServletResponse response,
                                  @ApiParam(name = "email", value = "email", required = true)
                                  @RequestBody Map<String, String> body) {
        String email = body.get("email");
        String newPassword = body.get("password");
        String verCode = body.get("verifyCode");

        logger.info("email " + email);
        logger.info("password " + newPassword);
        logger.info("verificationCode " + verCode);


        HttpSession session = request.getSession();
        if (verCode.equals(session.getAttribute("verifyCode"))) {
            User user = userService.getUserByEmail(email);
            notificationService.sendNotificationByTemplate(email, NotificationTemplate.CHANGE_PASSWORD_SUCCESS
                    , user.getFullName());
            session.setAttribute("token", null);
            session.invalidate();
            return ResponseGenerator.genSuccessResult(userService.resetPassword(email, newPassword));
        } else {
            return ResponseGenerator.genFailResult("Invalid verification code");
        }

    }

    private String checkTokenOfSuperAdmin(HttpServletRequest request, HttpServletResponse response) {
        String message = checkTokenOfAdmin(request, response);
        if ("Not an administrator account".equals(message)) {
            return "Not an super administrator account";
        }
        if ("Success".equals(message)) {
            HttpSession session = request.getSession();
            String token = (String) session.getAttribute("token");
            boolean isSuperAdmin = JwtUtil.getUserInfo(token, "role").equals("super admin");
            if (!isSuperAdmin) {
                return "Not an super administrator account";
            }
        }
        return message;
    }


    private String checkTokenOfAdmin(HttpServletRequest request, HttpServletResponse response) {
        String message = checkTokenOfUser(request, response);
        if ("Success".equals(message) || "Not an active account".equals(message)) {
            HttpSession session = request.getSession();
            String token = (String) session.getAttribute("token");
            boolean isAdmin = !JwtUtil.getUserInfo(token, "role").equals("visitor");
            if (!isAdmin) {
                return "Not an administrator account";
            }
            return "Success";
        }
        return message;
    }

    private String checkTokenOfUser(HttpServletRequest request, HttpServletResponse response) {
        String result = (String) request.getAttribute("verification result");
        logger.info("verification result" + result);
        if (!"logged in".equals(result)) {
            return result;
        }
        HttpSession session = request.getSession();
        String token = (String) session.getAttribute("token");
        boolean isUserActive = JwtUtil.getUserInfo(token, "status").equals("active");
        if (!isUserActive) {
            return "Not an active account";
        }
        return "Success";
    }

    @ApiOperation("get a list of all accounts to be confirmed")
    @GetMapping("getAllAccountToBeConfirmed")
    public Response getAllAccountToBeConfirmed(HttpServletRequest request, HttpServletResponse response) {
        String message = checkTokenOfAdmin(request, response);
        if (!"Success".equals(message)) {
            return ResponseGenerator.genFailResult(message);
        }
        List<User> users = userService.getAllAccountToBeConfirmed();
        if (!users.isEmpty()) {
            return ResponseGenerator.genSuccessResult(users);
        } else {
            return ResponseGenerator.genFailResult("No users to be confirmed");

        }
    }

    @ApiOperation("confirm the registration of user to create a new account")
    @PostMapping("confirmUserRegistration")
    public Response confirmUserRegistration(HttpServletRequest request, HttpServletResponse response,
                                            @ApiParam(name = "email", value = "email", required = true)
                                            @RequestBody Map<String, String> body) {
        String message = checkTokenOfAdmin(request, response);
        if (!"Success".equals(message)) {
            return ResponseGenerator.genFailResult(message);
        }
        String email = body.get("email");
        if (userService.emailExists(email)) {
            User user = userService.getUserByEmail(email);
            notificationService.sendNotificationByTemplate(email, NotificationTemplate.REGISTER_SUCCESS
                    , user.getFullName());
            return ResponseGenerator.genSuccessResult(userService.confirmUserRegistration(user.getUserId()));
        } else {
            return ResponseGenerator.genFailResult("User does not exist");
        }
    }

    @ApiOperation("reject the application of user to create a new account")
    @PostMapping("rejectUserRegistration")
    public Response rejectUserRegistration(HttpServletRequest request, HttpServletResponse response,
                                           @ApiParam(name = "email", value = "email", required = true)
                                           @RequestBody Map<String, String> body) {
        String message = checkTokenOfAdmin(request, response);
        if (!"Success".equals(message)) {
            return ResponseGenerator.genFailResult(message);
        }
        String email = body.get("email");
        if (userService.emailExists(email)) {
            User user = userService.getUserByEmail(email);
            notificationService.sendNotificationByTemplate(email, NotificationTemplate.REGISTER_FAIL
                    , user.getFullName());
            return ResponseGenerator.genSuccessResult(userService.rejectUserRegistration(user.getUserId()));
        } else {
            return ResponseGenerator.genFailResult("User does not exist");
        }

    }


//    @ApiOperation("change username")
//    @PostMapping("changeUserName")
//    public Response changeUserName(int userId, String newFirstName, String newLastName) {
//        return ResponseGenerator.genSuccessResult(userService.changeUserName(userId, newFirstName, newLastName));
//
//    }

    @ApiOperation("deactivate account")
    @PostMapping("deactivateUser")
    public Response deactivateUser(HttpServletRequest request, HttpServletResponse response,
                                   @ApiParam(name = "email", value = "email", required = true)
                                   @RequestBody Map<String, String> body) {
        String message = checkTokenOfAdmin(request, response);
        if (!"Success".equals(message)) {
            return ResponseGenerator.genFailResult(message);
        }
        String email = body.get("email");
        if (userService.emailExists(email)) {
            User user = userService.getUserByEmail(email);
            if (user.getUserAccountStatus().equals(UserAccountStatus.INACTIVE)) {
                return ResponseGenerator.genFailResult("This account is already an inactive account and cannot be " +
                        "deactivated again");
            }
            return ResponseGenerator.genSuccessResult(userService.deactivateUser(user.getUserId()));

        } else {
            return ResponseGenerator.genFailResult("User does not exist");
        }
    }

    @ApiOperation("activate account")
    @PostMapping("activateUser")
    public Response activeUser(HttpServletRequest request, HttpServletResponse response,
                               @ApiParam(name = "email", value = "email", required = true)
                               @RequestBody Map<String, String> body) {
        String message = checkTokenOfAdmin(request, response);
        if (!"Success".equals(message)) {
            return ResponseGenerator.genFailResult(message);
        }
        String email = body.get("email");
        if (userService.emailExists(email)) {
            User user = userService.getUserByEmail(email);
            if (user.getUserAccountStatus().equals(UserAccountStatus.ACTIVE)) {
                return ResponseGenerator.genFailResult("This account is already an active account and cannot be " +
                        "activated again");
            }
            return ResponseGenerator.genSuccessResult(userService.activateUser(user.getUserId()));

        } else {
            return ResponseGenerator.genFailResult("User does not exist");
        }
    }

    @ApiOperation("get all users")
    @GetMapping("getAllUsers")
    public Response getAllUsers(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("Authorization");
        logger.info(token);
        String message = checkTokenOfAdmin(request, response);
        logger.info(message);
        String userMe = checkTokenOfUser(request, response);
        logger.info(userMe);
        if (!"Success".equals(message)) {
            return ResponseGenerator.genFailResult(message);
        }
        return ResponseGenerator.genSuccessResult(userService.getAllUsers());

    }

    @ApiOperation("get a list of all admins")
    @GetMapping("getAllAdministrator")
    public Response getAllAdministrator(HttpServletRequest request, HttpServletResponse response) {
        String message = checkTokenOfSuperAdmin(request, response);
        if (!"Success".equals(message)) {
            return ResponseGenerator.genFailResult(message);
        }
        return ResponseGenerator.genSuccessResult(userService.getAllAdministrators());
    }


    @ApiOperation("insert a admin")
    @PostMapping("insertAdmin")
    public Response insertAdmin(HttpServletRequest request, HttpServletResponse response,
                                @ApiParam(name = "email", value = "email", required = true)
                                @RequestBody Map<String, String> body) {
        String message = checkTokenOfSuperAdmin(request, response);
        if (!"Success".equals(message)) {
            return ResponseGenerator.genFailResult(message);
        }
        String email = body.get("email");
        if (userService.emailExists(email)) {
            User user = userService.getUserByEmail(email);
            if (!user.getUserRole().equals(UserRole.VISITOR)) {
                return ResponseGenerator.genFailResult("This account is already an administrator account.");
            }
            return ResponseGenerator.genSuccessResult(userService.updateUserRole(user.getUserId(), UserRole.ADMIN));
        } else {
            return ResponseGenerator.genFailResult("User does not exist");
        }

    }

    @ApiOperation("revoke administrator privileges")
    @PostMapping("revokeAdmin")
    public Response revokeAdmin(HttpServletRequest request, HttpServletResponse response,
                                @ApiParam(name = "email", value = "email", required = true)
                                @RequestBody Map<String, String> body) {
        String message = checkTokenOfSuperAdmin(request, response);
        if (!"Success".equals(message)) {
            return ResponseGenerator.genFailResult(message);
        }
        String email = body.get("email");
        if (userService.emailExists(email)) {
            User user = userService.getUserByEmail(email);
            if (user.getUserRole().equals(UserRole.VISITOR)) {
                return ResponseGenerator.genFailResult("This account is already an visitor account.");
            }
            if (user.getUserRole().equals(UserRole.SUPER_ADMIN)) {
                return ResponseGenerator.genFailResult("This account is the super administrator, can not be revoked");
            }
            return ResponseGenerator.genSuccessResult(userService.updateUserRole(user.getUserId(), UserRole.ADMIN));
        } else {
            return ResponseGenerator.genFailResult("User does not exist");
        }

    }


}


