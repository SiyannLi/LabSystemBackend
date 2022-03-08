package com.example.LabSystemBackend.jwt;

import com.example.LabSystemBackend.controller.ItemController;
import com.example.LabSystemBackend.controller.UserController;
import com.example.LabSystemBackend.controller.WebcamController;
import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.entity.UserAccountStatus;
import com.example.LabSystemBackend.service.ItemService;
import com.example.LabSystemBackend.service.UserService;
import com.example.LabSystemBackend.ui.ExceptionMessage;
import com.example.LabSystemBackend.ui.KeyMessage;
import com.example.LabSystemBackend.ui.OutputMessage;
import com.example.LabSystemBackend.util.DataGenerate;
import org.junit.Assert;
import org.junit.Test;
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

import java.util.Arrays;
import java.util.Random;

@ActiveProfiles("unittest")
@RunWith(SpringRunner.class)
@WebMvcTest(WebcamController.class)
public class AuthenticationInterceptorTestForUserLogin {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    //user token url
    private final String passTokenTestUrl = "/webcam/userWebcamAccess";

    private void checkTokenPermission(User user, int expectedCode, String expectMessage) {
        String token = JwtUtil.createToken(user);
        UserController.emailTokens.put(user.getEmail(), token);
        try {
            Mockito.when(userService.emailExists(user.getEmail())).thenReturn(true);
            Mockito.when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
            MvcResult mvcResult = mockMvc.perform(
                            MockMvcRequestBuilders.get(passTokenTestUrl)
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
    public void checkTokenUserFail() {
        User user = DataGenerate.generateUser();
        int id = new Random().nextInt(100) + 1;
        user.setUserId(id);
        //set role
        user.setUserAccountStatus(Arrays.stream(UserAccountStatus.values())
                .filter(e -> !e.equals(UserAccountStatus.ACTIVE)).findAny().get());
        checkTokenPermission(user, 402, ExceptionMessage.INACTIVE_ACCOUNT);
    }

    @Test
    public void checkTokenUserSucceed() {
        User user = DataGenerate.generateUser();
        int id = new Random().nextInt(100) + 1;
        user.setUserId(id);
        //set role
        user.setUserAccountStatus(UserAccountStatus.ACTIVE);
        checkTokenPermission(user, 200, OutputMessage.SUCCEED);
    }




}
