package com.zj.quartz.batch.commom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.FlowBuilder.SplitBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.JobFlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.CallableTaskletAdapter;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.CollectionUtils;

import com.zj.quartz.batch.commom.batch.mapper.model.BizBatchJobInfo;

import lombok.Data;

@DisallowConcurrentExecution
public class BatchQuartzJob extends QuartzJobBean {

    private static ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    static {
        executor.setMaxPoolSize(10);
        executor.setCorePoolSize(10);
        executor.setQueueCapacity(Integer.MAX_VALUE);
        executor.afterPropertiesSet();
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            JobKey key = context.getJobDetail().getKey();
            BizBatchJobInfo queryInfo = new BizBatchJobInfo();
            queryInfo.setJobName(key.getName());
            queryInfo.setGroupName(key.getGroup());
            List<BizBatchJobInfo> jobInfos = BatchQuartzHelpFactory.getBizBatchJobInfoMapper().select(queryInfo);
            if (CollectionUtils.isEmpty(jobInfos)) {
                return;
            }
            Collections.sort(jobInfos, new Comparator<BizBatchJobInfo>() {
                @Override
                public int compare(BizBatchJobInfo o1, BizBatchJobInfo o2) {
                    if (o1.getPriorityOrder() == o2.getPriorityOrder()) {
                        return 0;
                    }
                    return o1.getPriorityOrder() > o2.getPriorityOrder() ? -1 : 1;
                }
            });

            JobBuilder jobBuilder = BatchQuartzHelpFactory.getJobBuilderFactory().get(key.toString());
            Map<String, JobParameter> params = new HashMap<String, JobParameter>();
            JobFlowBuilder simpleJobBuilder = null;

            BizBatchJobInfoView curInfoView = BizBatchJobInfoView.build(jobInfos);
            while (curInfoView != null) {
                List<BizBatchJobInfo> curJobInfos = curInfoView.getJobInfos();
                String curGroupStepName = curJobInfos.get(0).getStepName();
                SplitBuilder<Flow> split = new FlowBuilder<Flow>(curGroupStepName).split(executor);
                Flow[] flows = new Flow[curJobInfos.size()];
                for (int i = 0; i < curJobInfos.size(); i++) {
                    BizBatchJobInfo bizBatchJobInfo = curJobInfos.get(i);
                    flows[i] =
                            new FlowBuilder<Flow>(curGroupStepName).start(buildJobStep(params, bizBatchJobInfo)).end();
                }
                Flow curFlow = split.add(flows).build();
                if (simpleJobBuilder == null) {

                    simpleJobBuilder = jobBuilder.start(curFlow);
                } else {
                    simpleJobBuilder.next(curFlow);
                }
                curInfoView = curInfoView.getChildView();
            }

            Job job = simpleJobBuilder.build().listener(new JobExecutionListener() {

                @Override
                public void beforeJob(JobExecution jobExecution) {
                    System.out.println("before");
                }

                @Override
                public void afterJob(JobExecution jobExecution) {
                    System.out.println("afterJob");
                }
            }).build();
            String uuid = UUID.randomUUID().toString();
            params.put(uuid, new JobParameter(uuid, true));
            JobParameters jobParameters = new JobParameters(params);
            BatchQuartzHelpFactory.getJobLauncher().run(job, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private TaskletStep buildJobStep(Map<String, JobParameter> params, BizBatchJobInfo bizBatchJobInfo) {
        params.put(bizBatchJobInfo.getStepName(), new JobParameter(bizBatchJobInfo.getBodyString(), true));
        StepBuilder stepBuilder = BatchQuartzHelpFactory.getStepBuilderFactory().get(bizBatchJobInfo.getStepName())
                .allowStartIfComplete(true);
        CallableTaskletAdapter callableTaskletAdapter = new CallableTaskletAdapter();
        callableTaskletAdapter.setCallable(new Callable<RepeatStatus>() {

            @Override
            public RepeatStatus call() throws Exception {
                System.out.println(new Date());
                System.out.println(bizBatchJobInfo.getStepName() + ":" + bizBatchJobInfo.getBodyString());
                Thread.sleep(3000);
                return RepeatStatus.FINISHED;
            }
        });
        TaskletStep build = stepBuilder.tasklet(callableTaskletAdapter).build();
        return build;
    }

    @Data
    static class BizBatchJobInfoView {

        private List<BizBatchJobInfo> jobInfos = new ArrayList<BizBatchJobInfo>();

        private BizBatchJobInfoView childView;

        public void addBizBatchJobInfo(BizBatchJobInfo bizBatchJobInfo) {
            jobInfos.add(bizBatchJobInfo);
        }

        public static BizBatchJobInfoView build(List<BizBatchJobInfo> jobInfos) {
            int curOrder = jobInfos.get(0).getPriorityOrder();
            BizBatchJobInfoView bizBatchJobInfoView = new BizBatchJobInfoView();
            BizBatchJobInfoView curBizBatchJobInfoView = bizBatchJobInfoView;

            for (BizBatchJobInfo bizBatchJobInfo : jobInfos) {
                if (curOrder == bizBatchJobInfo.getPriorityOrder()) {
                    curBizBatchJobInfoView.addBizBatchJobInfo(bizBatchJobInfo);
                } else {
                    curOrder = bizBatchJobInfo.getPriorityOrder();
                    BizBatchJobInfoView childBatchInfo = new BizBatchJobInfoView();
                    childBatchInfo.addBizBatchJobInfo(bizBatchJobInfo);
                    curBizBatchJobInfoView.setChildView(childBatchInfo);
                    curBizBatchJobInfoView = childBatchInfo;
                }
            }

            return bizBatchJobInfoView;
        }

    }

}