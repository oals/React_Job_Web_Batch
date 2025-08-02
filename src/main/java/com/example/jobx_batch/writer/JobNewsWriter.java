package com.example.jobx_batch.writer;

import com.example.jobx_batch.client.EmploymentApiClient;
import com.example.jobx_batch.dto.JobDto;
import com.example.jobx_batch.dto.NewsDto;
import com.example.jobx_batch.service.JobBatchApiService;
import com.example.jobx_batch.utils.XmlParserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@RequiredArgsConstructor
public class JobNewsWriter implements ItemWriter<NewsDto> {

    private final JobBatchApiService jobBatchApiService;

    @Override
    public void write(Chunk<? extends NewsDto> items) throws Exception {
        for (NewsDto news : items) {
            System.out.println("Write News: " + news.getTitle());
            jobBatchApiService.insertNews(news);
        }
    }

}
