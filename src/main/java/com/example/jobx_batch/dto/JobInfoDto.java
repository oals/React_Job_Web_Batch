package com.example.jobx_batch.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobInfoDto {

    private String name;

    private String description;

    private int salaryEntry;

    private int salaryMid;

    private int salarySenior;

}
