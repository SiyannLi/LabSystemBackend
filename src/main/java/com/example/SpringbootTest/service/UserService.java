package com.example.SpringbootTest.service;

import com.example.SpringbootTest.entity.User;
import java.util.List;

public interface UserService {
    List<User> getAllUser();

    User getUser(int userId);

    int deleteUser(int userId);

    int insertUser(User user);

    //login
    User login(String email, String password, boolean isAdmin);

    //log out
    User logout(String email);

    //register one account
    User register(String email, String password, String name, String vertificationCode);

    //reset password
    User resetPassword(String email, String newPassword, String vertificationCode);

    //confirm the application of user to create a new account
    User confirmUserApplication(int userId);

    //reject the application of user to create a new account
    boolean rejectUserApplication(int userId);//拒绝用户申请后，直接在数据库里清除用户的信息

    //change username
    User changeUserName(int userId, String newName);

    //activate or deactivate account
    User deactivateUser(int userId);

    User activateUser(int userId);

    //get a list of all users
    List<User> getAllUsers();

    //get a list of all admins
    List<User> getAllAdministrator();
}
