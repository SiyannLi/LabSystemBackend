package com.example.LabSystemBackend.jwt;

import com.example.LabSystemBackend.controller.UserController;
import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.entity.UserAccountStatus;
import com.example.LabSystemBackend.entity.UserRole;
import com.example.LabSystemBackend.service.NotificationService;
import com.example.LabSystemBackend.service.UserService;
import com.example.LabSystemBackend.service.VerifyCodeService;
import com.example.LabSystemBackend.ui.ExceptionMessage;
import com.example.LabSystemBackend.ui.InputMessage;
import com.example.LabSystemBackend.ui.KeyMessage;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@ActiveProfiles("unittest")
@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
class AuthenticationInterceptorTest {
        @Autowired
        private MockMvc mockMvc;
        @MockBean
        private UserService userService;
        @MockBean
        private NotificationService notificationService;
        @MockBean
        private VerifyCodeService verifyCodeService;
        //pass token
        private final String passTokenTestUrl = "/users/visitorLogin";
    private final String needTokenUrl = "/users/logout";
    private final String adminTokenUrl = "/users/getAllAccountToBeConfirmed";
    private final String superAdminTokenUrl = "/users/getAllAdministrator";

    private void checkTokenPermission(String url, User user, int expectedCode, String expectMessage) {
        String token = JwtUtil.createToken(user);
        UserController.emailTokens.put(user.getEmail(), token);
        try {
            Mockito.when(userService.emailExists(user.getEmail())).thenReturn(true);
            Mockito.when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
            MvcResult mvcResult = mockMvc.perform(
                            MockMvcRequestBuilders.get(url)
                                    .header(KeyMessage.TOKEN, token)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(expectedCode))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(expectMessage))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            UserController.emailTokens.remove(user.getEmail());
        } catch (Exception e) {
            Assert.fail("fail to request");
        }

    }

    @Test
    void checkTokenAdminFail() {
        User user = DataGenerate.generateUser();
        int id = new Random().nextInt(100) + 1;
        user.setUserId(id);
        //set role
        user.setUserRole(UserRole.USER);
        checkTokenPermission(adminTokenUrl, user, 402, ExceptionMessage.NOT_ADMIN);
    }

    @Test
    void checkTokenSuperAdminSucceed() {
        User user = DataGenerate.generateUser();
        int id = new Random().nextInt(100) + 1;
        user.setUserId(id);
        //set role
        user.setUserRole(UserRole.SUPER_ADMIN);
        user.setEmail(InputMessage.SUPER_ADMIN_EMAIL);
        List<User> users = new ArrayList<>();
        Mockito.when(userService.getAllAdministrators()).thenReturn(users);
        checkTokenPermission(superAdminTokenUrl, user, 200, "SUCCESS");
    }

    @Test
    void checkTokenSuperAdminFail() {
        User user = DataGenerate.generateUser();
        int id = new Random().nextInt(100) + 1;
        user.setUserId(id);
        UserRole role = Arrays.stream(UserRole.values()).filter(e -> !e.equals(UserRole.SUPER_ADMIN)).findAny().get();
        //set role
        user.setUserRole(role);
        //user or admin
        checkTokenPermission(superAdminTokenUrl, user, 402, ExceptionMessage.NOT_SUPER_ADMIN);
    }

    @Test
    void checkTokenAdminSucceed() {
        User user = DataGenerate.generateUser();
        int id = new Random().nextInt(100) + 1;
        user.setUserId(id);
        //admin or super admin
        UserRole role = Arrays.stream(UserRole.values()).filter(e -> !e.equals(UserRole.USER)).findAny().get();
        user.setUserRole(role);
        List<User> users = new ArrayList<>();
        Mockito.when(userService.getAllAccountToBeConfirmed()).thenReturn(users);
        checkTokenPermission(adminTokenUrl, user, 200, OutputMessage.NO_USER_TO_CONFIRM);
    }

    @Test
    void nullTokenFail() {
        try {
            MvcResult mvcResult = mockMvc.perform(
                            MockMvcRequestBuilders.get(needTokenUrl)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(402))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ExceptionMessage.TOKEN_NULL))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        } catch (Exception e) {
            Assert.fail("fail to request");
        }
    }

    @Test
    void tokenWorryFail() {
        try {
            Faker faker = new Faker();
            String token = faker.internet().password();
            MvcResult mvcResult = mockMvc.perform(
                            MockMvcRequestBuilders.get(needTokenUrl)
                                    .header(KeyMessage.TOKEN, token)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(402))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ExceptionMessage.TOKEN_WRONG))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        } catch (Exception e) {
            Assert.fail("fail to request");
        }
    }

    @Test
    void tokenConflictFail() {
        try {
            User user = DataGenerate.generateUser();
            int id = new Random().nextInt(100) + 1;
            user.setUserId(id);
            String tokenFront = JwtUtil.createToken(user);
            String tokenServer = JwtUtil.createToken(user, 2, 1);
            UserController.emailTokens.put(user.getEmail(), tokenServer);
            MvcResult mvcResult = mockMvc.perform(
                            MockMvcRequestBuilders.get(needTokenUrl)
                                    .header(KeyMessage.TOKEN, tokenFront)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(402))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ExceptionMessage.TOKEN_CONFLICT))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            UserController.emailTokens.remove(user.getEmail());
        } catch (Exception e) {
            Assert.fail("fail to request");
        }
    }

    @Test
    void emailNotExistFail() {
        try {
            User user = DataGenerate.generateUser();
            int id = new Random().nextInt(100) + 1;
            user.setUserId(id);
            String token = JwtUtil.createToken(user);
            UserController.emailTokens.put(user.getEmail(), token);
            Mockito.when(userService.emailExists(user.getEmail())).thenReturn(false);
            MvcResult mvcResult = mockMvc.perform(
                            MockMvcRequestBuilders.get(needTokenUrl)
                                    .header(KeyMessage.TOKEN, token)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(402))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ExceptionMessage.USER_NOT_EXISTS))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            UserController.emailTokens.remove(user.getEmail());
        } catch (Exception e) {
            Assert.fail("fail to request");
        }
    }

    @Test
    void tokenExpiredFail() {
        try {
            User user = DataGenerate.generateUser();
            int id = new Random().nextInt(100) + 1;
            user.setUserId(id);
            String token = JwtUtil.createToken(user, 2, 1);
            UserController.emailTokens.put(user.getEmail(), token);
            Thread.currentThread().sleep(2000);
            Mockito.when(userService.emailExists(user.getEmail())).thenReturn(true);
            MvcResult mvcResult = mockMvc.perform(
                            MockMvcRequestBuilders.get(needTokenUrl)
                                    .header(KeyMessage.TOKEN, token)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(402))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ExceptionMessage.EXPIRED_TOKEN))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            UserController.emailTokens.remove(user.getEmail());
        } catch (Exception e) {
            Assert.fail("fail to request");
        }
    }

    @Test
    void CheckTokenRefresh() {
        try {
            User user = DataGenerate.generateUser();
            int id = new Random().nextInt(100) + 1;
            user.setUserId(id);
            String token = JwtUtil.createToken(user, 2, 1);
            UserController.emailTokens.put(user.getEmail(), token);
            Thread.currentThread().sleep(1000);
            Mockito.when(userService.emailExists(user.getEmail())).thenReturn(true);
            Mockito.when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
            MvcResult mvcResult = mockMvc.perform(
                            MockMvcRequestBuilders.get(needTokenUrl)
                                    .header(KeyMessage.TOKEN, token)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(200))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(OutputMessage.SUCCEED))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            String tokenServer = UserController.emailTokens.get(user.getEmail());
            Assert.assertNotEquals(token, tokenServer);
            UserController.emailTokens.remove(user.getEmail());
        } catch (Exception e) {
            Assert.fail("fail to request");
        }
    }

    @Test
    void checkForPassToken() {
        Faker faker = new Faker();
        String token = faker.internet().password();
        User user = DataGenerate.generateUser();
        int id = new Random().nextInt(100) + 1;
        user.setUserId(id);
        user.setUserAccountStatus(UserAccountStatus.ACTIVE);
        Mockito.when(userService.emailExists(user.getEmail())).thenReturn(true);
        Mockito.when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
        String body = "{\"" + KeyMessage.EMAIL + "\":\"" + user.getEmail() + "\",\"" + KeyMessage.PASSWORD
                + "\":\"" + user.getUserPassword() + "\"}";
        try {
            MvcResult mvcResult = mockMvc.perform(
                            MockMvcRequestBuilders.post(passTokenTestUrl)
                                    .header(KeyMessage.TOKEN, token)
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
}