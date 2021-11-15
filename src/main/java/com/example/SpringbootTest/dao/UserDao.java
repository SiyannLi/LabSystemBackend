package com.example.SpringbootTest.dao;

import com.example.SpringbootTest.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserDao {
    List<User> getAllUser();

    User getUser(@Param("userId") int userId);

    int deleteUser(@Param("userId") int userId);

    int insertUser(@Param("user") User user);

    int updateUser(@Param("user") User user);
}
