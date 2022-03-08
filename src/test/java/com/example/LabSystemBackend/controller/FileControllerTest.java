package com.example.LabSystemBackend.controller;

import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.jwt.AuthenticationInterceptor;
import com.example.LabSystemBackend.jwt.JwtUtil;
import com.example.LabSystemBackend.service.NotificationService;
import com.example.LabSystemBackend.service.UserService;
import com.example.LabSystemBackend.service.VerifyCodeService;
import com.example.LabSystemBackend.ui.KeyMessage;
import com.example.LabSystemBackend.ui.OutputMessage;
import com.example.LabSystemBackend.util.DataGenerate;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("unittest")
@RunWith(SpringRunner.class)
@WebMvcTest(FileController.class)
class FileControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private AuthenticationInterceptor interceptor;

    @Test
    void view() {
        try {
            Mockito.when(interceptor.preHandle(Mockito.any(HttpServletRequest.class)
                    , Mockito.any(HttpServletResponse.class), Mockito.any(Object.class))).thenReturn(true);
            MvcResult mvcResult = mockMvc.perform(
                            MockMvcRequestBuilders.get("/files/view")
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(200))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        } catch (Exception e) {
            Assert.fail("fail to request");
        }
    }
}