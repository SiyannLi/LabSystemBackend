package com.example.LabSystemBackend.service.impl;

import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.service.UserService;
import com.example.LabSystemBackend.util.DataGenerate;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest
class UserServiceImplTest {
    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImplTest.class);



    @Test
    void getAllUser() {
        List<User> a = userService.getAllUsers();
        for (User user : a) {
            logger.info(user.toString());
        }
    }

    @Test
    void deleteUser() {
        userService.deleteUser(1);
        assertNull(userService.getUser(1));
    }

    @Test
    void insertUser() {
        User user = DataGenerate.generateUser();
        userService.insertUser(user);
        logger.info(user.getUserId().toString());
    }

    @Test
    void login() {
    }

    @Test
    void logout() {
    }

    @Test
    void register() {
    }

    @Test
    void resetPassword() {
    }

    @Test
    void confirmUserRegistration() {
    }

    @Test
    void rejectUserRegistration() {
    }

    @Test
    void changeUserName() {
    }

    @Test
    void deactivateUser() {
    }

    @Test
    void activateUser() {
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void getAllAdministrator() {
    }
}