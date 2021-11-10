package com.example.SpringbootTest.util;

import com.example.SpringbootTest.entity.User;
import com.github.javafaker.Faker;

import java.util.Arrays;
import java.util.Locale;
import java.util.Random;


public class DataGenerate {

    Faker faker = new Faker();

    public User generateUser() {
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
