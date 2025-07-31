package com.example.jobx_batch.config;

import com.example.jobx_batch.client.EmploymentApiClient;
import com.example.jobx_batch.processor.JobCodeProcessor;
import com.example.jobx_batch.reader.JobCodeReader;
import com.example.jobx_batch.service.JobBatchApiService;
import com.example.jobx_batch.writer.JobInfoWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class JobConfig {

    @Value("${employment24.auth.key}")
    private String EMPLOYMENT24_AUTH_KEY;

    private final EmploymentApiClient employmentApiClient;
    private final JobBatchApiService jobBatchApiService;


    @Bean
    public Job selectJobInfo(JobRepository jobRepository, PlatformTransactionManager transactionManager) {

        Step step = new StepBuilder("jobInfoStep", jobRepository)
                .<String, String>chunk(10, transactionManager)
                .reader(new JobCodeReader(employmentApiClient, EMPLOYMENT24_AUTH_KEY))
                .processor(new JobCodeProcessor(jobBatchApiService))
                .writer(new JobInfoWriter(employmentApiClient, jobBatchApiService, EMPLOYMENT24_AUTH_KEY))
                .build();

        return new JobBuilder("jobInfoJob", jobRepository)
                .start(step)
                .build();

    }





}