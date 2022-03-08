package com.example.LabSystemBackend.service.impl;

import com.example.LabSystemBackend.entity.Notification;
import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.entity.UserAccountStatus;
import com.example.LabSystemBackend.entity.UserRole;
import com.example.LabSystemBackend.service.NotificationService;
import com.example.LabSystemBackend.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;

import static org.junit.jupiter.api.Assertions.*;
/**
 * @version 1.0
 * @author Sheyang Li
 *
 */

@ActiveProfiles("unittest")
@Transactional
@Rollback(value = true)
@SpringBootTest
class VerifyCodeServiceImplTest {
    @Autowired
    VerifyCodeServiceImpl verifyCodeService;
    @MockBean
    UserService userService;
    @MockBean
    NotificationService notificationService;

    //test data
    User user = new User(39,"admin","admin","cong liu@hotmail.com",
            UserRole.ADMIN,"a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3",
            UserAccountStatus.ACTIVE,true);
    @Test
    void getRandomVerCode() {
        assertNotNull( verifyCodeService.getRandomVerCode());

    }


    private void sendVerifyCode(String email, String code, Boolean emailExists, String rejectId) throws MessagingException {

        Mockito.when(userService.emailExists(Mockito.anyString())).thenReturn(emailExists);
        Mockito.when(userService.getUserByEmail(Mockito.anyString())).thenReturn(user);
        Mockito.when(notificationService.sendNotification(Mockito.anyString(), Mockito.any())).thenReturn(2);
        Notification testNotification = verifyCodeService.sendVerifyCode(email,code);
        if(emailExists){
        assertEquals(39,testNotification.getRecipientId());
             }else {testNotification.getRecipientId().toString().equals("ID_OF_UNREGISTERED");}
    }
    @Test
    void sendVerifyCodebyUser() throws MessagingException {
        sendVerifyCode(user.getEmail(), "123456", true, user.getUserId().toString());
    }
    @Test
    void sendVerifyCodebyNoUser() throws MessagingException {
        sendVerifyCode("liucong@hotmail.com", "123456", false, "ID_OF_UNREGISTERED");
    }



    private void checkVerifyCode(String input, String verCode) {
     assertTrue(verifyCodeService.checkVerifyCode(input,verCode));
    }
    @Test
    void checkVerifyCode(){
        checkVerifyCode("123456","123456");
    }
}