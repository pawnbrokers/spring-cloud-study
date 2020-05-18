package com.yuan.springcloud.controller;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.yuan.springcloud.pojo.Dept;
import com.yuan.springcloud.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//提供restFul服务
@RestController
public class DeptController {

    @Autowired
    DeptService deptService;

    @HystrixCommand(fallbackMethod = "getHystrix")
    @GetMapping("/dept/get/{id}")
    public Dept get(@PathVariable("id") Long id) {
        Dept dept = deptService.queryById(id);
        if (dept == null) {
            throw new RuntimeException("id==>" + id + ",不存在该用户");
        }
        return dept;
    }

    //备选方案

    public Dept getHystrix(@PathVariable("id") Long id) {
        return new Dept().setDeptno(id)
                .setDname("id==>" + id + ",不存在该用户---@Hystrix")
                .setDb_source("没有此数据库");
    }

}
