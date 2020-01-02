package com.zj.quartz.batch.commom;

import lombok.Data;

@Data
public class BatchDTO {
    private String stepName;
    private String bodyString;
    private String restUrl;
    private String order;

}
