package com.zj.quartz.batch.commom;

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zj.quartz.batch.commom.batch.mapper.dao.BizBatchJobInfoMapper;

@Service
public class BatchQuartzHelpFactory {

    private static BizBatchJobInfoMapper bizBatchJobInfoMapper;

    private static JobLauncher jobLauncher;

    private static JobBuilderFactory jobBuilderFactory;
    private static StepBuilderFactory stepBuilderFactory;

    @Autowired
    public void setBizBatchJobInfoMapper(BizBatchJobInfoMapper bizBatchJobInfoMapper) {
        BatchQuartzHelpFactory.bizBatchJobInfoMapper = bizBatchJobInfoMapper;
    }

    @Autowired
    public void setJobLauncher(JobLauncher jobLauncher) {
        BatchQuartzHelpFactory.jobLauncher = jobLauncher;
    }

    @Autowired
    public void setJobBuilderFactory(JobBuilderFactory jobBuilderFactory) {
        BatchQuartzHelpFactory.jobBuilderFactory = jobBuilderFactory;
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        BatchQuartzHelpFactory.stepBuilderFactory = stepBuilderFactory;
    }

    public static BizBatchJobInfoMapper getBizBatchJobInfoMapper() {
        return bizBatchJobInfoMapper;
    }

    public static JobLauncher getJobLauncher() {
        return jobLauncher;
    }

    public static JobBuilderFactory getJobBuilderFactory() {
        return jobBuilderFactory;
    }

    public static StepBuilderFactory getStepBuilderFactory() {
        return stepBuilderFactory;
    }

}
