package com.example.jobx_batch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(
		exclude = org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration.class
)
@EnableBatchProcessing(modular = true)
public class JobxBatchApplication {
	public static void main(String[] args) {
		SpringApplication.run(JobxBatchApplication.class, args);
	}
}