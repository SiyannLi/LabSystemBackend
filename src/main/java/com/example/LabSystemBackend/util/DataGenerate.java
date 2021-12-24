package com.example.LabSystemBackend.util;

import com.example.LabSystemBackend.dao.UserDao;
import com.example.LabSystemBackend.entity.Order;
import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.entity.UserAccountStatus;
import com.example.LabSystemBackend.entity.UserRole;
import com.example.LabSystemBackend.service.UserService;
import com.example.LabSystemBackend.service.impl.UserServiceImpl;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Random;


public class DataGenerate {

    private static final Faker faker = new Faker();

    public  static User generateUser() {
        String name = faker.harryPotter().character().toLowerCase();
        User user = new User();
        //user.setUserId(new Random().nextInt());
        user.setFirstName(name.split(" ")[0]);
        user.setUserPassword(faker.internet().password());
        user.setLastName(name.split(" ")[1]);
        int role = new Random().nextInt(UserRole.values().length);
        user.setUserRole(UserRole.values()[role]);
        user.setEmail(faker.internet().emailAddress(name.replaceAll(" ","").toLowerCase()));
        user.setUserAccountStatus(UserAccountStatus.CONFIRMING);
        return user;
    }


}
