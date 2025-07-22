package com.example.jobx_batch.dao;

import com.example.jobx_batch.dto.JobInfoDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JobDao {

    void insertJob(JobInfoDto job);
}
