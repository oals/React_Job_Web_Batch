package com.example.jobx_batch.processor;

import com.example.jobx_batch.dto.JobInfoDto;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class JobItemProcessor  implements ItemProcessor<JobInfoDto, JobInfoDto> {

    @Override
    public JobInfoDto process(JobInfoDto item) {
        return item;
    }

}
