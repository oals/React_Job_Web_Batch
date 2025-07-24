package com.example.jobx_batch.config;

import com.example.jobx_batch.client.EmploymentApiClient;
import com.example.jobx_batch.dto.JobDto;
import com.example.jobx_batch.service.JobBatchApiService;
import com.example.jobx_batch.utils.XmlParserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class JobConfig {

    @Value("${employment24.auth.key}")
    private String EMPLOYMENT24_AUTH_KEY;

    private final EmploymentApiClient employmentApiClient;
    private final JobBatchApiService jobBatchApiService;

    @Bean
    public Job selectJobInfo(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        Step step1 = new StepBuilder("stepPrintHello", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("👋 Hello Step 실행 중");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();

        Step stepSelectEmployment24JobList = new StepBuilder("stepPrintWorld", jobRepository)
                .tasklet((contribution, chunkContext) -> {

                    System.out.println("🌍 고용24 직업 리스트 API 호출 Step 실행 중");

                    String target = "JOBCD";
                    String apiUrl = "https://www.work24.go.kr/cm/openApi/call/wk/callOpenApiSvcInfo212L01.do?authKey=%s&returnType=XML&target=%s";
                    String url = String.format(
                            apiUrl,
                            EMPLOYMENT24_AUTH_KEY,
                            target
                    );

                    String responseXml = employmentApiClient.callEmploymentInfo(url);

                    if (responseXml != null) {

                        ExecutionContext jobContext = chunkContext.getStepContext()
                                .getStepExecution()
                                .getJobExecution()
                                .getExecutionContext();

                        XmlParserUtils xmlParserUtils = new XmlParserUtils();
                        List<String> jobCdList = xmlParserUtils.extractJobCds(responseXml);

                        jobCdList.forEach(cd -> System.out.println("💼 직업코드: " + cd));

                        jobContext.put("jobCdList", jobCdList); // ⬅ Job 범위로 저장

                    }

                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();


        Step stepFilterNewJobs = new StepBuilder("stepFinal", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("jobCd 검사!");

                    ExecutionContext jobContext = chunkContext.getStepContext()
                            .getStepExecution()
                            .getJobExecution()
                            .getExecutionContext();

                    List<String> jobCdList = (List<String>) jobContext.get("jobCdList");

                    if (jobCdList != null) {

                        List<String> dbJobCdList = jobBatchApiService.selectJobCdList();

                        if (!dbJobCdList.isEmpty()) {

                            List<String> diffList = new ArrayList<>(jobCdList);

                            diffList.removeAll(dbJobCdList);

                            jobContext.put("jobCdList", diffList);

                            return RepeatStatus.FINISHED;
                        }
                        jobContext.put("jobCdList", null);
                        return RepeatStatus.FINISHED;
                    }
                    throw new IllegalStateException("jobCdList == null");
                }, transactionManager)
                .build();


        Step stepSelectEmployment24JobInfo = new StepBuilder("stepFinal", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("🌍 고용24 직업 상세 API 호출 Step 실행 중");

                    ExecutionContext jobContext = chunkContext.getStepContext()
                            .getStepExecution()
                            .getJobExecution()
                            .getExecutionContext();

                    List<String> jobCdList = (List<String>) jobContext.get("jobCdList");

                    if (jobCdList != null) {

                        String target = "JOBDTL";
                        String apiUrl = "https://www.work24.go.kr/cm/openApi/call/wk/callOpenApiSvcInfo212D01.do?authKey=%s&returnType=XML&target=%s&jobGb=1&jobCd=%s&dtlGb=1";
                        String jobCd = jobCdList.get(0);
                        String url = String.format(
                                apiUrl,
                                EMPLOYMENT24_AUTH_KEY,
                                target,
                                jobCd
                        );

                        String responseXml = employmentApiClient.callEmploymentInfo(url);

                        if (responseXml != null) {

                            XmlParserUtils xmlParserUtils = new XmlParserUtils();
                            JobDto jobDto = xmlParserUtils.parse(responseXml);

                            jobBatchApiService.insertJob(jobDto);

                        }

                    }

                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();

        Step step4 = new StepBuilder("stepFinal", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("✅ 모든 스텝 완료!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();

        return new JobBuilder("multiStepJob", jobRepository)
                .start(step1)
                .next(stepSelectEmployment24JobList)
                .next(stepFilterNewJobs)
                .next(stepSelectEmployment24JobInfo)
                .next(step4)
                .build();

    }





}