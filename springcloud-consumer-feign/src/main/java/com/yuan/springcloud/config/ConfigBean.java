package com.yuan.springcloud.config;


import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ConfigBean {

    //    配置负载均衡，实现restTemplate
    //IRule接口
    @Bean
    @LoadBalanced//Ribbon的作用
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }


}
