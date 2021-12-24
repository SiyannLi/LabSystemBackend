package com.example.LabSystemBackend.dao;

import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.entity.UserAccountStatus;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserDao {
    List<User> getAllUser();

    List<User> getAllAdministrators();

    User getUser(@Param("userId") int userId);

    int deleteUser(@Param("userId") int userId);

    int insertUser(@Param("user") User user);

    int updatePassword(@Param("userId") int userId, @Param("password") String password);

    int updateUserAccountStatus(@Param("userAccountStatus")String userAccountStatus,@Param("userId") int userId);

    int updateName(@Param("userId")int userId, @Param("firstName") String firstName, @Param("lastName") String lastName);


}

