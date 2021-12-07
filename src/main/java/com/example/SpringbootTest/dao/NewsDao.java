package com.example.SpringbootTest.dao;

import com.example.SpringbootTest.entity.News;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NewsDao {
    int insertNews(@Param("news") News news);

    List<News> getAllNews();

    News getUserAllNews(@Param("receiverId") int receiverId);


}
