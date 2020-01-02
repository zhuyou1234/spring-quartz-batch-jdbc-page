package com.zj.quartz.batch.commom.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.zj.quartz.batch.commom.BatchDTO;
import com.zj.quartz.batch.commom.BatchQuartzDTO;
import com.zj.quartz.batch.commom.QuartzBatchService;
import com.zj.quartz.batch.commom.batch.mapper.dao.BizBatchJobInfoMapper;
import com.zj.quartz.batch.commom.batch.mapper.dao.BizJobInfoMapper;
import com.zj.quartz.batch.commom.batch.mapper.model.BizBatchJobInfo;
import com.zj.quartz.batch.commom.batch.mapper.model.BizJobInfo;
import com.zj.quartz.batch.commom.quartz.QuartzService;

import lombok.Data;

@RestController
@RequestMapping("/batchQuartz")
@Transactional
public class BatchQuartzController {

    @Autowired
    private QuartzBatchService quartzBatchService;

    @Autowired
    private QuartzService quartzService;

    @Autowired
    private BizBatchJobInfoMapper bizBatchJobInfoMapper;

    @Autowired
    private BizJobInfoMapper bBizJobInfoMapper;

    @PostMapping(value = "/addjob")
    public void addjob(String jobName, String groupName, String cronExpression) throws Exception {

        BizBatchJobInfo bbji = new BizBatchJobInfo();
        bbji.setJobName(jobName);
        bbji.setGroupName(groupName);
        List<BizBatchJobInfo> select = bizBatchJobInfoMapper.select(bbji);
        if (CollectionUtils.isEmpty(select)) {
            throw new RuntimeException("fail jobName jobGroupName illegal");
        }

        String jName = jobName;
        String jGroup = groupName;
        String tName = "tName" + jobName;
        String tGroup = "tGroup" + groupName;
        String cron = cronExpression;

        List<BatchDTO> batchs = new ArrayList<BatchDTO>();
        for (BizBatchJobInfo tmp : select) {
            BatchDTO batchDTO = new BatchDTO();
            batchDTO.setBodyString(tmp.getBodyString());
            batchDTO.setOrder(tmp.getPriorityOrder().toString());
            batchDTO.setStepName(tmp.getStepName());
            batchDTO.setRestUrl(tmp.getBodyString());
        }
        quartzBatchService.startBatch(jName, jGroup, tName, tGroup, cron, batchs);

        BizJobInfo bizJobInfo = new BizJobInfo();
        bizJobInfo.setJobName(jobName);
        bizJobInfo.setGroupName(groupName);
        bizJobInfo.setCronExpression(cronExpression);
        bBizJobInfoMapper.insert(bizJobInfo);

    }

    @GetMapping(value = "/queryjob")
    public Map<String, Object> queryBatch(@RequestParam(value = "pageNum") Integer pageNum,
            @RequestParam(value = "pageSize") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<BizJobInfo> selectAll = bBizJobInfoMapper.selectAll();
        List<BizJobInfoView> bizJobInfoViews = new ArrayList<BatchQuartzController.BizJobInfoView>();
        for (BizJobInfo bizJobInfo : selectAll) {
            BizJobInfoView bb = new BizJobInfoView();
            String jobName = bizJobInfo.getJobName();
            bb.setJobName(jobName);
            String groupName = bizJobInfo.getGroupName();
            bb.setGroupName(groupName);
            bb.setCronExpression(bizJobInfo.getCronExpression());
            StringJoiner sj = new StringJoiner("<br/>");
            BizBatchJobInfo bbji = new BizBatchJobInfo();
            bbji.setJobName(jobName);
            bbji.setGroupName(groupName);
            List<BizBatchJobInfo> select = bizBatchJobInfoMapper.select(bbji);
            for (BizBatchJobInfo bizBatchJobInfo : select) {
                sj.add(JSON.toJSONString(bizBatchJobInfo));
            }
            bb.setBatchInfos(sj.toString());
            bizJobInfoViews.add(bb);
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("jobInfos", bizJobInfoViews);
        map.put("number", bizJobInfoViews.size());
        return map;
    }

    @PostMapping(value = "/deletejob")
    public void deletejob(String jobName, String groupName) throws Exception {
        quartzBatchService.shutdownJob(jobName, groupName);
        BizJobInfo bizJobInfo = new BizJobInfo();
        bizJobInfo.setJobName(jobName);
        bizJobInfo.setGroupName(groupName);
        bBizJobInfoMapper.delete(bizJobInfo);
    }

    @Data
    public static class BizJobInfoView {
        private String jobName;
        private String groupName;
        private String cronExpression;
        private String batchInfos;
    }

    /**
     * 新增任务
     */
    @RequestMapping("/add")
    public String insertTask(@RequestBody BatchQuartzDTO batchQuartzDTO) {
        quartzBatchService.addJob(batchQuartzDTO);
        return "添加成功！";
    }

    /**
     * 删除任务
     */
    @GetMapping("/delete")
    public String deleteTask(String jName, String jGroup) {
        quartzService.deleteJob(jName, jGroup);
        return "删除成功！";
    }

    public static void main(String[] args) {
        BatchQuartzDTO batchQuartzDTO = new BatchQuartzDTO();
        batchQuartzDTO.setjGroup("zjtestjg4");
        batchQuartzDTO.setjName("zjtestj4");
        batchQuartzDTO.settGroup("zjtestt4");
        batchQuartzDTO.settName("zjtestt4");
        batchQuartzDTO.setCron("0/10 * * * * ?");
        List<BatchDTO> batchs = new ArrayList<BatchDTO>();
        batchs.add(createBtd("1"));
        batchs.add(createBtd("2"));
        batchs.add(createBtd("2"));
        batchs.add(createBtd("3"));
        batchs.add(createBtd("4"));
        batchs.add(createBtd("4"));
        batchQuartzDTO.setBatchs(batchs);
        System.out.println(JSON.toJSONString(batchQuartzDTO));

    }

    private static BatchDTO createBtd(String order) {
        BatchDTO bdt = new BatchDTO();
        bdt.setBodyString(order);
        bdt.setOrder(order);
        bdt.setRestUrl(order);
        bdt.setStepName(UUID.randomUUID().toString() + order);
        return bdt;
    }

}
