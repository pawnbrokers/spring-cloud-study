package com.yuan.springcloud;

import com.yuan.myrule.MyRandomRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

//ribbon和eureka整合之后，客户端可以直接调用方法，不用关心ip地址和端口号
@SpringBootApplication
@EnableEurekaClient
//在微服务启动的时候就能去加载我们自定义的Ribbon类
//@RibbonClient(name = "SPRINGCLOUD-PROVIDER-DEPT-8001", configuration = MyRandomRule.class)
public class DeptConsumer {
    public static void main(String[] args) {
        SpringApplication.run(DeptConsumer.class, args);
    }
}
