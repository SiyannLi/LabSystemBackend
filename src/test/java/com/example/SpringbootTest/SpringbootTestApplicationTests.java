package com.example.SpringbootTest;

import com.example.SpringbootTest.entity.User;
import com.example.SpringbootTest.util.DataGenerate;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
class SpringbootTestApplicationTests {

    @Test
    void contextLoads() {
        DataGenerate dataGenerate = new DataGenerate();
        User user = dataGenerate.generateUser();
        System.out.println(user);
    }

}
