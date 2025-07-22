package com.example.jobx_batch.writer;

import com.example.jobx_batch.dao.JobDao;
import com.example.jobx_batch.dto.JobInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobItemWriter implements ItemWriter<JobInfoDto> {

    private final JobDao jobDao;

    @Override
    public void write(Chunk<? extends JobInfoDto> chunk) {
        for (JobInfoDto job : chunk.getItems()) {
//            jobDao.insertJob(job);
            System.out.println(job.getName());
        }
    }
}