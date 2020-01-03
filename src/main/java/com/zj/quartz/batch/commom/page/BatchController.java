package com.zj.quartz.batch.commom.page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.zj.quartz.batch.commom.batch.mapper.dao.BizBatchJobInfoMapper;
import com.zj.quartz.batch.commom.batch.mapper.model.BizBatchJobInfo;

@RestController
@RequestMapping("/batch")
public class BatchController {

    @Autowired
    private BizBatchJobInfoMapper bizBatchJobInfoMapper;

    @PostMapping(value = "/addbatch")
    public void addBatch(String jobName, String stepName, String bodyString, String priorityOrder,String groupName,String restUrl) {
        BizBatchJobInfo bizBatchJobInfo = new BizBatchJobInfo();
        bizBatchJobInfo.setJobName(jobName);
        bizBatchJobInfo.setStepName(stepName);
        bizBatchJobInfo.setBodyString(bodyString);
        bizBatchJobInfo.setPriorityOrder(Integer.valueOf(priorityOrder));
        bizBatchJobInfo.setGroupName(groupName);
        bizBatchJobInfo.setRestUrl(restUrl);
        bizBatchJobInfoMapper.insert(bizBatchJobInfo);
    }

    @GetMapping(value = "/queryBatch")
    public Map<String, Object> queryBatch(@RequestParam(value = "pageNum") Integer pageNum,
            @RequestParam(value = "pageSize") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<BizBatchJobInfo> selectAll = bizBatchJobInfoMapper.selectAll();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("batchInfos", selectAll);
        map.put("number", selectAll.size());
        return map;
    }
    

    @PostMapping(value = "/delete")
    public void deleteBatch(String stepName) {
        BizBatchJobInfo bizBatchJobInfo = new BizBatchJobInfo();
        bizBatchJobInfo.setStepName(stepName);
        bizBatchJobInfoMapper.delete(bizBatchJobInfo);
    }

}
