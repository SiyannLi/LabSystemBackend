package com.example.SpringbootTest.dao;

import com.example.SpringbootTest.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface UserDao {
    int addUser(User user);

    User getUserByUserId(int userId);

    User getUserByUserNameAndPassword(@Param("userName") String userName, @Param("userPassword") String userPassword);
}
