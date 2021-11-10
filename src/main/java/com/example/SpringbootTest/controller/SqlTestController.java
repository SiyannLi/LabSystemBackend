package com.example.SpringbootTest.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class SqlTestController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/hello")
    public String index() {
        return "hello";
    }


}
