package com.example.LabSystemBackend.controller;


import com.example.LabSystemBackend.common.Response;
import com.example.LabSystemBackend.common.ResponseGenerator;
import com.example.LabSystemBackend.entity.*;
import com.example.LabSystemBackend.jwt.comment.AdminLoginToken;
import com.example.LabSystemBackend.jwt.comment.PassToken;
import com.example.LabSystemBackend.jwt.comment.SuperAdminLoginToken;
import com.example.LabSystemBackend.service.VerifyCodeService;
import com.example.LabSystemBackend.jwt.JwtUtil;
import com.example.LabSystemBackend.service.NotificationService;
import com.example.LabSystemBackend.service.UserService;
import com.example.LabSystemBackend.ui.InputMessage;
import com.example.LabSystemBackend.ui.KeyMessage;
import com.example.LabSystemBackend.ui.NotificationTemplate;
import com.example.LabSystemBackend.ui.OutputMessage;
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
    public static final HashMap<String, String> emailVerifyCodes = new HashMap<>();
    public static final HashMap<String, String> emailTokens = new HashMap<>();


    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private VerifyCodeService verifyCodeService;


    @PassToken
    @ApiOperation("send verification code")
    @PostMapping("sendVerificationCode")
    public Response sendVerificationCode(@ApiParam(name = "emailAndInfo", value = "emailAndInfo", required = true)
                                         @Param("email") @RequestBody Map<String, String> body) {
        String info = body.get(KeyMessage.INFO);
        String email = body.get(KeyMessage.EMAIL);
        boolean userExist = userService.emailExists(email);
        boolean send = false;
        String message = OutputMessage.SUCCEED;
        switch (info) {
            case InputMessage
                    .REGISTER: {
                if (!userExist) {
                    logger.info(email);
                    send = true;
                } else {
                    message = OutputMessage.USER_EXISTS;
                }
                break;
            }
            case InputMessage.CHANGE_PASSWORD: {
                if (userExist) {
                    logger.info(email);
                    send = true;
                } else {
                    message = OutputMessage.USER_NOT_EXISTS;
                }
                break;
            }
            default: {
                message = OutputMessage.ERROR;
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


    @PassToken
    @ApiOperation("visitor login")
    @PostMapping("visitorLogin")
    public Response visitorLogin(@RequestBody Map<String, String> body) {
        String email = body.get(KeyMessage.EMAIL);
        String password = body.get(KeyMessage.PASSWORD);
        logger.debug("visitorLogin: ");
        logger.debug("email: " + email);
        logger.debug("password: " + password);
        if (!userService.emailExists(email)) {
            return ResponseGenerator.genFailResult(OutputMessage.USER_NOT_EXISTS);
        }
        User user = userService.getUserByEmail(email);
        if (!user.getUserAccountStatus().equals(UserAccountStatus.ACTIVE)) {
            return ResponseGenerator.genFailResult(OutputMessage.ACCOUNT_INACTIVE);
        }
        if (!password.equals(user.getUserPassword())) {
            return ResponseGenerator.genFailResult(OutputMessage.INCORRECT_PASSWORD);
        }
        String token = JwtUtil.createToken(user);
        emailTokens.put(email, token);
        logger.info("token: " + token);
        return ResponseGenerator.genSuccessResult(token, user.getFirstName()
                , user.getLastName(), email, true, false);

    }

    @PassToken
    @ApiOperation("admin login")
    @PostMapping("adminLogin")
    public Response adminLogin(@ApiParam(name = "emailAndPass", value = "emailAndPass", required = true)
                               @RequestBody Map<String, String> body) {
        String email = body.get(KeyMessage.EMAIL);
        String password = body.get(KeyMessage.PASSWORD);
        logger.debug("adminLogin: ");
        logger.debug("email: " + email);
        logger.debug("password: " + password);
        if (!userService.emailExists(email)) {
            return ResponseGenerator.genFailResult(OutputMessage.USER_NOT_EXISTS);
        }
        User user = userService.getUserByEmail(email);
        if (user.getUserRole().equals(UserRole.USER)) {
            return ResponseGenerator.genFailResult(OutputMessage.NOT_ADMIN);
        }
        if (!password.equals(user.getUserPassword())) {
            return ResponseGenerator.genFailResult(OutputMessage.INCORRECT_PASSWORD);
        }
        String token = JwtUtil.createToken(user);
        logger.info("token: " + token);
        emailTokens.put(email, token);
        return ResponseGenerator.genSuccessResult(token, user.getFirstName()
                , user.getLastName(), email, false, true);
    }


    @GetMapping("logout")
    public Response logout(@RequestHeader("Authorization") String token) {
        String email = JwtUtil.getUserInfo(token, KeyMessage.EMAIL);
        logger.info("emailServer: " + emailTokens.get(email));
        emailTokens.remove(email);
        logger.info("emailServer2: " + emailTokens.get(email));
        return ResponseGenerator.genSuccessResult(OutputMessage.SUCCEED);

    }

    @PassToken
    @ApiOperation("register one account")
    @PostMapping("register")
    public Response register(@ApiParam(name = "email", value = "email", required = true)
                             @RequestBody Map<String, String> body) {
        String email = body.get(KeyMessage.EMAIL);
        String password = body.get("userPassword");
        String firstName = body.get(KeyMessage.FIRSTNAME);
        String lastName = body.get(KeyMessage.LASTNAME);
        String verificationCode = body.get(KeyMessage.VERIFY_CODE);

        logger.info("email " + email);
        logger.info("password " + password);
        logger.info("firstName " + firstName);
        logger.info("lastName " + lastName);
        logger.info("verificationCode " + verificationCode);
        String verifyCode = emailVerifyCodes.get(email);
        logger.info("VerCode" + verifyCode);
        if (userService.emailExists(email)) {
            return ResponseGenerator.genFailResult(OutputMessage.USER_EXISTS);
        }
        if (verificationCode.equals(verifyCode)) {
            String userName = firstName + OutputMessage.BLANK + lastName;
            userService.register(email, password, firstName, lastName);
            emailVerifyCodes.remove(email);
            notificationService.sendNotificationByTemplate(email, NotificationTemplate.RESISTER_FINISHED
                    , userName);
            List<User> admins = userService.getAllAdminReceiveBulkEmail();
            for (User admin : admins) {
                String adminEmail = admin.getEmail();
                String adminName = admin.getFullName();
                notificationService.sendNotificationByTemplate(adminEmail, NotificationTemplate.NEW_REGISTRATION_REQUEST
                        , adminName);
            }
            return ResponseGenerator.genSuccessResult();
        } else {
            return ResponseGenerator.genFailResult("Invalid verification code");
        }
    }

    @PassToken
    @ApiOperation("reset password")
    @PostMapping("resetPassword")
    public Response resetPassword(@ApiParam(name = "email", value = "email", required = true)
                                  @RequestBody Map<String, String> body) {
        String email = body.get("email");
        String newPassword = body.get("userPassword");
        String verCode = body.get("verifyCode");

        logger.info("email " + email);
        logger.info("password " + newPassword);
        logger.info("verificationCode " + verCode);


        if (verCode.equals(emailVerifyCodes.get(email))) {
            User user = userService.getUserByEmail(email);
            notificationService.sendNotificationByTemplate(email, NotificationTemplate.PASSWORD_RESET_SUCCESS
                    , user.getFullName());
            emailVerifyCodes.remove(email);
            emailTokens.remove(email);
            logger.info(newPassword);
            return ResponseGenerator.genSuccessResult(userService.resetPassword(email, newPassword));
        } else {
            return ResponseGenerator.genFailResult("Invalid verification code");
        }

    }


    @AdminLoginToken
    @ApiOperation("get a list of all accounts to be confirmed")
    @GetMapping("getAllAccountToBeConfirmed")
    public Response getAllAccountToBeConfirmed(@RequestHeader("Authorization") String token) {
        String opEmail = JwtUtil.getUserInfo(token, "email");
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
            return ResponseGenerator.genSuccessResult(emailTokens.get(opEmail), usersInfo);
        } else {
            return ResponseGenerator.genFailResult(emailTokens.get(opEmail), "No users to be confirmed");

        }
    }

    @AdminLoginToken
    @ApiOperation("confirm the registration of user to create a new account")
    @PostMapping("confirmUserRegistration")
    public Response confirmUserRegistration(@RequestHeader("Authorization") String token,
            @ApiParam(name = "email", value = "email", required = true)
            @RequestBody Map<String, String> body) {
        String email = body.get("email");
        logger.info("email :" + email);
        String opEmail = JwtUtil.getUserInfo(token, "email");
        if (userService.emailExists(email)) {
            User user = userService.getUserByEmail(email);
            if (!UserAccountStatus.PENDING.equals(user.getUserAccountStatus())) {
                return ResponseGenerator.genFailResult(emailTokens.get(opEmail), "User status is not pending");
            }
            notificationService.sendNotificationByTemplate(email, NotificationTemplate.REGISTER_SUCCESS
                    , user.getFullName());
            return ResponseGenerator.genSuccessResult(emailTokens.get(opEmail), userService.confirmUserRegistration(user.getUserId()));
        } else {
            return ResponseGenerator.genFailResult(emailTokens.get(opEmail), "User does not exist");
        }
    }

    @AdminLoginToken
    @ApiOperation("reject the application of user to create a new account")
    @PostMapping("rejectUserRegistration")
    public Response rejectUserRegistration(@RequestHeader("Authorization") String token,
                                           @ApiParam(name = "email", value = "email", required = true)
                                           @RequestBody Map<String, String> body) {
        String email = body.get("email");

        logger.info("email :" + email);

        String opEmail = JwtUtil.getUserInfo(token, "email");
        if (userService.emailExists(email)) {
            User user = userService.getUserByEmail(email);
            if (!UserAccountStatus.PENDING.equals(user.getUserAccountStatus())) {
                return ResponseGenerator.genFailResult(emailTokens.get(opEmail), "User status is not pending");
            }
            notificationService.sendNotificationByTemplate(email, NotificationTemplate.REGISTER_REJECTED
                    , user.getFullName(), opEmail);
            return ResponseGenerator.genSuccessResult(emailTokens.get(opEmail), userService.rejectUserRegistration(user.getUserId()));
        } else {
            return ResponseGenerator.genFailResult(emailTokens.get(opEmail), "User does not exist");
        }

    }


    @AdminLoginToken
    @ApiOperation("reset information of users ")
    @PostMapping("resetUserInfo")
    public Response resetUserInfo(@RequestHeader("Authorization") String token,
                                  @ApiParam(name = "nameAndStatus", value = "nameAndStatus", required = true)
                                  @RequestBody Map<String, String> body) {
        String firstName = body.get("firstName");
        String lastName = body.get("lastName");
        String userStatus = body.get("userStatus");
        String email = body.get("email");
        String opEmail = JwtUtil.getUserInfo(token, "email");

        logger.info("email " + email);
        logger.info("opEmail " + opEmail);
        logger.info("firstName " + firstName);
        logger.info("lastName " + lastName);
        logger.info("status " + userStatus);

        if (userService.emailExists(email)) {
            User user = userService.getUserByEmail(email);
            int userId = user.getUserId();
            userService.updateName(userId, firstName, lastName);
            switch (userStatus.toLowerCase()) {
                case "active":
                    userService.activateUser(userId);
                    break;
                case "inactive":
                    userService.deactivateUser(userId);
                    break;
                default:
                    return ResponseGenerator.genFailResult(emailTokens.get(opEmail), "Input error");
            }
            notificationService.sendNotificationByTemplate(email, NotificationTemplate.INFO_CHANGED
                    , user.getFullName(), opEmail);
            return ResponseGenerator.genSuccessResult(emailTokens.get(opEmail),"SUCCEED");
        } else {
            return ResponseGenerator.genFailResult(emailTokens.get(opEmail), "User does not exist");
        }

    }


    @AdminLoginToken
    @ApiOperation("get all users")
    @GetMapping("getAllUsers")
    public Response getAllUsers(@RequestHeader("Authorization") String token) {
        String opEmail = JwtUtil.getUserInfo(token, "email");
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
        return ResponseGenerator.genSuccessResult(emailTokens.get(opEmail), usersInfo);

    }

    @SuperAdminLoginToken
    @ApiOperation("get a list of all admins")
    @GetMapping("getAllAdministrator")
    public Response getAllAdministrator(@RequestHeader("Authorization") String token) {
        String opEmail = JwtUtil.getUserInfo(token, "email");
        List<User> admins = userService.getAllAdministrators();
        List<Map<String, String>> adminsInfo = new ArrayList<>();
        for (User admin : admins) {
            int idx = admins.indexOf(admin);
            adminsInfo.add(new HashMap<>());
            adminsInfo.get(idx).put("firstName", admin.getFirstName());
            adminsInfo.get(idx).put("lastName", admin.getLastName());
            adminsInfo.get(idx).put("email", admin.getEmail());

        }
        return ResponseGenerator.genSuccessResult(emailTokens.get(opEmail), adminsInfo);
    }


    @SuperAdminLoginToken
    @ApiOperation("insert a admin")
    @PostMapping("insertAdmin")
    public Response insertAdmin(@RequestHeader("Authorization") String token,
            @ApiParam(name = "email", value = "email", required = true)
            @RequestBody Map<String, String> body) {
        String email = body.get("email");
        String opEmail = JwtUtil.getUserInfo(token, "email");
        logger.info("email " + email);
        logger.info("opEmail " + opEmail);
        if (userService.emailExists(email)) {
            User user = userService.getUserByEmail(email);
            if (!user.getUserRole().equals(UserRole.USER)) {
                return ResponseGenerator.genFailResult(emailTokens.get(opEmail)
                        , "This account is already an administrator account.");
            }
            userService.updateUserRole(user.getUserId(), UserRole.ADMIN);
            userService.updateAdminEmailSetting(user.getUserId(), true);
            notificationService.sendNotificationByTemplate(email, NotificationTemplate.BECOME_ADMIN
                    , user.getFullName());
            return ResponseGenerator.genSuccessResult(emailTokens.get(opEmail), "SUCCESS");
        } else {
            return ResponseGenerator.genFailResult(emailTokens.get(opEmail), "User does not exist");
        }

    }

    @SuperAdminLoginToken
    @ApiOperation("revoke administrator privileges")
    @PostMapping("revokeAdmin")
    public Response revokeAdmin(@RequestHeader("Authorization") String token,
                                @ApiParam(name = "email", value = "email", required = true)
                                @RequestBody Map<String, String> body) {
        String email = body.get("email");
        String opEmail = JwtUtil.getUserInfo(token, "email");
        logger.info("email " + email);
        logger.info("opEmail " + opEmail);
        if (userService.emailExists(email)) {
            User user = userService.getUserByEmail(email);
            if (user.getUserRole().equals(UserRole.USER)) {
                return ResponseGenerator.genFailResult(emailTokens.get(opEmail)
                        , "This account is already an visitor account.");
            }
            if (user.getUserRole().equals(UserRole.SUPER_ADMIN)) {
                return ResponseGenerator.genFailResult(emailTokens.get(opEmail)
                        , "This account is super admin account, can not be revoked");
            }
            userService.updateUserRole(user.getUserId(), UserRole.USER);
            userService.updateAdminEmailSetting(user.getUserId(), false);
            notificationService.sendNotificationByTemplate(email, NotificationTemplate.ADMIN_ROLE_REVOKED
                    , user.getFullName(), opEmail);
            return ResponseGenerator.genSuccessResult(emailTokens.get(opEmail), "SUCCESS");
        } else {
            return ResponseGenerator.genFailResult(emailTokens.get(opEmail), "User does not exist");
        }

    }

    @AdminLoginToken
    @ApiOperation("Change email settings")
    @GetMapping("changeEmailSetting")
    public Response changeEmailSetting(@RequestHeader("Authorization") String token,
                                       @ApiParam(name = "isReceiveBulkEmail", value = "isReceiveBulkEmail", required = true)
                                       @RequestBody Map<String, String> body) {
        String email = JwtUtil.getUserInfo(token, "email");
        boolean newSetting = Boolean.valueOf(body.get("isReceiveBulkEmail"));
        User admin = userService.getUserByEmail(email);

        userService.updateAdminEmailSetting(admin.getUserId(), newSetting);

        return ResponseGenerator.genSuccessResult(emailTokens.get(email), OutputMessage.SUCCEED);

    }


}


