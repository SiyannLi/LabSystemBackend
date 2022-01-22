package com.example.LabSystemBackend.service.impl;

import com.example.LabSystemBackend.dao.UserDao;
import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.entity.UserAccountStatus;
import com.example.LabSystemBackend.entity.UserRole;
import com.example.LabSystemBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

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
    public int resetPassword(String email, String newPassword) {
        User user = userDao.getUserByEmail(email);
        return userDao.updatePassword(user.getUserId(), newPassword);
    }

    @Override
    public int confirmUserRegistration(int userId) {
        return userDao.updateUserAccountStatus(userId, UserAccountStatus.ACTIVE);
    }

    @Override
    public int rejectUserRegistration(int userId) {
        return userDao.deleteUser(userId);
    }

    @Override
    public int register(String email, String password, String firstName, String lastName, String verificationCode) {
       User user = new User();
       user.setEmail(email);
       user.setUserRole(UserRole.VISITOR);
       user.setUserPassword(password);
       user.setUserAccountStatus(UserAccountStatus.CONFIRMING);
       user.setFirstName(firstName);
       user.setLastName(lastName);
       user.setVerifyCode(verificationCode);
        return userDao.insertUser(user);
    }

    @Override
    public int changeUserName(int userId, String newFirstName, String newLastName) {
        return userDao.updateName(userId, newFirstName, newLastName);
    }

    @Override
    public int deactivateUser(int userId) {
        return userDao.updateUserAccountStatus(userId, UserAccountStatus.INACTIVE);
    }

    @Override
    public int activateUser(int userId) {
        return userDao.updateUserAccountStatus(userId, UserAccountStatus.ACTIVE);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public List<User> getAllAdministrators() {
        return userDao.getAllAdministrators();
    }


    @Override
    public List<User> getAllAccountToBeConfirmed() {
        return userDao.getAllAccountToBeConfirmed();
    }

    @Override
    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    @Override
    public Boolean emailExists(String email) {
        return userDao.getUserByEmail(email) != null;
    }

    @Override
    public int updateUserRole(int userId, UserRole role) {
        return userDao.updateUserRole(userId, role);
    }
}
