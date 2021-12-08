package com.example.LabSystemBackend;

import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.service.UserService;
import com.example.LabSystemBackend.util.DataGenerate;
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
