package com.zj.quartz.batch.commom.batch.mapper.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Table(name = "biz_job_info")
@Data
public class BizJobInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "job_name")
    private String jobName;
    
    @Column(name = "group_name")
    private String groupName;
    
    @Column(name = "cron_expression")
    private String cronExpression;

}