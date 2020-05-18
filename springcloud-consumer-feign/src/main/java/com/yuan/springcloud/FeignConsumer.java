package com.yuan.springcloud;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

//ribbon和eureka整合之后，客户端可以直接调用方法，不用关心ip地址和端口号
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.yuan.springcloud"})
public class FeignConsumer {
    public static void main(String[] args) {
        SpringApplication.run(FeignConsumer.class,args);
    }
}
