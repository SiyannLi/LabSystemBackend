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


import javax.mail.MessagingException;
import java.util.*;


@CrossOrigin("*")
@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    public static final HashMap<String, String> emailVerifyCodes = new HashMap<>();
    public static final HashMap<String, String> emailTokens = new HashMap<>();

    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private VerifyCodeService verifyCodeService;


//    @ApiOperation("get one user")
//    @GetMapping("get/{userId}")
//    public Response getUser(@ApiParam(name = "userId", value = "userId", required = true) @PathVariable int userId) {
//        User user = userService.getUser(userId);
//        if (null != user) {
//            return ResponseGenerator.genSuccessResult(user);
//        } else {
//            return ResponseGenerator.genFailResult("user dont exist");
//        }
//    }


    @ApiOperation("send verification code")
    @PostMapping("sendVerificationCode")
    public Response sendVerificationCode(@ApiParam(name = "emailAndInfo", value = "emailAndInfo", required = true)
                                         @Param("email") @RequestBody Map<String, String> body) throws MessagingException {
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
            emailVerifyCodes.put(email, verCode);
            send = false;
            return ResponseGenerator.genSuccessResult(message);
        }
        return ResponseGenerator.genFailResult(message);
    }


//    @ApiOperation("insert one user")
//    @PostMapping("/insertUser")
//    public Response insertUser(@ApiParam(name = "user", value = "user", required = true) @Param("user") @RequestParam User user) {
//        if (userService.insertUser(user) > 0) {
//            return ResponseGenerator.genSuccessResult();
//        } else {
//            return ResponseGenerator.genFailResult("fail to insert user");
//        }
//    }


    @ApiOperation("visitor login")
    @PostMapping("visitorLogin")
    public Response visitorLogin(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        logger.debug("visitorLogin");
        logger.debug("email " + email);
        logger.debug("password" + password);
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
        emailTokens.put(email, token);
        return ResponseGenerator.genSuccessResult(token, email, JwtUtil.getUserInfo(token, "firstName")
                , JwtUtil.getUserInfo(token, "lastName"));

    }

    @ApiOperation("admin login")
    @PostMapping("adminLogin")
    public Response adminLogin(@ApiParam(name = "emailAndPass", value = "emailAndPass", required = true)
                               @RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        logger.info(email);
        logger.info(password);
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
        emailTokens.put(email, token);
        return ResponseGenerator.genSuccessResult(token, email, JwtUtil.getUserInfo(token, "firstName")
                , JwtUtil.getUserInfo(token, "lastName"));
    }


    @PostMapping("logout")
    public Response logout(@RequestHeader("Authorization") String token,
                           @ApiParam(name = "emailAndPass", value = "emailAndPass", required = true)
                           @RequestBody Map<String, String> body) {
        String email = body.get("email");
//        String tokenServer = emailTokens.get(email);
//        if (token == null) {
//            if (tokenServer == null) {
//                return ResponseGenerator.genFailResult("not logged in");
//            } else {
//                emailTokens.remove(email);
//                emailVerifyCodes.remove(email);
//                return ResponseGenerator.genFailResult("wrong token");
//            }
//        }
//        if (!token.equals(tokenServer) || !JwtUtil.verify(token)) {
//            emailTokens.remove(email);
//            emailVerifyCodes.remove(email);
//            return ResponseGenerator.genFailResult("wrong token");
//        }
        emailTokens.remove(email);
        emailVerifyCodes.remove(email);
        return ResponseGenerator.genSuccessResult("logout success");

    }

    @ApiOperation("register one account")
    @PostMapping("register")
    public Response register(@ApiParam(name = "email", value = "email", required = true)
                             @RequestBody Map<String, String> body) throws MessagingException {
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
        String verifyCode = emailVerifyCodes.get(email);
        logger.info("VerCode" + verifyCode);
        logger.info("verificationCode2 " + verifyCode);
        if (userService.emailExists(email)) {
            return ResponseGenerator.genFailResult("This email has been registered.");
        }
        String userName = firstName + " " + lastName;
        notificationService.sendNotificationByTemplate(email, NotificationTemplate.RESISTER_CONFIRMING
                , userName);
        userService.register(email, password, firstName, lastName, verificationCode);
        return ResponseGenerator.genSuccessResult();
       /* if (verificationCode.equals(verifyCode)) {
            String userName = firstName + " " + lastName;
            notificationService.sendNotificationByTemplate(email, NotificationTemplate.RESISTER_CONFIRMING
                    , userName);
            userService.register(email, password, firstName, lastName, verificationCode);
            emailVerifyCodes.remove(email);
            return ResponseGenerator.genSuccessResult();
        } else {
            return ResponseGenerator.genFailResult("Invalid verification code");
        }*/
    }

    @ApiOperation("reset password")
    @PostMapping("resetPassword")
    public Response resetPassword(@ApiParam(name = "email", value = "email", required = true)
                                  @RequestBody Map<String, String> body) throws MessagingException {
        String email = body.get("email");
        String newPassword = body.get("userPassword");
        String verCode = body.get("verifyCode");

        logger.info("email " + email);
        logger.info("password " + newPassword);
        logger.info("verificationCode " + verCode);


        if (verCode.equals(emailVerifyCodes.get(email))) {
            User user = userService.getUserByEmail(email);
            notificationService.sendNotificationByTemplate(email, NotificationTemplate.CHANGE_PASSWORD_SUCCESS
                    , user.getFullName());
            emailTokens.remove(email);
            emailVerifyCodes.remove(email);
            logger.info(newPassword);
            return ResponseGenerator.genSuccessResult(userService.resetPassword(email, newPassword));
        } else {
            return ResponseGenerator.genFailResult("Invalid verification code");
        }

    }

    private Response checkTokenOfSuperAdmin(String token, String email) {
        Response response = checkTokenOfAdmin(token, email);
        //String message = response.getMessage();
//        if ("Not an administrator account".equals(message)) {
//            response = ResponseGenerator.genFailResult(response.getToken(), email
//                    , "Not an super administrator account");
//        }
//        if ("Success".equals(message)) {
//            boolean isSuperAdmin = JwtUtil.getUserInfo(token, "role").equals("super admin");
//            if (!isSuperAdmin) {
//                response = ResponseGenerator.genFailResult(response.getToken(), email
//                        , "Not an super administrator account");
//            }
//        }
        return response;
    }


    private Response checkTokenOfAdmin(String token, String email) {
        Response response = checkTokenOfUser(token, email);
//        String message = response.getMessage();
//        if ("Success".equals(message) || "Not an active account".equals(message)) {
//            boolean isAdmin = !JwtUtil.getUserInfo(token, "role").equals("visitor");
//            if (!isAdmin) {
//                response = ResponseGenerator.genFailResult(response.getToken(), email
//                        , "Not an administrator account");
//            }
//            response = ResponseGenerator.genSuccessResult(response.getToken(), email, "Success");
//        }
        return response;
    }

    private Response checkTokenOfUser(String token, String email) {
        //       String tokenServer = emailTokens.get(email);
//        if (token == null) {
//            if (tokenServer == null) {
//                return ResponseGenerator.genFailResult("not logged in");
//            } else {
//                emailTokens.remove(email);
//                emailVerifyCodes.remove(email);
//                return ResponseGenerator.genFailResult("wrong token");
//            }
//        }
//        if (!token.equals(tokenServer) || !JwtUtil.verify(token)) {
//            emailTokens.remove(email);
//            emailVerifyCodes.remove(email);
//            return ResponseGenerator.genFailResult("wrong token");
//        }
//        boolean isUserActive = JwtUtil.getUserInfo(token, "status").equals("active");
//        if (!isUserActive) {
//            return ResponseGenerator.genFailResult(tokenServer, email, "Not an active account");
//        }
        return ResponseGenerator.genSuccessResult(token/*Server*/, email, "Success");
    }

    @ApiOperation("get a list of all accounts to be confirmed")
    @PostMapping("getAllAccountToBeConfirmed")
    public Response getAllAccountToBeConfirmed(@RequestHeader("Authorization") String token
            , @ApiParam(name = "emailAndPass", value = "emailAndPass", required = true)
                                               @RequestBody Map<String, String> body) {
        String email = body.get("email");
        Response response = checkTokenOfAdmin(token, email);
//        String message = response.getMessage();
//        if (!"Success".equals(message)) {
//            return response;
//        }
        List<User> users = userService.getAllAccountToBeConfirmed();
        if (!users.isEmpty()) {
            List<Map<String, String>> usersInfo = new ArrayList<>();
            for (User user : users) {
                int idx = users.indexOf(user);
                usersInfo.add(new HashMap<>());
                usersInfo.get(idx).put("firstName", user.getFirstName());
                usersInfo.get(idx).put("lastName", user.getLastName());
                usersInfo.get(idx).put("email", user.getEmail());

            }
            response.setData(usersInfo);
            return response;
        } else {
            return ResponseGenerator.genFailResult(response.getToken(), email, "No users to be confirmed");

        }
    }

    @ApiOperation("confirm the registration of user to create a new account")
    @PostMapping("confirmUserRegistration")
    public Response confirmUserRegistration(@RequestHeader("Authorization") String token,
                                            @ApiParam(name = "email", value = "email", required = true)
                                            @RequestBody Map<String, String> body) throws MessagingException {
        String operatorEmail = body.get("operatorEmail");
        String email = body.get("email");

        logger.info("email :" + email);

        Response response = checkTokenOfAdmin(token, operatorEmail);
//        String message = response.getMessage();
//        if (!"Success".equals(message)) {
//            return response;
//        }
        if (userService.emailExists(email)) {
            User user = userService.getUserByEmail(email);
            notificationService.sendNotificationByTemplate(email, NotificationTemplate.REGISTER_SUCCESS
                    , user.getFullName());
            userService.confirmUserRegistration(user.getUserId());
           return response;
        } else {
            return ResponseGenerator.genFailResult(response.getToken(), operatorEmail, "User does not exist");
        }
    }

    @ApiOperation("reject the application of user to create a new account")
    @PostMapping("rejectUserRegistration")
    public Response rejectUserRegistration(@RequestHeader("Authorization") String token,
                                           @ApiParam(name = "email", value = "email", required = true)
                                           @RequestBody Map<String, String> body) throws MessagingException {
        String operatorEmail = body.get("operatorEmail");
        String email = body.get("email");

        logger.info("email :" + email);

        Response response = checkTokenOfAdmin(token, operatorEmail);
//        String message = response.getMessage();
//        if (!"Success".equals(message)) {
//            return response;
//        }
        if (userService.emailExists(email)) {
            User user = userService.getUserByEmail(email);
            notificationService.sendNotificationByTemplate(email, NotificationTemplate.REGISTER_FAIL
                    , user.getFullName());
            userService.rejectUserRegistration(user.getUserId());
            return response;
        } else {
            return ResponseGenerator.genFailResult(response.getToken(), operatorEmail, "User does not exist");
        }

    }


