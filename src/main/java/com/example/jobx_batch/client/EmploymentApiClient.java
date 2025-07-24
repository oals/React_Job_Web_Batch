package com.example.jobx_batch.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class EmploymentApiClient {

    private final RestTemplate restTemplate;

    public EmploymentApiClient() {
        this.restTemplate = new RestTemplate();
    }

    public String callEmploymentInfo(String url) {

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            return response.getBody();
        } catch (Exception e) {
            System.err.println("❌ 고용24 API 호출 오류: " + e.getMessage());
            return null;
        }
    }

}