package com.example.SpringbootTest.service.impl;

import com.example.SpringbootTest.dao.UserDao;
import com.example.SpringbootTest.entity.User;
import com.example.SpringbootTest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public List<User> getAllUser() {
        return userDao.getAllUser();
    }

    @Override
    public User getUser(int userId) {
        return userDao.getUser(userId);
    }

    @Override
    public int deleteUser(int userId) {
        return userDao.deleteUser(userId);
    }

    @Override
    public int insertUser(User user) {
        return userDao.insertUser(user);
    }

    @Override
    public User login(String email, String password, boolean isAdmin) {
        return null;
    }

    @Override
    public User logout(String email) {
        return null;
    }

    @Override
    public User register(String email, String password, String name, String vertificationCode) {
        return null;
    }

    @Override
    public User resetPassword(String email, String newPassword, String vertificationCode) {
        return null;
    }

    @Override
    public User confirmUserRegistration(int userId) {
        return null;
    }

    @Override
    public boolean rejectUserRegistration(int userId) {
        return false;
    }

    @Override
    public User changeUserName(int userId, String newName) {
        return null;
    }

    @Override
    public User deactivateUser(int userId) {
        return null;
    }

    @Override
    public User activateUser(int userId) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public List<User> getAllAdministrator() {
        return null;
    }
}
