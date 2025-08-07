package com.example.jobx_batch.writer;

import com.example.jobx_batch.client.EmploymentApiClient;
import com.example.jobx_batch.dto.JobDto;
import com.example.jobx_batch.service.JobBatchApiService;
import com.example.jobx_batch.utils.XmlParserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@RequiredArgsConstructor
public class JobInfoWriter implements ItemWriter<String> {

    private final EmploymentApiClient employmentApiClient;
    private final JobBatchApiService jobBatchApiService;
    private final String authKey;

    @Override
    public void write(Chunk<? extends String> jobCds) throws Exception {
        String target = "JOBDTL";

        for (String jobCd : jobCds) {
            String apiUrl = String.format(
                    "https://www.work24.go.kr/cm/openApi/call/wk/callOpenApiSvcInfo212D01.do?authKey=%s&returnType=XML&target=%s&jobGb=1&jobCd=%s&dtlGb=1",
                    authKey,
                    target,
                    jobCd
            );

            String responseXml = employmentApiClient.callEmploymentInfo(apiUrl);

            if (responseXml != null) {
                XmlParserUtils xmlParserUtils = new XmlParserUtils();
                JobDto jobDto = xmlParserUtils.parse(responseXml);
                System.out.println("Write Job: " + jobDto.getJobMdclNm());
                jobBatchApiService.insertJob(jobDto);
            }
        }
    }

}
