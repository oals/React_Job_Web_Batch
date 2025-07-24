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
    private final Job selectJobInfo;

    public JobBatchController(JobLauncher jobLauncher, @Qualifier("selectJobInfo") Job selectJobInfo) {
        this.jobLauncher = jobLauncher;
        this.selectJobInfo = selectJobInfo;
    }


    @GetMapping("/batch/job/run")
    public String jobStart() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addString("runTime", LocalDateTime.now().toString()) // JobInstance 식별용
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(selectJobInfo, params);
            return "Batch started: " + execution.getStatus();
        } catch (Exception e) {
            e.printStackTrace();
            return "Batch failed: " + e.getMessage();
        }
    }

}
