package com.example.LabSystemBackend.dao;

import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.entity.UserAccountStatus;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserDao {
    List<User> getAllUsers();

    List<User> getAllAdministrators();

    User getUser(@Param("userId") int userId);

    User getUserByEmailAndPassword(@Param("email") String email, @Param("password") String password);

    User getUserByEmail(@Param("email") String email);

    int deleteUser(@Param("userId") int userId);

    int insertUser(@Param("user") User user);

    int updatePassword(@Param("userId") int userId, @Param("password") String password);

    int updateUserAccountStatus(@Param("userId") int userId, @Param("userAccountStatus") UserAccountStatus userAccountStatus);

    int updateName(@Param("userId") int userId, @Param("firstName") String firstName, @Param("lastName") String lastName);

    User getLastUser();
}

