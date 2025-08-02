package com.example.jobx_batch.service;

import com.example.jobx_batch.dto.JobDto;
import com.example.jobx_batch.dto.NewsDto;

import java.util.List;

public interface JobBatchApiService {

    List<String> selectJobCdList();

    void insertJob(JobDto jobDto);

    void insertNews(NewsDto newsDto);

}
