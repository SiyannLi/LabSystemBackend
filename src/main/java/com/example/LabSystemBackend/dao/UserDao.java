package com.example.LabSystemBackend.dao;

import com.example.LabSystemBackend.entity.User;
import com.example.LabSystemBackend.entity.UserAccountStatus;
import com.example.LabSystemBackend.entity.UserRole;

import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @version 1.0
 * @author Cong Liu, Siyan Li
 */
@Mapper
public interface UserDao {
    List<User> getAllUsers();

    List<User> getAllAdministrators();

    List<User> getAllAccountToBeConfirmed();

    List<User> getAllAdminReceiveBulkEmail();

    User getUser(@Param("userId") int userId);

    User getUserByEmail(@Param("email") String email);

    int deleteUser(@Param("userId") int userId);

    int insertUser(@Param("user") User user);

    int updatePassword(@Param("userId") int userId, @Param("password") String password);

    int updateUserAccountStatus(@Param("userId") int userId, @Param("userAccountStatus") UserAccountStatus userAccountStatus);

    int updateName(@Param("userId") int userId, @Param("firstName") String firstName, @Param("lastName") String lastName);

    int updateUserRole(@Param("userId") int userId, @Param("userRole") UserRole userRole);

    int updateAdminEmailSetting(@Param("userId") int userId, @Param("receiveBulkEmail") boolean receiveBulkEmail);

}

