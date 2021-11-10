package com.example.SpringbootTest.service.impl;

import com.example.SpringbootTest.dao.UserDao;
import com.example.SpringbootTest.entity.User;
import com.example.SpringbootTest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User findById(Integer userId){
        return userDao.getUserByUserId(userId);
    }

    @Override
    public int save(User user) {
        return userDao.addUser(user);
    }

}
