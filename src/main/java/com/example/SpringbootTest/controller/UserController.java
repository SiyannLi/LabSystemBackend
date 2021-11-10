package com.example.SpringbootTest.controller;


import com.example.SpringbootTest.common.Result;
import com.example.SpringbootTest.common.ResultGenerator;
import com.example.SpringbootTest.entity.User;
import com.example.SpringbootTest.service.UserService;
import com.example.SpringbootTest.util.DataGenerate;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("get one user")
    @GetMapping("get/{userId}")
    public Result getUsers(@ApiParam(name = "id", value = "主键id", required = true) @PathVariable int userId) {
        System.out.println("获取参数" + userId);
        DataGenerate dataGenerate = new DataGenerate();
        System.out.println("进入函数");
        //User user = userService.getUser(userId);
        System.out.println("获取用户");
        User user = dataGenerate.generateUser();
        if (null != user) {
            return ResultGenerator.genSuccessResult(user);
        } else {
            return ResultGenerator.genFailResult("user dont exist");
        }

    }


}


