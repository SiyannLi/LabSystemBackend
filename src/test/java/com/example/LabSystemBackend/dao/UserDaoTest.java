package com.example.LabSystemBackend.dao;

import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.entity.UserAccountStatus;
import com.example.LabSystemBackend.util.DataGenerate;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserDaoTest {

    @Autowired
    UserDao userDao;

    private static final Logger logger = LoggerFactory.getLogger(UserDaoTest.class);

    @Test
    void getAllUser() {
        List<User> users = userDao.getAllUsers();
        assertNotNull(users);
    }

    @Test
    void getAllAdministrators() {
        List<User> ads = userDao.getAllAdministrators();
        for (User ad : ads) {
            assertEquals("ADMIN", ad.getUserRole().toString());
        }

    }

    @Test
    void getUser() {
        User user = userDao.getUser(2);
        assertNotNull(user);
    }

    @Test
    void deleteUser() {
        User user = DataGenerate.generateUser();
        userDao.insertUser(user);
        userDao.deleteUser(user.getUserId());
        User get = userDao.getUser(user.getUserId());
        assertNull(get);
    }

    @Test
    void insertUser() {
        User user = DataGenerate.generateUser();
        userDao.insertUser(user);
        User get = userDao.getUser(user.getUserId());
        assertEquals(user, get);
        userDao.deleteUser(user.getUserId());

    }

    @Test
    void updatePassword() {
        User user = DataGenerate.generateUser();
        userDao.insertUser(user);
        userDao.updatePassword(user.getUserId(), "testPassword");
        User get = userDao.getUser(user.getUserId());
        assertEquals("testPassword", get.getUserPassword());
        userDao.deleteUser(user.getUserId());
    }

    @Test
    void updateUserAccountStatus() {
        User user = DataGenerate.generateUser();
        userDao.insertUser(user);
        userDao.updateUserAccountStatus(user.getUserId(), UserAccountStatus.INACTIVE);
        User get = userDao.getUser(user.getUserId());
        assertEquals(UserAccountStatus.INACTIVE, get.getUserAccountStatus());
        userDao.deleteUser(user.getUserId());
    }

    @Test
    void updateName() {
        User user = DataGenerate.generateUser();
        userDao.insertUser(user);
        userDao.updateName(user.getUserId(), "testFirstName", "testLastName");
        User get = userDao.getUser(user.getUserId());
        assertEquals("testFirstName", get.getFirstName());
        assertEquals("testLastName", get.getLastName());
        userDao.deleteUser(user.getUserId());
    }

    @Test
    void getLastUser() {
        User user = userDao.getLastUser();
        assertNotNull(user);
    }

    @Test
    void addData(){
        for (int i = 0;i<10;i++){
            User user = DataGenerate.generateUser();
            userDao.insertUser(user);
        }
    }
}