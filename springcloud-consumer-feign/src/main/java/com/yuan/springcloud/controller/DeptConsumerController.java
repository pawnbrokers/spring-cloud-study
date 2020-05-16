package com.yuan.springcloud.controller;

import com.yuan.springcloud.pojo.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class DeptConsumerController {

    //怎样调用service？
    //消费者不需要service，只需要请求到url即可
    //restFul请求,RestTemplate处理对应请求，供我们直接调用！
    //先要注册到spring中，没有bean，手动注册
    //(url,实体，Class<T> responseType)
    @Autowired
    private RestTemplate restTemplate;//提供多种便捷访问远程http服务的方法，是一种简单的restFul服务模板

//    private static final String REST_URL_PREFIX = "http://localhost:8001";
//用ribbon的话就不能写死，应该是一个变量，通过注册中心的服务名来访问
private static final String REST_URL_PREFIX = "http://SPRINGCLOUD-PROVIDER-DEPT-8001";

    @RequestMapping("/consumer/dept/get/{id}")
    public Dept get(@PathVariable("id")int id){
        return restTemplate.getForObject(REST_URL_PREFIX+"/dept/get/"+id,Dept.class);
    }


    @RequestMapping("/consumer/dept/add")
    public boolean add(Dept dept){
        return restTemplate.postForObject(REST_URL_PREFIX+"/dept/add",dept,Boolean.class);
    }

    @RequestMapping("/consumer/dept/list")
    public List<Dept> list(){
        return restTemplate.getForObject(REST_URL_PREFIX+"/dept/list", List.class);
    }




}
