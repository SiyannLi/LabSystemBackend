package com.example.SpringbootTest.service;

import com.example.SpringbootTest.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserService {
    List<User> getAllUser();

    User getUser(int userId);

    boolean deleteUser(int userId);

}
