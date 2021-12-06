package com.example.SpringbootTest.controller;

import com.example.SpringbootTest.common.Result;
import com.example.SpringbootTest.service.NewsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/news")
public class NewsController {
    @Autowired
    private NewsService newsService;

    @ApiOperation("create a news")
    @PostMapping("addNews")
    public Result addNews(int senderId, int receiverId, String content){
        return null;
    }

    @ApiOperation("get list of all news")
    @GetMapping("getAllNews")
    public Result getAllNews(){
        return null;
    }

    @ApiOperation("get list of all news of this user")
    @GetMapping("getUserAllNews")
    public Result getUserAllNews(int receiverId){
        return null;
    }
}
