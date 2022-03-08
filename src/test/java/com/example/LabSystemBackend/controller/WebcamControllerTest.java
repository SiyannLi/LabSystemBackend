package com.example.LabSystemBackend.controller;

import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.jwt.AuthenticationInterceptor;
import com.example.LabSystemBackend.jwt.JwtUtil;
import com.example.LabSystemBackend.ui.KeyMessage;
import com.example.LabSystemBackend.ui.OutputMessage;
import com.example.LabSystemBackend.util.DataGenerate;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("unittest")
@RunWith(SpringRunner.class)
@WebMvcTest(WebcamController.class)
class WebcamControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthenticationInterceptor interceptor;

    private void webcamAccess(String url) {
        try {
            User user = DataGenerate.generateUser();
            int id = new Random().nextInt(100) + 1;
            user.setUserId(id);
            String token = JwtUtil.createToken(user);
            Mockito.when(interceptor.preHandle(Mockito.any(HttpServletRequest.class)
                    , Mockito.any(HttpServletResponse.class), Mockito.any(Object.class))).thenReturn(true);
            MvcResult mvcResult = mockMvc.perform(
                            MockMvcRequestBuilders.get(url)
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

    @Test
    void adminWebcamAccess() {
        webcamAccess("/webcam/adminWebcamAccess");
    }

    @Test
    void userWebcamAccess() {
        webcamAccess("/webcam/userWebcamAccess");
    }
}