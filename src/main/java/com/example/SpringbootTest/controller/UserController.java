package com.example.SpringbootTest.controller;

import com.example.SpringbootTest.common.Constants;
import com.example.SpringbootTest.common.ResultGenerator;
import com.example.SpringbootTest.entity.User;
import com.example.SpringbootTest.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getUser")
    public String getUser(@Param("id") Integer id) {
        User user = userService.findById(id);
        return user.getUserName();
    }


}


