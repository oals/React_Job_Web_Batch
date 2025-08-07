package com.example.jobx_batch.scheduler;

import jakarta.annotation.PostConstruct;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job selectJobInfoJob;
    private final Job selectJobNewsJob;
    private Scheduler scheduler;

    public BatchScheduler(JobLauncher jobLauncher,
                          @Qualifier("selectJobInfo") Job selectJobInfoJob,
                          @Qualifier("selectJobNews") Job selectJobNewsJob) {
        this.jobLauncher = jobLauncher;
        this.selectJobInfoJob = selectJobInfoJob;
        this.selectJobNewsJob = selectJobNewsJob;
    }

    @PostConstruct
    public void startScheduler() throws SchedulerException {
        scheduler = StdSchedulerFactory.getDefaultScheduler();

        JobDetail selectJobInfoDetail = JobBuilder.newJob(QuartzJob.class)
                .withIdentity("selectJobInfoJob")
                .usingJobData("jobName", "selectJobInfo")
                .build();

        SimpleTrigger selectJobInfoTrigger = TriggerBuilder.newTrigger()
                .withIdentity("selectJobInfoTrigger")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInHours(24 * 14)
                        .repeatForever())
                .build();

        JobDetail selectJobNewsDetail = JobBuilder.newJob(QuartzJob.class)
                .withIdentity("selectJobNewsJob")
                .usingJobData("jobName", "selectJobNews")
                .build();

        CronTrigger selectJobNewsTrigger = TriggerBuilder.newTrigger()
                .withIdentity("selectJobNewsTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 5 0 * * ?"))
                .build();

        scheduler.getContext().put("jobLauncher", jobLauncher);
        scheduler.getContext().put("selectJobInfoJob", selectJobInfoJob);
        scheduler.getContext().put("selectJobNewsJob", selectJobNewsJob);

        scheduler.scheduleJob(selectJobInfoDetail, selectJobInfoTrigger);
        scheduler.scheduleJob(selectJobNewsDetail, selectJobNewsTrigger);

        scheduler.start();
    }

    public static class QuartzJob implements org.quartz.Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            try {
                SchedulerContext schedulerContext = context.getScheduler().getContext();

                JobLauncher jobLauncher = (JobLauncher) schedulerContext.get("jobLauncher");
                Job selectJobInfoJob = (Job) schedulerContext.get("selectJobInfoJob");
                Job selectJobNewsJob = (Job) schedulerContext.get("selectJobNewsJob");

                String jobName = context.getJobDetail().getJobDataMap().getString("jobName");

                Job jobToRun = null;
                if ("selectJobInfo".equals(jobName)) {
                    jobToRun = selectJobInfoJob;
                } else if ("selectJobNews".equals(jobName)) {
                    jobToRun = selectJobNewsJob;
                }

                if (jobToRun != null) {
                    JobParameters params = new JobParametersBuilder()
                            .addLong("time", System.currentTimeMillis())
                            .toJobParameters();

                    jobLauncher.run(jobToRun, params);
                }
            } catch (Exception e) {
                throw new JobExecutionException(e);
            }
        }
    }
}
