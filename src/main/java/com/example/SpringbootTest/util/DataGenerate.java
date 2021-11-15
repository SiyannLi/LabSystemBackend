package com.example.SpringbootTest.util;

import com.example.SpringbootTest.entity.User;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Random;


public class DataGenerate {

    private static final Faker faker = new Faker();

    public  static User generateUser() {
        User user = new User();
        user.setUserName(faker.harryPotter().character().toLowerCase());
        user.setUserPassword(faker.internet().password());
        user.setRealName(faker.name().name());
        String[] role = {"visitor", "admin"};
        Random random = new Random();
        user.setUserRole(role[random.nextInt(2)]);
        user.setEmail(faker.internet().emailAddress(user.getRealName().replaceAll(" ","").toLowerCase()));
        return user;
    }
}
