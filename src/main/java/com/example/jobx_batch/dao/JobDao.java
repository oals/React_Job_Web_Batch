package com.example.jobx_batch.dao;

import com.example.jobx_batch.dto.JobDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JobDao {

    List<String> selectJobCdList();

    void insertJob(JobDto jobDto);

    // 자격증 여러 건 insert (한 번에)
    void insertCerts(@Param("relCertList") List<JobDto.RelCert> relCertList);

    // job_cert_map 매핑 테이블 insert (한 건씩 반복 호출)
    void insertJobCerts(@Param("jobCd") String jobCd, @Param("certNm") String certNm);

    // 전공 여러 건 insert (한 번에)
    void insertMajors(@Param("relMajorList") List<JobDto.RelMajor> relMajorList);

    // job_major_map 매핑 테이블 insert (한 건씩 반복 호출)
    void insertJobMajors(@Param("jobCd") String jobCd,
                         @Param("majorCd") String majorCd,
                         @Param("majorNm") String majorNm);

    // 관련 직업 여러 건 insert (한 번에)
    void insertRelatedJobs(@Param("relJobList") List<JobDto.RelJob> relJobList);

    // 매핑 테이블 insert (한 건씩 반복 호출)
    void insertJobRelMap(@Param("jobCd") String jobCd, @Param("relJobCd") String relJobCd);
}
