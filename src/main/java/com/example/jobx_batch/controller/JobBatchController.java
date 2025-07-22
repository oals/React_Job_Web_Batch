package com.example.jobx_batch.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController

public class JobBatchController {

    private final JobLauncher jobLauncher;
    private final Job exampleJob;

    public JobBatchController(JobLauncher jobLauncher, @Qualifier("exampleJob") Job exampleJob) {
        this.jobLauncher = jobLauncher;
        this.exampleJob = exampleJob;
    }

    @GetMapping("/run")
    public String runBatch() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(exampleJob, params);
        return "배치 실행됨";
    }

    @GetMapping("/run2")
    public String runBatc2h() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addString("runTime", LocalDateTime.now().toString()) // JobInstance 식별용
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(exampleJob, params);
            return "Batch started: " + execution.getStatus();
        } catch (Exception e) {
            e.printStackTrace();
            return "Batch failed: " + e.getMessage();
        }
    }

}
