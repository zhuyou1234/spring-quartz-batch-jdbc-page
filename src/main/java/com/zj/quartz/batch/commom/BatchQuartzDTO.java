package com.zj.quartz.batch.commom;

import java.util.List;

import lombok.Data;

public class BatchQuartzDTO {
    private String jName;
    private String jGroup;
    private String tName;
    private String tGroup;
    private String cron;
    private List<BatchDTO> batchs;
    public String getjName() {
        return jName;
    }
    public void setjName(String jName) {
        this.jName = jName;
    }
    public String getjGroup() {
        return jGroup;
    }
    public void setjGroup(String jGroup) {
        this.jGroup = jGroup;
    }
    public String gettName() {
        return tName;
    }
    public void settName(String tName) {
        this.tName = tName;
    }
    public String gettGroup() {
        return tGroup;
    }
    public void settGroup(String tGroup) {
        this.tGroup = tGroup;
    }
    public String getCron() {
        return cron;
    }
    public void setCron(String cron) {
        this.cron = cron;
    }
    public List<BatchDTO> getBatchs() {
        return batchs;
    }
    public void setBatchs(List<BatchDTO> batchs) {
        this.batchs = batchs;
    }

}
