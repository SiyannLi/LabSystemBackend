package com.example.SpringbootTest;

import com.example.SpringbootTest.controller.UserController;
import com.example.SpringbootTest.entity.User;
import com.example.SpringbootTest.service.UserService;
import com.example.SpringbootTest.util.DataGenerate;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
class SpringbootTestApplicationTests {

    @Autowired
    private UserService userService;
    @Test
    void insertUsersTest() {
        DataGenerate dataGenerate = new DataGenerate();
        for (int i = 0; i < 10; i++) {
            User user = dataGenerate.generateUser();
            userService.insertUser(user);
        }

        //assertEquals(user, userService.getUser(user.getUserId()));
    }

    @Test
    void insertOneUserTest() {
        DataGenerate dataGenerate = new DataGenerate();

            User user = dataGenerate.generateUser();

            System.out.println(user);


        //assertEquals(user, userService.getUser(user.getUserId()));
    }

}
