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
    public boolean deleteUser(int userId) {
        return userDao.deleteUser(userId) > 0;
    }
}
