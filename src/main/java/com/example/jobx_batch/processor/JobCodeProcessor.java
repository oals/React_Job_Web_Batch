package com.example.jobx_batch.processor;

import com.example.jobx_batch.service.JobBatchApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;

@RequiredArgsConstructor
public class JobCodeProcessor implements ItemProcessor<String, String> {

    private final JobBatchApiService jobBatchApiService;
    private List<String> dbJobCdList;

    @Override
    public String process(String jobCd) throws Exception {
        if (dbJobCdList == null) {
            dbJobCdList = jobBatchApiService.selectJobCdList();
        }

        if (dbJobCdList.contains(jobCd)) {
            return null;
        }
        return jobCd;
    }
}
