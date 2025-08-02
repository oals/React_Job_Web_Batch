package com.example.jobx_batch.reader;


import com.example.jobx_batch.client.EmploymentApiClient;
import com.example.jobx_batch.dto.NewsDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

            String query = "취업";
            String apiUrl = "https://newsapi.org/v2/everything?q=" + query + "&language=ko&apiKey=" + authKey;

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
            return null;
        }

    }


}