package com.example.SpringbootTest.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SqlTestController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/hello")
    public String index() {
        String sql = "SELECT userName FROM user WHERE userName= ?";

        String users = (String) jdbcTemplate.queryForObject(sql,
                new Object[]{"apple"}, String.class);
        return "Hello " + users;
    }


}
