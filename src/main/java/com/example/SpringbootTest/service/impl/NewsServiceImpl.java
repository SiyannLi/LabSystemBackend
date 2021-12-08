package com.example.SpringbootTest.service.impl;

import com.example.SpringbootTest.dao.NewsDao;
import com.example.SpringbootTest.entity.News;
import com.example.SpringbootTest.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {
    @Autowired
    private NewsDao newsDao;

    @Override
    public int addNews(int senderId, int receiverId, String content) {
        return newsDao.insertNews(new News());
    }

    @Override
    public List<News> getAllNews() {
        return null;
    }

    @Override
    public News getUserAllNews(int receiverId) {
        return null;
    }
}
