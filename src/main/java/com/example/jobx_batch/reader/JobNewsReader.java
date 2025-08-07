package com.example.jobx_batch.reader;


import com.example.jobx_batch.client.EmploymentApiClient;
import com.example.jobx_batch.dto.NewsDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
public class JobNewsReader implements ItemReader<NewsDto> {

    private final EmploymentApiClient employmentApiClient;
    private final String authKey;

    private Iterator<NewsDto> jobNewsIterator;

    @Override
    public NewsDto read() throws Exception {

        if (jobNewsIterator == null) {

            String query = "취업 OR 직업 OR 채용 OR 국비학원 OR 국비교육 OR 부트캠프";

            LocalDate yesterday = LocalDate.now().minusDays(1);
            String dateStr = yesterday.format(DateTimeFormatter.ISO_DATE); // "yyyy-MM-dd"

            String apiUrl = "https://newsapi.org/v2/everything?q=" + query +
                    "&language=ko" +
                    "&from=" + dateStr +
                    "&to=" + dateStr +
                    "&apiKey=" + authKey;

            System.out.println(apiUrl);
            String responseJson = employmentApiClient.callEmploymentInfo(apiUrl);

            if (responseJson != null) {
                System.out.println(responseJson);

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(responseJson);
                JsonNode articles = root.path("articles");

                if (articles.isArray()) {
                    List<NewsDto> newsList = objectMapper.convertValue(
                            articles,
                            new TypeReference<>() {}
                    );
                    jobNewsIterator = newsList.iterator();
                } else {
                    jobNewsIterator = Collections.emptyIterator();
                }
            } else {
                jobNewsIterator = Collections.emptyIterator();
            }
        }

        if (jobNewsIterator.hasNext()) {
            return jobNewsIterator.next();
        } else {
            jobNewsIterator = null;
            return null;
        }

    }


}