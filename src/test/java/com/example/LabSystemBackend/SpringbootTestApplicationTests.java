package com.example.LabSystemBackend;

import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.service.UserService;
import com.example.LabSystemBackend.util.DataGenerate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SpringbootTestApplicationTests {

    @Autowired
    private UserService userService;
    @Test
    void insertUsersTest() {
        for (int i = 0; i < 10; i++) {
            User user = DataGenerate.generateUser();
            userService.insertUser(user);
        }

        //assertEquals(user, userService.getUser(user.getUserId()));
    }

    @Test
    void insertOneUserTest() {
            User user = DataGenerate.generateUser();

            System.out.println(user);


        assertEquals(user, userService.getUser(user.getUserId()));
    }

}
