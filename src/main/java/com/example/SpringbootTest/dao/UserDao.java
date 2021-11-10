package com.example.SpringbootTest.dao;

import com.example.SpringbootTest.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserDao {
    @Select("select * from users")
    List<User> getAllUser();

    @Select("select * from users where userId = #{userId}")
    User getUser(@Param("id") int userId);

    @Delete("delete from users where userId = #{userId}")
    int deleteUser(@Param("userId") int userId);

}
