package com.zj.quartz.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
//        QuartzScheduler
//        QuartzSchedulerThread
//        StdJDBCDelegate
        SpringApplication.run(App.class, args);
    }
}
