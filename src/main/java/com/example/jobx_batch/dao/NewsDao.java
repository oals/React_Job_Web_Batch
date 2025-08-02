package com.example.jobx_batch.dao;

import com.example.jobx_batch.dto.NewsDto;
import com.example.jobx_batch.dto.SourceDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NewsDao {

    void insertNews(NewsDto newsDto);

    void insertNewsSource(SourceDto sourceDto);

    String selectNewsSource(SourceDto sourceDto);

}
