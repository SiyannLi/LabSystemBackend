package com.example.LabSystemBackend.service.impl;

import com.example.LabSystemBackend.dao.UserDao;
import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.entity.UserAccountStatus;
import com.example.LabSystemBackend.service.UserService;
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
    public int resetPassword(String email, String newPassword, String verificationCode) {
        return userDao.updatePassword(1,newPassword);
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
    public int register(String email, String password, String firstName, String lastName, String verificationCode) {
        return userDao.insertUser(new User());
    }

    @Override
    public int changeUserName(int userId, String newFirstName, String newLastName) {
        return userDao.updateName(userId,newFirstName,newLastName);
    }

    @Override
    public int deactivateUser(int userId) {
        return userDao.updateUserAccountStatus(UserAccountStatus.ACTIVE,userId);
    }

    @Override
    public int activateUser(int userId) {
        return userDao.updateUserAccountStatus(UserAccountStatus.ACTIVE,userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUser();
    }

    @Override
    public List<User> getAllAdministrator() {
        return userDao.getAllAdministrators();
    }
}
