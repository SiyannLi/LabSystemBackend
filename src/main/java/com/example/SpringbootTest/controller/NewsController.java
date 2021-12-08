package com.example.SpringbootTest.controller;

import com.example.SpringbootTest.common.Response;
import com.example.SpringbootTest.common.ResponseGenerator;
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
    public Response addNews(int senderId, int receiverId, String content){
        return ResponseGenerator.genSuccessResult(newsService.addNews(senderId,receiverId,content));
    }

    @ApiOperation("get list of all news")
    @GetMapping("getAllNews")
    public Response getAllNews(){
        return null;
    }

    @ApiOperation("get list of all news of this user")
    @GetMapping("getUserAllNews")
    public Response getUserAllNews(int receiverId){
        return null;
    }
}
