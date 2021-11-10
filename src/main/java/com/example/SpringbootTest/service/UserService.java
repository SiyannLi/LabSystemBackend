package com.example.SpringbootTest.service;

import com.example.SpringbootTest.entity.User;

public interface UserService {
    public User findById(Integer id);

    public int save(User user);
}
