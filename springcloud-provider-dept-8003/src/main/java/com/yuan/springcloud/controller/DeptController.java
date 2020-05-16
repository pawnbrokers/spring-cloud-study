package com.yuan.springcloud.controller;


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

    final
   DeptService deptService;

    @Autowired
    DiscoveryClient discoveryClient;//获取一些配置的信息，得到具体的微服务


    public DeptController(DeptService deptService) {
        this.deptService = deptService;
    }


    @PostMapping("/dept/add")
    public boolean addDept(Dept dept){
        return deptService.addDept(dept);
    }

    @GetMapping("/dept/get/{id}")
    public Dept queryById(@PathVariable("id") Long id){
        return deptService.queryById(id);
    }

    @GetMapping("/dept/list")
    public List<Dept> queryAll(){
        return deptService.queryAll();
    }



//    注册进来的微服务，获得一些消息

    @RequestMapping("/dept/discovery")
    public Object discovery(){
        //获得微服务列表的清单
        List<String> services = discoveryClient.getServices();
        System.out.println("discovery=>services"+ services);

        //得到具体的微服务信息,通过具体的名字来获取
        List<ServiceInstance> instances = discoveryClient.getInstances("SPRINGCLOUD-PROVIDER-DEPT-8001");
        for (ServiceInstance instance : instances) {
            System.out.println(instance.getHost()+"\t"
            +instance.getPort()+"\t"
            +instance.getUri()+ "\t"
            +instance.getServiceId());
        }
        return this.discoveryClient;
    }



}
