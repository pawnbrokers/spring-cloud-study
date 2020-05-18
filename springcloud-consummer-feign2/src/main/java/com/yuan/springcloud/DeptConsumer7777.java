package com.yuan.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.yuan.springcloud"})
public class DeptConsumer7777 {
    public static void main(String[] args) {
        SpringApplication.run(DeptConsumer7777.class,args);
    }
}
