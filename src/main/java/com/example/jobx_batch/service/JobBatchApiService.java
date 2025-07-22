package com.example.jobx_batch.service;

import com.example.jobx_batch.dto.JobInfoDto;

import java.util.List;

public interface JobBatchApiService {

    List<JobInfoDto> fetchJobListFromApi();

}
