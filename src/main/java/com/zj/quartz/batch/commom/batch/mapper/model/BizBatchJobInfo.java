package com.zj.quartz.batch.commom.batch.mapper.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Table(name = "biz_batch_job_info")
@Data
public class BizBatchJobInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "job_name")
    private String jobName;

    @Column(name = "step_name")
    private String stepName;

    @Column(name = "body_string")
    private String bodyString;

    @Column(name = "priority_order")
    private Integer priorityOrder;
    
    @Column(name = "group_name")
    private String groupName;
    
    @Column(name = "rest_url")
    private String restUrl;

}