package com.example.jobx_batch.service;

import com.example.jobx_batch.dto.JobInfoDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobBatchApiServiceImpl implements JobBatchApiService{

    @Override
    public List<JobInfoDto> fetchJobListFromApi() {
        return List.of(new JobInfoDto("백엔드 개발자test", "test", 4500, 7000, 10000));

    }
}
