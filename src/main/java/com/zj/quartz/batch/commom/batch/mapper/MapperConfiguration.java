package com.zj.quartz.batch.commom.batch.mapper;

import org.springframework.context.annotation.Configuration;

import tk.mybatis.spring.annotation.MapperScan;

@Configuration
@MapperScan(basePackages = "com.zj.quartz.batch.commom.batch.mapper.dao")
// @MapperScan(factoryBean = MapperFactoryBean.class,basePackages = "com.zj.quartz.batch.commom.batch.mapper.dao")
public class MapperConfiguration {

}
