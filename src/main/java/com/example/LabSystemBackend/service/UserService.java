package com.example.LabSystemBackend.service;

import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.entity.UserAccountStatus;
import com.example.LabSystemBackend.entity.UserRole;

import java.util.List;

/**
 * @version 1.0
 * @author Cong Liu, Siyan Li
 *
 * User service
 */
public interface UserService {

    User getUser(int userId);

    int deleteUser(int userId);


    //register one account
    int register(String email, String password, String firstName, String lastName);

    //reset password
    int resetPassword(String email, String newPassword);

    //confirm the application of user to create a new account
    int confirmUserRegistration(int userId);

    //reject the application of user to create a new account
    int rejectUserRegistration(int userId);

    //activate or deactivate account
    int deactivateUser(int userId);

    int activateUser(int userId);

    //get a list of all users
    List<User> getAllUsers();

    //get a list of all admins
    List<User> getAllAdministrators();

    List<User> getAllAccountToBeConfirmed();

    List<User> getAllAdminReceiveBulkEmail();

    User getUserByEmail(String email);

    Boolean emailExists(String email);

    int updateUserRole(int userId, UserRole role);

    int updateName(int userId, String firstName, String lastName);

    int updateAdminEmailSetting(int userId, boolean receiveBulkEmail);
}
