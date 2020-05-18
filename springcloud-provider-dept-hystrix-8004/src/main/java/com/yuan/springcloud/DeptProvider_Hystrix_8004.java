package com.yuan.springcloud;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient //将自己注册到注册中心
@EnableDiscoveryClient//服务发现
@EnableCircuitBreaker//添加对于熔断的支持
public class DeptProvider_Hystrix_8004 {

    public static void main(String[] args) {
        SpringApplication.run(DeptProvider_Hystrix_8004.class,args);
    }
}
