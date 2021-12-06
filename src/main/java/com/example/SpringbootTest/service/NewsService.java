package com.example.SpringbootTest.service;

import com.example.SpringbootTest.entity.News;

import java.util.List;

public interface NewsService {
    //新建一个消息
    News addNews(int senderId, int receiverId, String content);

    //获取所有消息
    List<News> getAllNews();

    //获取当前按用户所有收到的消息
    News getUserAllNews(int receiverId);
}
