package com.example.jobx_batch.service;

import com.example.jobx_batch.dao.JobDao;
import com.example.jobx_batch.dao.NewsDao;
import com.example.jobx_batch.dto.JobDto;
import com.example.jobx_batch.dto.NewsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobBatchApiServiceImpl implements JobBatchApiService{

    private final JobDao jobDao;
    private final NewsDao newsDao;

    @Override
    public List<String> selectJobCdList() {
        return jobDao.selectJobCdList();
    }

    @Override
    public void insertJob(JobDto jobDto) {
        jobDao.insertJob(jobDto);

        if (jobDto.getRelCertList() != null && !jobDto.getRelCertList().isEmpty()) {
            jobDao.insertCerts(jobDto.getRelCertList());

            for (JobDto.RelCert cert : jobDto.getRelCertList()) {
                jobDao.insertJobCerts(jobDto.getJobCd(), cert.getCertNm());
            }
        }

        if (jobDto.getRelMajorList() != null && !jobDto.getRelMajorList().isEmpty()) {
            jobDao.insertMajors(jobDto.getRelMajorList());

            for (JobDto.RelMajor major : jobDto.getRelMajorList()) {
                jobDao.insertJobMajors(jobDto.getJobCd(), major.getMajorCd(), major.getMajorNm());
            }
        }

        if (jobDto.getRelJobList() != null && !jobDto.getRelJobList().isEmpty()) {
            jobDao.insertRelatedJobs(jobDto.getRelJobList());

            for (JobDto.RelJob relJob : jobDto.getRelJobList()) {
                jobDao.insertJobRelMap(jobDto.getJobCd(), relJob.getJobCd());
            }
        }
    }

    @Override
    public void insertNews(NewsDto newsDto) {

        newsDao.insertNewsSource(newsDto.getSource());

        String sourceId = newsDao.selectNewsSource(newsDto.getSource());

        newsDto.setSourceId(sourceId);

        newsDao.insertNews(newsDto);

    }

}
