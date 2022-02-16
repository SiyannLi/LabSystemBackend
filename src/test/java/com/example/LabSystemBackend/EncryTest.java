package com.example.LabSystemBackend;

import com.example.LabSystemBackend.controller.UserController;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EncryTest {
    private static final Logger logger = LoggerFactory.getLogger(EncryTest.class);

    @Autowired
    private StringEncryptor encryptor;

    @Test
    public void getPass() {
//        String mailHost = encryptor.encrypt("smtp.mailtrap.io");
//        String mailUserName = encryptor.encrypt("32f908f3a8d286");
//        String mailPassword = encryptor.encrypt("ca4302891da600");
//        String mailPort = encryptor.encrypt("2525");
//
//        String mailAddress = encryptor.encrypt("tecolabsystem@outlook.com");
//        String mailPassword = encryptor.encrypt("Xj9wXHUPj<d_X/G");
//        String host = encryptor.encrypt("smtp.office365.com");

        String url = encryptor.encrypt("jdbc:mysql://129.13.170.39:3306/labSystem");
        String userName = encryptor.encrypt("labsystem");

        logger.info(url);
        logger.info(userName);





    }
}
