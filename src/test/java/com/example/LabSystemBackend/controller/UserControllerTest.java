package com.example.LabSystemBackend.controller;

import com.example.LabSystemBackend.entity.Notification;
import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.entity.UserAccountStatus;
import com.example.LabSystemBackend.entity.UserRole;
import com.example.LabSystemBackend.jwt.AuthenticationInterceptor;
import com.example.LabSystemBackend.jwt.JwtUtil;
import com.example.LabSystemBackend.service.NotificationService;
import com.example.LabSystemBackend.service.UserService;
import com.example.LabSystemBackend.service.VerifyCodeService;
import com.example.LabSystemBackend.ui.InputMessage;
import com.example.LabSystemBackend.ui.KeyMessage;
import com.example.LabSystemBackend.ui.NotificationTemplate;
import com.example.LabSystemBackend.ui.OutputMessage;
import com.example.LabSystemBackend.util.DataGenerate;
import com.github.javafaker.Faker;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @version 1.0
 * @author  cong Liu
 *
 * UserController Test
 */
@ActiveProfiles("unittest")
@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private NotificationService notificationService;
    @MockBean
    private VerifyCodeService verifyCodeService;
    @MockBean
    private AuthenticationInterceptor interceptor;


    private String sendVerificationCode(String info, boolean emailExist, int resultCode, String message) {
        Faker faker = new Faker();
        String email = faker.internet().emailAddress();
        String verifyCode = "123456";
        Notification notification = DataGenerate.generateNotification();
        notification.setRecipientId(User.ID_OF_UNREGISTERED);
        notification.setSenderId(User.ID_OF_SYSTEM);
        try {
            Mockito.when(interceptor.preHandle(Mockito.any(HttpServletRequest.class)
                    , Mockito.any(HttpServletResponse.class), Mockito.any(Object.class))).thenReturn(true);
            Mockito.when(userService.emailExists(email)).thenReturn(emailExist);
            Mockito.when(verifyCodeService.getRandomVerCode()).thenReturn(verifyCode);
            Mockito.when(verifyCodeService.sendVerifyCode(email, verifyCode)).thenReturn(notification);
            String body = "{\"" + KeyMessage.INFO + "\":\"" + info + "\",\"" + KeyMessage.EMAIL
                    + "\":\"" + email + "\"}";
            MvcResult mvcResult = mockMvc.perform(
                            MockMvcRequestBuilders.post("/users/sendVerificationCode")
                                    .contentType(MediaType.APPLICATION_JSON).content(body)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(resultCode))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();

        } catch (MessagingException e) {
            Assert.fail("fail to send email");
        } catch (Exception e) {
            Assert.fail("fail to request");
        }
        return email;
    }

    @Test
    void sendVerificationCodeRegSucceed() {
        String email = sendVerificationCode(InputMessage.REGISTER, false, 200, OutputMessage.SUCCEED);
        UserController.emailVerifyCodes.remove(email);
    }

    @Test
    void sendVerificationCodePasswordSucceed() {
        String email = sendVerificationCode(InputMessage.CHANGE_PASSWORD, true, 200
                , OutputMessage.SUCCEED);
        UserController.emailVerifyCodes.remove(email);
    }

    @Test
    void sendVerificationCodeRegEmailFail() {
        sendVerificationCode(InputMessage.REGISTER, true, 500, OutputMessage.USER_EXISTS);
    }

    @Test
    void sendVerificationCodePasswordEmailFail() {
        sendVerificationCode(InputMessage.CHANGE_PASSWORD, false, 500
                , OutputMessage.USER_NOT_EXISTS);
    }

    @Test
    void sendVerificationCodeInfoFail() {
        Faker faker = new Faker();
        String input = faker.country().capital();
        int num = new Random().nextInt(2);
        sendVerificationCode(input, num == 1, 500, OutputMessage.ERROR);
    }


    private void loginFail(String url, String body, String message) {
        try {
            Mockito.when(interceptor.preHandle(Mockito.any(HttpServletRequest.class)
                    , Mockito.any(HttpServletResponse.class), Mockito.any(Object.class))).thenReturn(true);
            MvcResult mvcResult = mockMvc.perform(
                            MockMvcRequestBuilders.post(url)
                                    .contentType(MediaType.APPLICATION_JSON).content(body)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(500))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.token").doesNotExist())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message))

                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        } catch (Exception e) {
            Assert.fail("fail to request");
        }
    }

    @Test
    void visitorLoginSucceed() {
        User user = DataGenerate.generateUser();
        int id = new Random().nextInt(100) + 1;
        user.setUserId(id);
        user.setUserAccountStatus(UserAccountStatus.ACTIVE);
        Mockito.when(userService.emailExists(user.getEmail())).thenReturn(true);
        Mockito.when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
        String body = "{\"" + KeyMessage.EMAIL + "\":\"" + user.getEmail() + "\",\"" + KeyMessage.PASSWORD
                + "\":\"" + user.getUserPassword() + "\"}";
        try {
            Mockito.when(interceptor.preHandle(Mockito.any(HttpServletRequest.class)
                    , Mockito.any(HttpServletResponse.class), Mockito.any(Object.class))).thenReturn(true);
            MvcResult mvcResult = mockMvc.perform(
                            MockMvcRequestBuilders.post("/users/visitorLogin")
                                    .contentType(MediaType.APPLICATION_JSON).content(body)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(200))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(user.getEmail()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(user.getFirstName()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(user.getLastName()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.userLogged").value(true))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.adminLogged").value(false))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        } catch (Exception e) {
            Assert.fail("fail to request");
        }
        UserController.emailTokens.remove(user.getEmail());


    }

    @Test
    void visitorLoginStatusFail() {
        User user = DataGenerate.generateUser();
        int id = new Random().nextInt(100) + 1;
        user.setUserId(id);
        UserAccountStatus[] status = {UserAccountStatus.INACTIVE, UserAccountStatus.PENDING};
        int idx = new Random().nextInt(2);
        user.setUserAccountStatus(status[idx]);
        Mockito.when(userService.emailExists(user.getEmail())).thenReturn(true);
        Mockito.when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
        String body = "{\"" + KeyMessage.EMAIL + "\":\"" + user.getEmail() + "\",\"" + KeyMessage.PASSWORD
                + "\":\"" + user.getUserPassword() + "\"}";
        loginFail("/users/visitorLogin", body, OutputMessage.ACCOUNT_INACTIVE);


    }

    @Test
    void visitorLoginPasswordFail() {
        User user = DataGenerate.generateUser();
        int id = new Random().nextInt(100) + 1;
        user.setUserId(id);
        user.setUserAccountStatus(UserAccountStatus.ACTIVE);
        Mockito.when(userService.emailExists(user.getEmail())).thenReturn(true);
        Mockito.when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
        String body = "{\"" + KeyMessage.EMAIL + "\":\"" + user.getEmail() + "\",\"" + KeyMessage.PASSWORD
                + "\":\"" + user.getUserPassword() + "123456\"}";
        loginFail("/users/visitorLogin", body, OutputMessage.INCORRECT_PASSWORD);

    }

    @Test
    void visitorLoginEmailFail() {
        Faker faker = new Faker();
        String email = faker.internet().emailAddress();
        Mockito.when(userService.emailExists(email)).thenReturn(false);
        String body = "{\"" + KeyMessage.EMAIL + "\":\"" + email + "\",\"" + KeyMessage.PASSWORD
                + "\":\"123456\"}";
        loginFail("/users/visitorLogin", body, OutputMessage.USER_NOT_EXISTS);

    }


    @Test
    void adminLoginSucceed() {
        User user = DataGenerate.generateUser();
        int id = new Random().nextInt(100) + 1;
        user.setUserId(id);
        UserRole[] roles = {UserRole.SUPER_ADMIN, UserRole.ADMIN};
        user.setUserRole(roles[new Random().nextInt(2)]);
        Mockito.when(userService.emailExists(user.getEmail())).thenReturn(true);
        Mockito.when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
        String body = "{\"" + KeyMessage.EMAIL + "\":\"" + user.getEmail() + "\",\"" + KeyMessage.PASSWORD
                + "\":\"" + user.getUserPassword() + "\"}";
        try {
            Mockito.when(interceptor.preHandle(Mockito.any(HttpServletRequest.class)
                    , Mockito.any(HttpServletResponse.class), Mockito.any(Object.class))).thenReturn(true);
            MvcResult mvcResult = mockMvc.perform(
                            MockMvcRequestBuilders.post("/users/adminLogin")
                                    .contentType(MediaType.APPLICATION_JSON).content(body)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(200))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(user.getEmail()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(user.getFirstName()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(user.getLastName()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.userLogged").value(false))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.adminLogged").value(true))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        } catch (Exception e) {
            Assert.fail("fail to request");
        }
        UserController.emailTokens.remove(user.getEmail());


    }

    @Test
    void adminLoginRoleFail() {
        User user = DataGenerate.generateUser();
        int id = new Random().nextInt(100) + 1;
        user.setUserId(id);
        user.setUserRole(UserRole.USER);
        Mockito.when(userService.emailExists(user.getEmail())).thenReturn(true);
        Mockito.when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
        String body = "{\"" + KeyMessage.EMAIL + "\":\"" + user.getEmail() + "\",\"" + KeyMessage.PASSWORD
                + "\":\"" + user.getUserPassword() + "\"}";
        loginFail("/users/adminLogin", body, OutputMessage.NOT_ADMIN);


    }

    @Test
    void adminLoginPasswordFail() {
        User user = DataGenerate.generateUser();
        int id = new Random().nextInt(100) + 1;
        user.setUserId(id);
        UserRole[] roles = {UserRole.SUPER_ADMIN, UserRole.ADMIN};
        user.setUserRole(roles[new Random().nextInt(2)]);
        Mockito.when(userService.emailExists(user.getEmail())).thenReturn(true);
        Mockito.when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
        String body = "{\"" + KeyMessage.EMAIL + "\":\"" + user.getEmail() + "\",\"" + KeyMessage.PASSWORD
                + "\":\"" + user.getUserPassword() + "123456\"}";
        loginFail("/users/adminLogin", body, OutputMessage.INCORRECT_PASSWORD);

    }

    @Test
    void adminLoginEmailFail() {
        Faker faker = new Faker();
        String email = faker.internet().emailAddress();
        Mockito.when(userService.emailExists(email)).thenReturn(false);
        String body = "{\"" + KeyMessage.EMAIL + "\":\"" + email + "\",\"" + KeyMessage.PASSWORD
                + "\":\"123456\"}";
        loginFail("/users/adminLogin", body, OutputMessage.USER_NOT_EXISTS);
    }

    @Test
    void logout() {
        User user = DataGenerate.generateUser();
        int id = new Random().nextInt(100) + 1;
        user.setUserId(id);
        String token = JwtUtil.createToken(user);
        try {
            Mockito.when(interceptor.preHandle(Mockito.any(HttpServletRequest.class)
                    , Mockito.any(HttpServletResponse.class), Mockito.any(Object.class))).thenReturn(true);
            MvcResult mvcResult = mockMvc.perform(
                            MockMvcRequestBuilders.get("/users/logout")
                                    .header(KeyMessage.TOKEN, token)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(200))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(OutputMessage.SUCCEED))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        } catch (Exception e) {
            Assert.fail("fail to request");
        }
    }

    private void register(String verCodeInput, String verCode, boolean isEmailExist, int resultCode, String message) {
        Faker faker = new Faker();
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        if (verCode != null) {
            UserController.emailVerifyCodes.put(email, verCode);
        }
        try {
            Mockito.when(interceptor.preHandle(Mockito.any(HttpServletRequest.class)
                    , Mockito.any(HttpServletResponse.class), Mockito.any(Object.class))).thenReturn(true);
            Mockito.when(userService.emailExists(email)).thenReturn(isEmailExist);
            Mockito.when(verifyCodeService.checkVerifyCode(verCode, verCodeInput)).thenReturn(verCodeInput.equals(verCode));
            Mockito.when(userService.register(email, password, firstName, lastName)).thenReturn(1);
            String userName = firstName + OutputMessage.BLANK + lastName;
            Mockito.when(notificationService.sendNotificationByTemplate(email, NotificationTemplate.RESISTER_FINISHED
                    , userName)).thenReturn(1);
            Mockito.when(notificationService.sendToAllAdmin(NotificationTemplate.NEW_REGISTRATION_REQUEST)).thenReturn(1);
            String body = "{\"" + KeyMessage.EMAIL + "\":\"" + email + "\",\"" + KeyMessage.PASSWORD
                    + "\":\"" + password + "\",\"" + KeyMessage.FIRSTNAME + "\":\"" + firstName + "\",\""
                    + KeyMessage.LASTNAME + "\":\"" + lastName + "\",\"" + KeyMessage.VERIFY_CODE
                    + "\":\"" + verCodeInput + "\"}";
            MvcResult mvcResult = mockMvc.perform(
                            MockMvcRequestBuilders.post("/users/register")
                                    .contentType(MediaType.APPLICATION_JSON).content(body)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(resultCode))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        } catch (Exception e) {
            Assert.fail("fail to request");
        }
        UserController.emailVerifyCodes.remove(email);

    }

    @Test
    void registerSucceed() {
        Faker faker = new Faker();
        String verificationCode = String.valueOf(faker.number().randomNumber(6, true));
        register(verificationCode, verificationCode, false, 200, OutputMessage.SUCCEED);

    }

    @Test
    void registerEmailFail() {
        Faker faker = new Faker();
        String verificationCode = String.valueOf(faker.number().randomNumber(6, true));
        register(verificationCode, verificationCode, true, 500, OutputMessage.USER_EXISTS);

    }

    @Test
    void registerVerCodeFail() {
        Faker faker = new Faker();
        String verCodeInput = String.valueOf(faker.number().randomNumber(6, true));
        String verificationCode = String.valueOf(faker.number().randomNumber(6, true));
        register(verCodeInput, verificationCode, false, 500, OutputMessage.INVALID_VERIFY_CODE);

    }

    @Test
    void registerVerCodeNull() {
        Faker faker = new Faker();
        String verCodeInput = String.valueOf(faker.number().randomNumber(6, true));
        register(verCodeInput, null, false, 500, OutputMessage.INVALID_VERIFY_CODE);

    }

    private void resetPassword(String verCodeInput, String verCode, boolean isEmailExist, int resultCode, String message) {
        Faker faker = new Faker();
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        if (verCode != null) {
            UserController.emailVerifyCodes.put(email, verCode);
        }
        try {
            Mockito.when(interceptor.preHandle(Mockito.any(HttpServletRequest.class)
                    , Mockito.any(HttpServletResponse.class), Mockito.any(Object.class))).thenReturn(true);
            Mockito.when(userService.emailExists(email)).thenReturn(isEmailExist);
            Mockito.when(verifyCodeService.checkVerifyCode(verCode, verCodeInput)).thenReturn(verCodeInput.equals(verCode));
            if (isEmailExist) {
                User user = DataGenerate.generateUser();
                user.setUserId(new Random().nextInt(100) + 1);
                user.setEmail(email);
                Mockito.when(userService.getUserByEmail(email)).thenReturn(user);
                Mockito.when(notificationService.sendNotificationByTemplate(email, NotificationTemplate.PASSWORD_RESET_SUCCESS
                        , user.getFullName())).thenReturn(1);
                Mockito.when(userService.resetPassword(email, password)).thenReturn(1);
            }
            String body = "{\"" + KeyMessage.EMAIL + "\":\"" + email + "\",\"" + KeyMessage.USER_PASSWORD
                    + "\":\"" + password + "\",\"" + KeyMessage.VERIFY_CODE
                    + "\":\"" + verCodeInput + "\"}";
            MvcResult mvcResult = mockMvc.perform(
                            MockMvcRequestBuilders.post("/users/resetPassword")
                                    .contentType(MediaType.APPLICATION_JSON).content(body)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(resultCode))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        } catch (Exception e) {
            Assert.fail("fail to request");
        }
        UserController.emailVerifyCodes.remove(email);

    }


    @Test
    void resetPasswordSucceed() {
        Faker faker = new Faker();
        String verificationCode = String.valueOf(faker.number().randomNumber(6, true));
        resetPassword(verificationCode, verificationCode, true, 200, OutputMessage.SUCCEED);
    }

    @Test
    void resetPasswordEmailFail() {
        Faker faker = new Faker();
        String verificationCode = String.valueOf(faker.number().randomNumber(6, true));
        resetPassword(verificationCode, verificationCode, false, 500, OutputMessage.USER_NOT_EXISTS);

    }

    @Test
    void resetPasswordVerCodeFail() {
        Faker faker = new Faker();
        String verCodeInput = String.valueOf(faker.number().randomNumber(6, true));
        String verificationCode = String.valueOf(faker.number().randomNumber(6, true));
        resetPassword(verCodeInput, verificationCode, true, 500, OutputMessage.INVALID_VERIFY_CODE);

    }

    @Test
    void resetPasswordVerCodeNull() {
        Faker faker = new Faker();
        String verCodeInput = String.valueOf(faker.number().randomNumber(6, true));
        resetPassword(verCodeInput, null, true, 500, OutputMessage.INVALID_VERIFY_CODE);

    }

    private void checkUserRegistrationHandle(String url, boolean isEmailExist, boolean isPending, int resultCode
            , String message, Object data) {
        try {
            User opUser = DataGenerate.generateUser();
            String token = JwtUtil.createToken(opUser);
            UserController.emailTokens.put(opUser.getEmail(), token);
            Mockito.when(interceptor.preHandle(Mockito.any(HttpServletRequest.class)
                    , Mockito.any(HttpServletResponse.class), Mockito.any(Object.class))).thenReturn(true);
            Faker faker = new Faker();
            String email = faker.internet().emailAddress();
            Mockito.when(userService.emailExists(email)).thenReturn(isEmailExist);
            if (isEmailExist) {
                User user = DataGenerate.generateUser();
                user.setEmail(email);
                user.setUserId(new Random().nextInt(100) + 1);
                if (isPending) {
                    user.setUserAccountStatus(UserAccountStatus.PENDING);
                    Mockito.when(notificationService.sendNotificationByTemplate(email, NotificationTemplate.REGISTER_SUCCESS
                            , user.getFullName())).thenReturn(1);
                    Mockito.when(notificationService.sendNotificationByTemplate(email
                            , NotificationTemplate.REGISTER_REJECTED
                            , user.getFullName(), opUser.getEmail())).thenReturn(1);
                    Mockito.when(userService.confirmUserRegistration(user.getUserId())).thenReturn(1);
                    Mockito.when(userService.rejectUserRegistration(user.getUserId())).thenReturn(1);
                } else {
                    // not pending
                    user.setUserAccountStatus(Arrays.stream(UserAccountStatus.values())
                            .filter(e -> !e.equals(UserAccountStatus.PENDING)).findAny().get());
                }
                Mockito.when(userService.getUserByEmail(email)).thenReturn(user);
            }
            String body = "{\"" + KeyMessage.EMAIL + "\":\"" + email + "\"}";
            MvcResult mvcResult = mockMvc.perform(
                            MockMvcRequestBuilders.post(url)
                                    .header(KeyMessage.TOKEN, token)
                                    .contentType(MediaType.APPLICATION_JSON).content(body)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(resultCode))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(data))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            UserController.emailTokens.remove(opUser.getEmail());
        } catch (Exception e) {
            Assert.fail("fail to request");
        }

    }

    @Test
    void confirmUserRegistrationEmailNotExistFail() {
        checkUserRegistrationHandle("/users/confirmUserRegistration", false, false
                , 500, OutputMessage.USER_NOT_EXISTS
                , null);
    }

    @Test
    void confirmUserRegistrationNotPendingFail() {
        checkUserRegistrationHandle("/users/confirmUserRegistration", true, false
                , 500, OutputMessage.USER_NOT_PENDING
                , null);
    }

    @Test
    void confirmUserRegistrationSucceed() {
        checkUserRegistrationHandle("/users/confirmUserRegistration", true, true
                , 200, "SUCCESS"
                , 1);
    }

    @Test
    void rejectUserRegistrationEmailNotExistFail() {
        checkUserRegistrationHandle("/users/rejectUserRegistration", false, false
                , 500, OutputMessage.USER_NOT_EXISTS
                , null);
    }

    @Test
    void rejectUserRegistrationNotPendingFail() {
        checkUserRegistrationHandle("/users/rejectUserRegistration", true, false
                , 500, OutputMessage.USER_NOT_PENDING
                , null);
    }

    @Test
    void rejectUserRegistrationSucceed() {
        checkUserRegistrationHandle("/users/rejectUserRegistration", true, true
                , 200, "SUCCESS"
                , 1);
    }

    private void checkGetUsers(String url, List<User> users) {
        try {
            User admin = DataGenerate.generateUser();
            int id = new Random().nextInt(100) + 1;
            admin.setUserId(id);
            String token = JwtUtil.createToken(admin);
            UserController.emailTokens.put(admin.getEmail(), token);
            Mockito.when(interceptor.preHandle(Mockito.any(HttpServletRequest.class)
                    , Mockito.any(HttpServletResponse.class), Mockito.any(Object.class))).thenReturn(true);
            MvcResult mvcResult = mockMvc.perform(
                            MockMvcRequestBuilders.get(url)
                                    .header(KeyMessage.TOKEN, token)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(200))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()").value(users.size()))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            UserController.emailTokens.remove(admin.getEmail());
        } catch (Exception e) {
            Assert.fail("fail to request");
        }


    }

    @Test
    void getAllAccountToBeConfirmedEmpty() {
        try {
            User admin = DataGenerate.generateUser();
            int id = new Random().nextInt(100) + 1;
            admin.setUserId(id);
            admin.setUserRole(Arrays.stream(UserRole.values()).filter(e -> !e.equals(UserRole.USER)).findAny().get());
            String token = JwtUtil.createToken(admin);
            UserController.emailTokens.put(admin.getEmail(), token);
            Mockito.when(interceptor.preHandle(Mockito.any(HttpServletRequest.class)
                    , Mockito.any(HttpServletResponse.class), Mockito.any(Object.class))).thenReturn(true);
            List<User> users = new ArrayList<>();
            Mockito.when(userService.getAllAccountToBeConfirmed()).thenReturn(users);
            MvcResult mvcResult = mockMvc.perform(
                            MockMvcRequestBuilders.get("/users/getAllAccountToBeConfirmed")
                                    .header(KeyMessage.TOKEN, token)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(200))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(OutputMessage.NO_USER_TO_CONFIRM))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            UserController.emailTokens.remove(admin.getEmail());
        } catch (Exception e) {
            Assert.fail("fail to request");
        }

    }

    @Test
    void getAllAccountToBeConfirmedSucceed() {
        List<User> users = new ArrayList<>();
        int num = new Random().nextInt(10);
        for (int i = 0; i < num; i++) {
            User user = DataGenerate.generateUser();
            users.add(user);
        }
        Mockito.when(userService.getAllAccountToBeConfirmed()).thenReturn(users);
        checkGetUsers("/users/getAllAccountToBeConfirmed", users);

    }

    @Test
    void getAllUsers() {
        List<User> users = new ArrayList<>();
        int num = new Random().nextInt(10);
        for (int i = 0; i < num; i++) {
            User user = DataGenerate.generateUser();
            users.add(user);
        }
        Mockito.when(userService.getAllUsers()).thenReturn(users);
        checkGetUsers("/users/getAllUsers", users);


    }

    @Test
    void getAllAdministrator() {
        List<User> admins = new ArrayList<>();
        int num = new Random().nextInt(10);
        for (int i = 0; i < num; i++) {
            User user = DataGenerate.generateUser();
            admins.add(user);
        }
        Mockito.when(userService.getAllAdministrators()).thenReturn(admins);
        checkGetUsers("/users/getAllAdministrator", admins);
    }

    private void resetUserInfo(boolean isEmailExist, boolean isStatusCorrect, UserAccountStatus status
            , int resultCode, String message) {
        try {
            Assert.assertTrue("Parameters conflict"
                    ,!isStatusCorrect || !status.equals(UserAccountStatus.PENDING));
            User opUser = DataGenerate.generateUser();
            String token = JwtUtil.createToken(opUser);
            UserController.emailTokens.put(opUser.getEmail(), token);
            Mockito.when(interceptor.preHandle(Mockito.any(HttpServletRequest.class)
                    , Mockito.any(HttpServletResponse.class), Mockito.any(Object.class))).thenReturn(true);
            Faker faker = new Faker();
            String email = faker.internet().emailAddress();
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String userStatus;
            if (isStatusCorrect) {
                userStatus = status.name();
            } else {
                userStatus = "Pending";
            }
            Mockito.when(userService.emailExists(email)).thenReturn(isEmailExist);
            if (isEmailExist) {
                User user = DataGenerate.generateUser();
                user.setUserId(new Random().nextInt(100) + 1);
                user.setEmail(email);
                Mockito.when(userService.getUserByEmail(email)).thenReturn(user);
                Mockito.when(userService.updateName(user.getUserId(), firstName, lastName)).thenReturn(1);
                if (isStatusCorrect) {
                    Mockito.when(userService.activateUser(user.getUserId())).thenReturn(1);
                    Mockito.when(userService.deactivateUser(user.getUserId())).thenReturn(1);
                    Mockito.when(notificationService.sendNotificationByTemplate(email, NotificationTemplate.INFO_CHANGED
                            , user.getFullName(), opUser.getEmail())).thenReturn(1);
                }
            }
            String body = "{\"" + KeyMessage.EMAIL + "\":\"" + email + "\",\"" + KeyMessage.FIRSTNAME + "\":\""
                    + firstName + "\",\"" + KeyMessage.LASTNAME + "\":\"" + lastName + "\",\""
                    + KeyMessage.USER_STATUS + "\":\"" + userStatus + "\"}";
            MvcResult mvcResult = mockMvc.perform(
                            MockMvcRequestBuilders.post("/users/resetUserInfo")
                                    .header(KeyMessage.TOKEN, token)
                                    .contentType(MediaType.APPLICATION_JSON).content(body)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(resultCode))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            UserController.emailTokens.remove(opUser.getEmail());
        } catch (Exception e) {
            Assert.fail("fail to request");
        }
    }

    @Test
    void resetUserInfoSucceedInactive() {
        resetUserInfo(true, true, UserAccountStatus.INACTIVE, 200, OutputMessage.SUCCEED);

    }

    @Test
    void resetUserInfoSucceedActive() {
        resetUserInfo(true, true, UserAccountStatus.ACTIVE, 200, OutputMessage.SUCCEED);

    }

    @Test
    void resetUserInfoEmailNotExist() {
        boolean isStatusCorrect = new Random().nextInt(2) == 1;
        UserAccountStatus status = Arrays.stream(UserAccountStatus.values())
                .filter(e -> !e.equals(UserAccountStatus.PENDING)).findAny().get();
        resetUserInfo(false, isStatusCorrect, status, 500, OutputMessage.USER_NOT_EXISTS);
    }

    @Test
    void resetUserInfoEmailStatusIncorrect() {
        UserAccountStatus status = Arrays.stream(UserAccountStatus.values()).findAny().get();
        resetUserInfo(true, false, status, 500, OutputMessage.INPUT_ERROR);
    }

    private void checkAdminHandle(String url, boolean isEmailExist, boolean isAdmin, boolean isSuperAdmin
            , int resultCode, String message) {
        boolean checkInput = !isSuperAdmin || isAdmin;
        Assert.assertTrue("Super admin must be an admin", checkInput);
        try {
            User opUser = DataGenerate.generateUser();
            String token = JwtUtil.createToken(opUser);
            UserController.emailTokens.put(opUser.getEmail(), token);
            Mockito.when(interceptor.preHandle(Mockito.any(HttpServletRequest.class)
                    , Mockito.any(HttpServletResponse.class), Mockito.any(Object.class))).thenReturn(true);
            Faker faker = new Faker();
            String email = faker.internet().emailAddress();
            Mockito.when(userService.emailExists(email)).thenReturn(isEmailExist);
            if (isEmailExist) {
                User user = DataGenerate.generateUser();
                user.setEmail(email);
                user.setUserId(new Random().nextInt(100) + 1);
                if (isSuperAdmin) {
                    user.setUserRole(UserRole.SUPER_ADMIN);
                } else if (isAdmin) {
                    user.setUserRole(UserRole.ADMIN);
                } else {
                    user.setUserRole(UserRole.USER);
                }
                Mockito.when(userService.getUserByEmail(email)).thenReturn(user);
                Mockito.when(userService.updateUserRole(user.getUserId(),UserRole.ADMIN)).thenReturn(1);
                Mockito.when(userService.updateUserRole(user.getUserId(),UserRole.USER)).thenReturn(1);
                Mockito.when(userService.updateAdminEmailSetting(user.getUserId(), true)).thenReturn(1);
                Mockito.when(userService.updateAdminEmailSetting(user.getUserId(), false)).thenReturn(1);
                Mockito.when(notificationService.sendNotificationByTemplate(email, NotificationTemplate.BECOME_ADMIN
                        , user.getFullName())).thenReturn(1);
                Mockito.when(notificationService.sendNotificationByTemplate(email, NotificationTemplate.ADMIN_ROLE_REVOKED
                        , user.getFullName(), opUser.getEmail())).thenReturn(1);
            }
            String body = "{\"" + KeyMessage.EMAIL + "\":\"" + email + "\"}";
            MvcResult mvcResult = mockMvc.perform(
                            MockMvcRequestBuilders.post(url)
                                    .header(KeyMessage.TOKEN, token)
                                    .contentType(MediaType.APPLICATION_JSON).content(body)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(resultCode))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            UserController.emailTokens.remove(opUser.getEmail());

        } catch (Exception e) {
            Assert.fail("fail to request");
        }

    }

    @Test
    void insertAdminSucceed() {
        checkAdminHandle("/users/insertAdmin", true, false, false, 200
                , OutputMessage.SUCCEED);
    }

    @Test
    void insertAdminEmailNotExist() {
        checkAdminHandle("/users/insertAdmin", false, false, false, 500
                , OutputMessage.USER_NOT_EXISTS);
    }

    @Test
    void insertAdminAlreadyAdmin() {
        boolean isSuperAdmin = new Random().nextInt(2) == 1;
        checkAdminHandle("/users/insertAdmin", true, true, isSuperAdmin
                , 500
                , OutputMessage.ALREADY_ADMIN);
    }

    @Test
    void revokeAdminSucceed() {
        checkAdminHandle("/users/revokeAdmin", true, true, false, 200
                , OutputMessage.SUCCEED);
    }

    @Test
    void revokeAdminEmailNotExist() {
        checkAdminHandle("/users/revokeAdmin", false, false, false, 500
                , OutputMessage.USER_NOT_EXISTS);
    }

    @Test
    void revokeAdminAlreadyUser() {
        checkAdminHandle("/users/revokeAdmin", true, false, false
                , 500
                , OutputMessage.ALREADY_USER);
    }

    @Test
    void revokeAdminSuperAdmin() {
        checkAdminHandle("/users/revokeAdmin", true, true, true
                , 500
                , OutputMessage.CANNOT_REVOKE_SUPER_ADMIN);
    }

    @Test
    void changeEmailSetting() {
        try {
            User opUser = DataGenerate.generateUser();
            opUser.setUserId(new Random().nextInt(100) + 1);
            String token = JwtUtil.createToken(opUser);
            UserController.emailTokens.put(opUser.getEmail(), token);
            Mockito.when(interceptor.preHandle(Mockito.any(HttpServletRequest.class)
                    , Mockito.any(HttpServletResponse.class), Mockito.any(Object.class))).thenReturn(true);
            String newSetting = new Random().nextInt(2) == 1 ? "true" : "false";
            Mockito.when(userService.getUserByEmail(opUser.getEmail())).thenReturn(opUser);
            Mockito.when(userService.updateAdminEmailSetting(opUser.getUserId(), Boolean.valueOf(newSetting)))
                    .thenReturn(1);
            String body = "{\"" + KeyMessage.IS_ADMIN_RECEIVE_EMAIL + "\":\"" + newSetting + "\"}";
            MvcResult mvcResult = mockMvc.perform(
                            MockMvcRequestBuilders.post("/users/changeEmailSetting")
                                    .header(KeyMessage.TOKEN, token)
                                    .contentType(MediaType.APPLICATION_JSON).content(body)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(200))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(OutputMessage.SUCCEED))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();

            UserController.emailTokens.remove(opUser.getEmail());
        } catch (Exception e) {
            Assert.fail("fail to request");
        }

    }
}