//    @ApiOperation("change username")
//    @PostMapping("changeUserName")
//    public Response changeUserName(int userId, String newFirstName, String newLastName) {
//        return ResponseGenerator.genSuccessResult(userService.changeUserName(userId, newFirstName, newLastName));
//
//    }

    @ApiOperation("reset information of users ")
    @PostMapping("resetUserInfo")
    public Response resetUserInfo(/*@RequestHeader("Authorization") String token,*/
            @ApiParam(name = "nameAndStatus", value = "nameAndStatus", required = true)
            @RequestBody Map<String, String> body) {
        String firstName = body.get("firstName");
        String lastName = body.get("lastName");
        String userStatus = body.get("userStatus");
//        String operatorEmail = body.get("operatorEmail");
        String email = body.get("email");

        logger.info("email " + email);
        logger.info("firstName " + firstName);
        logger.info("lastName " + lastName);
        logger.info("status " + userStatus);

        if (userService.emailExists(email)) {
            User user = userService.getUserByEmail(email);
            int userId = user.getUserId();
            userService.updateName(userId, firstName, lastName);
            switch (userStatus.toLowerCase()) {
                case "active":
                    return ResponseGenerator.genSuccessResult(userService.activateUser(userId));
                case "inactive":
                    return ResponseGenerator.genSuccessResult(userService.deactivateUser(userId));
                default:
                    return ResponseGenerator.genFailResult("Input error");

            }
        } else {
            return ResponseGenerator.genFailResult("User does not exist");
        }

    }


    @ApiOperation("get all users")
    @PostMapping("getAllUsers")
    public Response getAllUsers(@RequestHeader("Authorization") String token,
                                @ApiParam(name = "email", value = "email", required = true)
                                @RequestBody Map<String, String> body) {
        String email = body.get("email");
        Response response = checkTokenOfAdmin(token, email);
//        String message = response.getMessage();
//        if (!"Success".equals(message)) {
//            return response;
//        }
        List<User> users = userService.getAllUsers();
        List<Map<String, String>> usersInfo = new ArrayList<>();
        for (User user : users) {
            int idx = users.indexOf(user);
            usersInfo.add(new HashMap<>());
            usersInfo.get(idx).put("firstName", user.getFirstName());
            usersInfo.get(idx).put("lastName", user.getLastName());
            usersInfo.get(idx).put("email", user.getEmail());
            usersInfo.get(idx).put("userAccountStatus", user.getUserAccountStatus().getStatusValue());

        }
        response.setData(usersInfo);
        return response;

    }

    @ApiOperation("get a list of all admins")
    @PostMapping("getAllAdministrator")
    public Response getAllAdministrator(@RequestHeader("Authorization") String token,
                                        @ApiParam(name = "email", value = "email", required = true)
                                        @RequestBody Map<String, String> body) {
        String email = body.get("email");
        Response response = checkTokenOfSuperAdmin(token, email);
//        String message = response.getMessage();
//        if (!"Success".equals(message)) {
//            return response;
//        }
        List<User> admins = userService.getAllAdministrators();
        List<Map<String, String>> adminsInfo = new ArrayList<>();
        for (User admin : admins) {
            int idx = admins.indexOf(admin);
            adminsInfo.add(new HashMap<>());
            adminsInfo.get(idx).put("firstName", admin.getFirstName());
            adminsInfo.get(idx).put("lastName", admin.getLastName());
            adminsInfo.get(idx).put("email", admin.getEmail());

        }

        response.setData(adminsInfo);
        return response;
    }


    @ApiOperation("insert a admin")
    @PostMapping("insertAdmin")
    public Response insertAdmin(@RequestHeader("Authorization") String token,
                                @ApiParam(name = "email", value = "email", required = true)
                                @RequestBody Map<String, String> body) {
        String operatorEmail = body.get("operatorEmail");
        String email = body.get("email");

        logger.info("email " + operatorEmail);

        Response response = checkTokenOfSuperAdmin(token, operatorEmail);
//        String message = response.getMessage();
//        if (!"Success".equals(message)) {
//            return response;
//        }
        if (userService.emailExists(email)) {
            User user = userService.getUserByEmail(email);
            if (!user.getUserRole().equals(UserRole.VISITOR)) {
                return ResponseGenerator.genFailResult(response.getToken(), operatorEmail
                        , "This account is already an administrator account.");
            }
            userService.updateUserRole(user.getUserId(), UserRole.ADMIN);
            String newToken = JwtUtil.createToken(user);
            emailTokens.put(email, newToken);
            return response;
        } else {
            return ResponseGenerator.genFailResult(response.getToken(), operatorEmail, "User does not exist");
        }

    }

    @ApiOperation("revoke administrator privileges")
    @PostMapping("revokeAdmin")
    public Response revokeAdmin(@RequestHeader("Authorization") String token,
                                @ApiParam(name = "email", value = "email", required = true)
                                @RequestBody Map<String, String> body) {
        String operatorEmail = body.get("operatorEmail");
        String email = body.get("email");

        logger.info("email " + operatorEmail);

        Response response = checkTokenOfSuperAdmin(token, operatorEmail);
//        String message = response.getMessage();
//        if (!"Success".equals(message)) {
//            return response;
//        }
        if (userService.emailExists(email)) {
            User user = userService.getUserByEmail(email);
            if (user.getUserRole().equals(UserRole.VISITOR)) {
                return ResponseGenerator.genFailResult(response.getToken(), operatorEmail
                        , "This account is already an visitor account.");
            }
            if (user.getUserRole().equals(UserRole.SUPER_ADMIN)) {
                return ResponseGenerator.genFailResult(response.getToken(), operatorEmail
                        , "This account is super admin account, can not be revoked");
            }
            userService.updateUserRole(user.getUserId(), UserRole.VISITOR);
            String newToken = JwtUtil.createToken(user);
            emailTokens.put(email, newToken);
            return response;
        } else {
            return ResponseGenerator.genFailResult(response.getToken(), operatorEmail, "User does not exist");
        }

    }


}


