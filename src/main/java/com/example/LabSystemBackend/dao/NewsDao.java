package com.example.LabSystemBackend.dao;

import com.example.LabSystemBackend.entity.News;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NewsDao {
    int insertNews(@Param("news") News news);

    List<News> getAllNews();

    News getUserAllNews(@Param("receiverId") int receiverId);


}
