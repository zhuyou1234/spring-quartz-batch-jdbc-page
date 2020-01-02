package com.zj.quartz.batch.commom;

import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zj.quartz.batch.commom.batch.mapper.dao.BizBatchJobInfoMapper;
import com.zj.quartz.batch.commom.batch.mapper.model.BizBatchJobInfo;

@Service
public class QuartzBatchService {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private BizBatchJobInfoMapper bizBatchJobInfoMapper;
    
//    JobDetail

    public void addJob(BatchQuartzDTO batchQuartzDTO) {

        String jName = batchQuartzDTO.getjName();
        String jGroup = batchQuartzDTO.getjGroup();
        String tName = batchQuartzDTO.gettName();
        String tGroup = batchQuartzDTO.gettGroup();
        String cron = batchQuartzDTO.getCron();
        List<BatchDTO> batchs = batchQuartzDTO.getBatchs();
        startBatch(jName, jGroup, tName, tGroup, cron, batchs);
    }

    public void startBatch(String jName, String jGroup, String tName, String tGroup, String cron,
            List<BatchDTO> batchs) {
        try {
            JobDetail jobDetail = JobBuilder.newJob(BatchQuartzJob.class).withIdentity(jName, jGroup).build();
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(tName, tGroup).startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
            for (BatchDTO batchDTO : batchs) {
                BizBatchJobInfo bizBatchJobInfo = new BizBatchJobInfo();
                BeanUtils.copyProperties(batchDTO, bizBatchJobInfo);
                bizBatchJobInfo.setJobName(jobDetail.getKey().toString());
                bizBatchJobInfo.setPriorityOrder(Integer.valueOf(batchDTO.getOrder()));
                bizBatchJobInfoMapper.insert(bizBatchJobInfo);
            }

            scheduler.start();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutdownJob(String jobName, String groupName) throws Exception {
        scheduler.deleteJob(new JobKey(jobName, groupName));
    }

}
