package com.example.LabSystemBackend.service.impl;

import com.example.LabSystemBackend.dao.NewsDao;
import com.example.LabSystemBackend.entity.News;
import com.example.LabSystemBackend.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {
    @Autowired
    private NewsDao newsDao;

    @Override
    public int sendNews(int senderId, int receiverId, String content) {
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

    @Override
    public int sendToAllAdmin(String content) {
        return 0;
    }
}
