package com.example.LabSystemBackend.service.impl;

import com.example.LabSystemBackend.dao.UserDao;
import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.entity.UserAccountStatus;
import com.example.LabSystemBackend.entity.UserRole;
import com.example.LabSystemBackend.service.UserService;
import com.example.LabSystemBackend.util.DataGenerate;
import com.github.javafaker.Faker;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("unittest")
@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceImplTest {
    @Autowired
    UserService userService;
    @MockBean
    private UserDao userDao;

    @Test
    void getUser() {
        User user = DataGenerate.generateUser();
        int id = new Random().nextInt(100) + 1;
        user.setUserId(id);
        Mockito.when(userDao.getUser(id)).thenReturn(user);
        User userTest = userService.getUser(id);
        Assert.assertNotNull(userTest);
        Assert.assertEquals(user, userTest);

    }

    @Test
    void deleteUser() {
        int result = new Random().nextInt(2);
        int id = new Random().nextInt(100) + 1;
        Mockito.when(userDao.deleteUser(id)).thenReturn(result);
        int resultTest = userService.deleteUser(id);
        Assert.assertNotNull(resultTest);
        Assert.assertEquals(result, resultTest);

    }

    @Test
    void resetPassword() {
        User user = DataGenerate.generateUser();
        int id = new Random().nextInt(100) + 1;
        user.setUserId(id);
        Faker faker  = new Faker();
        String newPassword = faker.internet().password();
        int result = new Random().nextInt(2);
        Mockito.when(userDao.getUserByEmail(user.getEmail())).thenReturn(user);
        Mockito.when(userDao.updatePassword(id, newPassword)).thenReturn(result);
        int resultTest = userService.resetPassword(user.getEmail(), newPassword);
        Assert.assertNotNull(resultTest);
        Assert.assertEquals(result, resultTest);

    }

    @Test
    void confirmUserRegistration() {
        int id = new Random().nextInt(100) + 1;
        int result = new Random().nextInt(2);
        Mockito.when(userDao.updateUserAccountStatus(id, UserAccountStatus.ACTIVE)).thenReturn(result);
        int resultTest = userService.confirmUserRegistration(id);
        Assert.assertNotNull(resultTest);
        Assert.assertEquals(result, resultTest);
    }

    @Test
    void rejectUserRegistration() {
        int id = new Random().nextInt(100) + 1;
        int result = new Random().nextInt(2);
        Mockito.when(userDao.deleteUser(id)).thenReturn(result);
        int resultTest = userService.rejectUserRegistration(id);
        Assert.assertNotNull(resultTest);
        Assert.assertEquals(result, resultTest);
    }

    @Test
    void register() {
        User user = DataGenerate.generateUser();
        int result = new Random().nextInt(2);
        Mockito.when(userDao.insertUser(Mockito.any(User.class))).thenReturn(result);
        int resultTest = userService.register(user.getEmail(), user.getUserPassword()
                , user.getFirstName(), user.getLastName());
        Assert.assertNotNull(resultTest);
        Assert.assertEquals(result, resultTest);

    }

    @Test
    void deactivateUser() {
        int id = new Random().nextInt(100) + 1;
        int result = new Random().nextInt(2);
        Mockito.when(userDao.updateUserAccountStatus(id, UserAccountStatus.INACTIVE)).thenReturn(result);
        int resultTest = userService.deactivateUser(id);
        Assert.assertNotNull(resultTest);
        Assert.assertEquals(result, resultTest);

    }

    @Test
    void activateUser() {
        int id = new Random().nextInt(100) + 1;
        int result = new Random().nextInt(2);
        Mockito.when(userDao.updateUserAccountStatus(id, UserAccountStatus.ACTIVE)).thenReturn(result);
        int resultTest = userService.activateUser(id);
        Assert.assertNotNull(resultTest);
        Assert.assertEquals(result, resultTest);
    }

    @Test
    void getAllUsers() {
        List<User> users = new ArrayList<>();
        int num = new Random().nextInt(100);
        for(int i = 0; i < num; i++) {
            User user = DataGenerate.generateUser();
            user.setUserId(i+1);
            int statusChoose = new Random().nextInt(2);
            UserAccountStatus status = statusChoose == 0 ? UserAccountStatus.INACTIVE : UserAccountStatus.ACTIVE;
            user.setUserAccountStatus(status);
            users.add(user);
        }
        Mockito.when(userDao.getAllUsers()).thenReturn(users);
        List<User> userTest = userService.getAllUsers();
        Assert.assertNotNull(userTest);
        Assert.assertTrue(userTest.size() == num);
        Assert.assertEquals(users, userTest);


    }

    @Test
    void getAllAdministrators() {
        List<User> admins = new ArrayList<>();
        int num = new Random().nextInt(100);
        for(int i = 0; i < num; i++) {
            User user = DataGenerate.generateUser();
            user.setUserId(i+1);
            int choose = new Random().nextInt(2);
            UserAccountStatus status = choose == 0 ? UserAccountStatus.INACTIVE : UserAccountStatus.ACTIVE;
            UserRole role = choose == 0 ? UserRole.ADMIN : UserRole.SUPER_ADMIN;
            user.setUserAccountStatus(status);
            user.setUserRole(role);
            admins.add(user);
        }
        Mockito.when(userDao.getAllAdministrators()).thenReturn(admins);
        List<User> adminTest = userService.getAllAdministrators();
        Assert.assertNotNull(adminTest);
        Assert.assertTrue(adminTest.size() == num);
        Assert.assertEquals(admins, adminTest);
    }

    @Test
    void getAllAccountToBeConfirmed() {
        List<User> users = new ArrayList<>();
        int num = new Random().nextInt(100);
        for(int i = 0; i < num; i++) {
            User user = DataGenerate.generateUser();
            user.setUserId(i+1);
            user.setUserAccountStatus(UserAccountStatus.PENDING);
            user.setUserRole(UserRole.USER);
            users.add(user);
        }
        Mockito.when(userDao.getAllAccountToBeConfirmed()).thenReturn(users);
        List<User> userTest = userService.getAllAccountToBeConfirmed();
        Assert.assertNotNull(userTest);
        Assert.assertTrue(userTest.size() == num);
        Assert.assertEquals(users, userTest);
    }

    @Test
    void getAllAdminReceiveBulkEmail() {
        List<User> admins = new ArrayList<>();
        int num = new Random().nextInt(100);
        for(int i = 0; i < num; i++) {
            User user = DataGenerate.generateUser();
            user.setUserId(i+1);
            int choose = new Random().nextInt(2);
            UserRole role = choose == 0 ? UserRole.ADMIN : UserRole.SUPER_ADMIN;
            user.setReceiveBulkEmail(true);
            user.setUserRole(role);
            admins.add(user);
        }
        Mockito.when(userDao.getAllAdminReceiveBulkEmail()).thenReturn(admins);
        List<User> adminTest = userService.getAllAdminReceiveBulkEmail();
        Assert.assertNotNull(adminTest);
        Assert.assertTrue(adminTest.size() == num);
        Assert.assertEquals(admins, adminTest);
    }

    @Test
    void getUserByEmail() {
        User user = DataGenerate.generateUser();
        int id = new Random().nextInt(100) + 1;
        user.setUserId(id);
        Mockito.when(userDao.getUserByEmail(user.getEmail())).thenReturn(user);
        User userTest = userService.getUserByEmail(user.getEmail());
        Assert.assertNotNull(userTest);
        Assert.assertEquals(user, userTest);

    }

    @Test
    void emailExistsTrue() {
        User user = DataGenerate.generateUser();
        int id = new Random().nextInt(100) + 1;
        user.setUserId(id);
        Mockito.when(userDao.getUserByEmail(user.getEmail())).thenReturn(user);
        boolean result = userService.emailExists(user.getEmail());
        Assert.assertTrue(result);

    }

    @Test
    void emailExistsFalse() {
        Faker faker = new Faker();
        String email = faker.internet().emailAddress();
        Mockito.when(userDao.getUserByEmail(email)).thenReturn(null);
        boolean result = userService.emailExists(email);
        Assert.assertFalse(result);

    }

    @Test
    void updateUserRole() {
        int id = new Random().nextInt(100) + 1;
        int role = new Random().nextInt(UserRole.values().length);
        int result = new Random().nextInt(2);
        Mockito.when(userDao.updateUserRole(id, UserRole.values()[role])).thenReturn(result);
        int resultTest = userService.updateUserRole(id, UserRole.values()[role]);
        Assert.assertNotNull(resultTest);
        Assert.assertEquals(result, resultTest);
    }

    @Test
    void updateName() {
        int id = new Random().nextInt(100) + 1;
        Faker faker  = new Faker();
        String[] name;
        do {
            name = faker.harryPotter().character().toLowerCase().split(" ");

        } while (name.length != 2);
        int result = new Random().nextInt(2);
        Mockito.when(userDao.updateName(id, name[0], name[1])).thenReturn(result);
        int resultTest = userService.updateName(id, name[0], name[1]);
        Assert.assertNotNull(resultTest);
        Assert.assertEquals(result, resultTest);
    }

    @Test
    void updateAdminEmailSetting() {
        int id = new Random().nextInt(100) + 1;
        int num = new Random().nextInt(2);
        boolean setting = num == 0 ? false : true;
        int result = new Random().nextInt(2);
        Mockito.when(userDao.updateAdminEmailSetting(id, setting)).thenReturn(result);
        int resultTest = userService.updateAdminEmailSetting(id, setting);
        Assert.assertNotNull(resultTest);
        Assert.assertEquals(result, resultTest);
    }
}