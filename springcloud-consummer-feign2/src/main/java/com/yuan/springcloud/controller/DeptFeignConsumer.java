package com.yuan.springcloud.controller;

import com.yuan.springcloud.pojo.Dept;
import com.yuan.springcloud.service.DeptFeignServiceNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DeptFeignConsumer {

    @Autowired
    DeptFeignServiceNew deptFeignServiceNew;


    @RequestMapping("/consumer/dept/get/{id}")
    public Dept get(@PathVariable("id")Long id){
        return deptFeignServiceNew.queryById(id);
    }


    @RequestMapping("/consumer/dept/add")
    public boolean add(Dept dept){
        return deptFeignServiceNew.addDept(dept);
    }

    @RequestMapping("/consumer/dept/list")
    public List<Dept> list(){
        return deptFeignServiceNew.queryAll();
    }
}
