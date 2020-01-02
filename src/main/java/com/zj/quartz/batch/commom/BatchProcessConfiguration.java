package com.zj.quartz.batch.commom;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@Configuration
public class BatchProcessConfiguration {
    
    private JobRepository jobRepository;
    
    private JobRegistry jobRegistry;
    
//    JobRegistryBeanPostProcessor

}
