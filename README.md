# 1. Spring Cloud与Spring Boot的关系

<!-- more -->

+ Spring Boot专注于快速方便地开发**单个个体微服务**
+ Spring Cloud是关注于全局的微服务协调整理框架，它将Spring Boot开发的一个个单体服务整合并管理起来，为各个微服务之间提供：配置管理，服务发现，断路器，路由，微代理，事件总线，全局锁，决策竞选，分布式会话等等集成服务。
+ Spring Boot可以离开Spring Cloud单独使用，开发项目，但是Spring Cloud离不开Spring Boot，属于依赖关系。
+ **Spring Boot专注于快速、方便地开发单个个体微服务，Spring Cloud关注全局的服务治理框架。**



# 2. 网站架构

![image-20200516213627056](https://i.loli.net/2020/05/16/QVycvMA68OG7XNl.png)



# 3. Dubbo和Spring Cloud区别

**最大区别：Spring Cloud抛弃了Dubbo的RPC通信，采用的是基于HTTP的REST方式**

严格来说，两种方式各有优劣。虽然从一定程度来说，后者牺牲了服务调用的性能，但是也避免了原生RPC带来的问题，而且REST相比于RPC更加灵活，服务消费防御调用方的依赖只依靠一纸契约，不存在代码级别的强依赖，这才强调快读演化的微服务环境下，显得更加合适。

**解决问题的领域不一样：Dubbo的定位是一款RPC框架，Spring Cloud的目标是微服务架构下的一站式解决方案**



# 4. 先不用Eureka，实现Rest通信

## 4.0 父模块依赖(主要是依赖管理)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.example</groupId>
    <artifactId>springcloud</artifactId>
    <version>1.0-SNAPSHOT</version>
    <modules>
        <module>springcloud-API</module>
        <module>springcloud-provider-dept-8001</module>
        <module>testDB</module>
        <module>springcloud-consumer</module>
        <module>springcloud-Eueka-7001</module>
        <module>springcloud-Eureka-7002</module>
        <module>springcloud-Eureka-7003</module>
        <module>springcloud-provider-dept-8002</module>
        <module>springcloud-provider-dept-8003</module>
        <module>springcloud-consumer-feign</module>
    </modules>
    <packaging>pom</packaging>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <junit.version>4.12</junit.version>

        <log4j.version>1.2.17</log4j.version>
    </properties>


    <dependencyManagement>
        <dependencies>
            <!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-dependencies -->
            <!--            springcloud的依赖-->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Greenwich.SR4</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!--            springboot-->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>2.1.4.RELEASE</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>druid</artifactId>
                <version>1.1.10</version>
            </dependency>
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>5.1.47</version>
            </dependency>

            <!--            springboot启动器-->
            <dependency>
                <groupId>org.mybatis.spring.boot</groupId>
                <artifactId>mybatis-spring-boot-starter</artifactId>
                <version>1.3.2</version>
            </dependency>


            <!--            日志以及测试-->
            <!--            juint-->
            <dependency>
                <groupId>junit</groupId>
                <artifactId>junit</artifactId>
                <version>${junit.version}</version>


            </dependency>
            <dependency>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>1.18.12</version>
            </dependency>

            <dependency>
                <groupId>log4j</groupId>
                <artifactId>log4j</artifactId>

                <version>@{log4j.version}</version>
            </dependency>
            <dependency>
                <groupId>ch.qos.logback</groupId>
                <artifactId>logback-core</artifactId>
                <version>1.2.3</version>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>
```



## 4.1 springcloud-API包

主要实现的功能就只是pojo，即类表关系映射

四步走：

1. 引入依赖
2. 编写配置文件
3. 开启功能@Enable
4. 需要的话写配置类

![image-20200516214405542](https://i.loli.net/2020/05/16/OBNLo96caHbUkV7.png)

这里我们不需要配置文件和配置类，引入依赖只有一个lombok



### 4.1.1 数据库表

![image-20200516214459670](https://i.loli.net/2020/05/16/G4pVfJbqKjl96AR.png)

### 4.1.2 Pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springcloud</artifactId>
        <groupId>org.example</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>springcloud-API</artifactId>

<!--当前moudle要用到的包-->
    <dependencies>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
</project>
```

### 4.1.3 实体类

![image-20200516214602786](https://i.loli.net/2020/05/16/eJIQ6shkZO8GniT.png)

```java
package com.yuan.springcloud.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
//第一步序列化
@Data
@NoArgsConstructor
@Accessors(chain = true)//支持链式写法
public class Dept implements Serializable {//类表关系映射
    private Long deptno;
    private String dname;
    private String db_source;//看一下这个数据是存在于哪个数据库的，微服务一个服务对应一个数据库，统一的信息可能存在不同的数据库

    public Dept(String dname){
        this.dname = dname;
    }
/*dept.set1().set()*/
}
```



## 4.2 服务提供者（都是微服务）

1. 引入依赖
2. 编写配置文件
3. 开启功能@Enable
4. 需要的话写配置类

### 4.2.1 项目结构

![image-20200516214836962](https://i.loli.net/2020/05/16/jGYacKV4oiuSrRT.png)

### 4.2.2 pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springcloud</artifactId>
        <groupId>org.example</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>springcloud-provider-dept-8001</artifactId>
    <dependencies>
<!--        首先我们需要拿到实体类-->
        <dependency>
            <groupId>org.example</groupId>
            <artifactId>springcloud-API</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

<!--        junit-->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
        </dependency>

        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-core</artifactId>
        </dependency>

        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
        </dependency>
<!--test-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-test</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>


<!--        服务器-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jetty</artifactId>
            <version>2.1.4.RELEASE</version>
        </dependency>

<!--        热部署工具-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <version>2.1.4.RELEASE</version>
        </dependency>

    </dependencies>
</project>
```

### 4.2.3 配置文件

```yml
server:
  port: 8001
  
mybatis:
  mapper-locations: classpath:com/yuan/springcloud/dao/*.xml
  type-aliases-package: com.yuan.springcloud
  config-location: classpath:com/yuan/springcloud/mybatis-config.xml
  
#   数据源
spring:
  application:
    name: springcloud-provider-dept-8001
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: org.gjt.mm.mysql.Driver
    url: jdbc:mysql://localhost:3306/db01?useUnicode=true&characterEncoding=utf-8
    username: root
    password: root
```

### 4.2.4 对应的Dao，Service，Controller

1. DeptDao

```java
package com.yuan.springcloud.service;

import com.yuan.springcloud.pojo.Dept;

import java.util.List;

public interface DeptService {

    public boolean addDept(Dept dept);

    public Dept queryById(Long id);

    public List<Dept> queryAll();
}
```

2. DeptDao.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.yuan.springcloud.dao.DeptDao">

    <insert id="addDept" parameterType="Dept">
        insert into dept (dname,db_source) values (#{dname},DATABASE())
    </insert>

    <select id="queryById" resultType="Dept" parameterType="Long">
        select * from dept where deptno = #{deptno}
    </select>

    <select id="queryAll" resultType="Dept">
        select * from dept
    </select>

</mapper>
```

3. DeptService

```java
package com.yuan.springcloud.service;

import com.yuan.springcloud.pojo.Dept;

import java.util.List;

public interface DeptService {

    public boolean addDept(Dept dept);

    public Dept queryById(Long id);

    public List<Dept> queryAll();
}
```

4. DeptServiceImpl

```java
package com.yuan.springcloud.service;

import com.yuan.springcloud.dao.DeptDao;
import com.yuan.springcloud.pojo.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {
    final
    DeptDao deptDao;

    public DeptServiceImpl(DeptDao deptDao) {
        this.deptDao = deptDao;
    }

    @Override
    public boolean addDept(Dept dept) {
        return deptDao.addDept(dept);
    }

    @Override
    public Dept queryById(Long id) {
        return deptDao.queryById(id);
    }

    @Override
    public List<Dept> queryAll() {
        return deptDao.queryAll();
    }
}
```

5. DeptController

```java
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
}
```

这里我们采用**@RestController**，向其他服务提供数据即可，调用者通关Http请求来获得数据，我们先单独测试，这个微服务是否可用

### 4.2.5 主配置文件

```java
@SpringBootApplication
public class DeptProvider_8001 {
    public static void main(String[] args) {
        SpringApplication.run(DeptProvider_8001.class,args);
    }
}
```

### 4.2.6 不需要启用什么服务，也不需要编写配置类，直接访问测试

![image-20200516215846555](https://i.loli.net/2020/05/16/eYUJGjt8MvCfnoy.png)

## 4.3 服务调用者

1. 引入依赖
2. 编写配置文件
3. 开启功能@Enable
4. 需要的话写配置类

### 4.3.1 项目结构

![image-20200516220013630](https://i.loli.net/2020/05/16/L1v6WOGAHeQk8Jh.png)

这里我们暂时并不需要config和myrule程序包

### 4.3.2 pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springcloud</artifactId>
        <groupId>org.example</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>springcloud-consumer</artifactId>

    <!--    导包，实体类+web-->

    <dependencies>
        <dependency>
            <groupId>org.example</groupId>
            <artifactId>springcloud-API</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!--        热部署工具-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <version>2.1.4.RELEASE</version>
        </dependency>
    </dependencies>
</project>
```

### 4.3.3 配置文件

```yml
server:
  port: 8888
```

### 4.3.4 Controller

```java
package com.yuan.springcloud.controller;

import com.yuan.springcloud.pojo.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    private static final String REST_URL_PREFIX = "http://localhost:8001";
//用ribbon的话就不能写死，应该是一个变量，通过注册中心的服务名来访问
//private static final String REST_URL_PREFIX = "http://SPRINGCLOUD-PROVIDER-DEPT-8001";

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
```

### 4.3.5 RestTemplate

容器中并没有这样一个模板类，所以我们要自己写对应的配置类，将其加入容器中

```java
package com.yuan.springcloud.config;


import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import com.yuan.myrule.MyRandomRule;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ConfigBean {

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

}
```

**主启动类正常就好，没有额外注解**

### 4.3.6 测试

![image-20200516220653117](https://i.loli.net/2020/05/16/GQROmIeiTuhHX3t.png)

# 5. Eureka注册中心

## 5.1 CAP原则

![image-20200516221140300](https://i.loli.net/2020/05/16/DX2M13U5aqVvcB8.png)

![image-20200516221156157](https://i.loli.net/2020/05/16/bDzH8nqBheMymAN.png)

![image-20200516221211311](https://i.loli.net/2020/05/16/GcaOjX1JZ53dEQv.png)

## 5.2 注册中心Server

### 5.2.1 项目结构

![image-20200516221353947](https://i.loli.net/2020/05/16/taYB4QD8IrNezAk.png)

### 5.2.2 Pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springcloud</artifactId>
        <groupId>org.example</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>springcloud-Eueka-7001</artifactId>

<!--   导报-->
    <dependencies>
        <!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-eureka-server -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka-server</artifactId>
            <version>1.4.6.RELEASE</version>
        </dependency>
        <!--        热部署工具-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <version>2.1.4.RELEASE</version>
        </dependency>

    </dependencies>
</project>
```

这里引入的是EurekaServer包

### 5.2.3 application.yml

```yml
server:
  port: 7001
 # Euraka配置
eureka:
  instance:
    hostname: eureka7001.com # Eureka服务端的实例名称
  client:
    register-with-eureka: false # 表示是否向eureka注册自己
    fetch-registry: false # 为false则表示自己是注册中心，不需要检索这里面的服务
    service-url: # 与注册中心交互的地址，也是一个监控页面
#      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/eureka
      defaultZone: http://eureka7002.com:7002/eureka/,http://eureka7003.com:7003/eureka/
```

如果注册中心是集群的话，就采用下面的配置，实现多个注册中心之间的关联，当然也可以向其他的Eureka中注册自己

### 5.2.4 主启动类

![image-20200516221625597](https://i.loli.net/2020/05/16/eGPbCp6fvuIVsXZ.png)

```java
@SpringBootApplication
@EnableEurekaServer//开启EurekaServer服务，表示这是一个服务端的启动类，允许别人注册进来，访问地址：localhost:7001即可，注册地址才需要在后面加/eureka
public class EurekaServer_7001 {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServer_7001.class,args);
    }
}
```

**这里需要开启Eureka服务的注解。配置文件均不需要**

### 5.2.5 测试

![image-20200516221738420](https://i.loli.net/2020/05/16/pHy7xJtahANjOzl.png)

这里我们关联其他两个Eureka注册中心，但是此时还没有注册的微服务



# 6. 服务提供者注册服务

![image-20200516222305300](https://i.loli.net/2020/05/16/U65vIMBXFondsr7.png)

## 6.1 pom.xml

```xml
<!--        加入Eureka依赖-->
        <!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-eureka -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka</artifactId>
            <version>1.4.6.RELEASE</version>
        </dependency>
<!--        添加监控信息,完善监控信息-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
```

其实监控信息的包可引可不引，对我们的功能没有影响

## 6.2 application.yml

```yml
server:
  port: 8001

mybatis:
  mapper-locations: classpath:com/yuan/springcloud/dao/*.xml
  type-aliases-package: com.yuan.springcloud
  config-location: classpath:com/yuan/springcloud/mybatis-config.xml


#   数据源
spring:
  application:
    name: springcloud-provider-dept-8001
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: org.gjt.mm.mysql.Driver
    url: jdbc:mysql://localhost:3306/db01?useUnicode=true&characterEncoding=utf-8
    username: root
    password: root
    #Eureka的配置,服务注册到哪里？
eureka:
  client:
    service-url:
      defaultZone: http://localhost:7001/eureka/,http://localhost:7002/eureka/,http://localhost:7003/eureka/
  instance:
    instance-id: springcloud-provider-dept8001 # 修改默认的描述信息
# info
info:
  app.name: yuan_tangbo
  company.name: ZJU
```

## 6.3 我们最主要配置的三样东西：

1. 我们在注册中心的服务名称

![image-20200516222413581](https://i.loli.net/2020/05/16/KgjsJMqY8AmbLn3.png)

2. 我们要注册到哪里

![image-20200516222437985](https://i.loli.net/2020/05/16/opQazjZY2reuTtm.png)

3. info信息，这里就是我们上面引入的默认配置信息的那个包（可写可不写）

![image-20200516222516261](https://i.loli.net/2020/05/16/L9WjNwut1E3K8iT.png)

## 6.4 开启配置

主启动类添加注解

```java
@SpringBootApplication
@EnableEurekaClient //将自己注册到注册中心
@EnableDiscoveryClient//服务发现
public class DeptProvider_8001 {
    public static void main(String[] args) {
        SpringApplication.run(DeptProvider_8001.class,args);
    }
}
```

**这里的服务发现主要就是附加功能，可写可不写，实现我们的一些信息的打印功能**



## 6.5 Controller中的修改

```java
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
```



## 6.6 测试

### 6.6.1 不开启集群

![image-20200516222931827](https://i.loli.net/2020/05/16/PpwMGIngDAkURoT.png)

+ 前者就是我们的服务名称，如果我们建立集群的话，同一个服务一定要保证名称相同
+ 后者就是instanceid，代表每一个独特的微服务，即对应一台服务器



**点击后者就是出现我们定义的info信息：**

![image-20200516223127247](https://i.loli.net/2020/05/16/dJWsIGHiC3Nw4mo.png)

### 6.6.2 开启集群

![image-20200516223403160](https://i.loli.net/2020/05/16/qZjxyJsR2eALnbd.png)

**后面出现了三个微服务，对应三个服务器，显示的就是我们的instanceid**

### 6.6.3 discovery测试

我们进入http://localhost:8001/dept/discovery看一下

![image-20200516223256556](https://i.loli.net/2020/05/16/l9JE7UDxVBKrbYg.png)

**控制台打印：**

![image-20200516223545342](https://i.loli.net/2020/05/16/jEMbBpX1Jk5y3Oc.png)

**一定要保证服务的名称是一样的！！！！**



# 7. Ribbon

![image-20200516223704336](https://i.loli.net/2020/05/16/jRp6lutvMGZd8EL.png)

![image-20200516223718946](https://i.loli.net/2020/05/16/Gr2jPzxLDlVEepS.png)

![image-20200516223729570](https://i.loli.net/2020/05/16/xIBlvzNHoRyqu4K.png)

**nginx是服务器端的负载均衡，相当于又加了一道门，请求过来之后，由服务器进行分配**

**ribbon是进程式的负载均衡，消费者在发送请求的时候，向注册中心获知哪些地址可用，然后自己再从在这些地址中选出一个合适的服务器，直接向其发送请求**



# 8.  消费者注册服务中心

![image-20200516224044960](https://i.loli.net/2020/05/16/sAvSOXV2brEpqWx.png)

## 8.1 pom.xml

```xml
   <!--        加入Eureka依赖-->
        <!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-eureka -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka</artifactId>
            <version>1.4.6.RELEASE</version>
        </dependency>
 <!--        配置Ribbon-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-ribbon</artifactId>
            <version>1.4.6.RELEASE</version>
        </dependency> 
```

## 8.2 application.yml

```yml
server:
  port: 8888
# Eureka集成
eureka:
  client:
    register-with-eureka: false # 不向eureka注册自己
    service-url:
      defaultZone: http://eureka7002.com:7002/eureka/,http://eureka7003.com:7003/eureka/,http://eureka7001.com:7001/eureka/
```

**我们这里注册中心也做了一个集群，但是消费者并不需要向注册中心注册**

## 8.3 主启动类开启设置

```java
package com.yuan.springcloud;

import com.yuan.myrule.MyRandomRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

//ribbon和eureka整合之后，客户端可以直接调用方法，不用关心ip地址和端口号
@SpringBootApplication
@EnableEurekaClient
public class DeptConsumer {
    public static void main(String[] args) {
        SpringApplication.run(DeptConsumer.class,args);
    }
}
```

开启Eureka注册服务即可

## 8.4 Controller

```java
package com.yuan.springcloud.controller;

import com.yuan.springcloud.pojo.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
```

```java
//    private static final String REST_URL_PREFIX = "http://localhost:8001";
//用ribbon的话就不能写死，应该是一个变量，通过注册中心的服务名来访问
private static final String REST_URL_PREFIX = "http://SPRINGCLOUD-PROVIDER-DEPT-8001";
```

**最主要的变化就是这样一个前缀名不要再写死，而是要从注册中心中做负载均衡拉取**

## 8.5 RestTemplate

```java
@Configuration
public class ConfigBean {

    //    配置负载均衡，实现restTemplate
    //IRule接口
    @Bean
    @LoadBalanced//Ribbon的作用
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

//    @Bean
//    public IRule myRule(){
//        return new MyRandomRule();
//    }
    
}
```

这里我们需要在RsetTemplate上开启`@LoadBalanced`注解，开启Ribbon

默认采用的是轮询算法

想要修改算法，即可采用下面的注释，也可采用我们自己写的负载均衡算法

![image-20200516224831050](https://i.loli.net/2020/05/16/qntcXWJu6Ed2eVB.png)



## 8.6 测试

**第一次：**

![image-20200516225020627](https://i.loli.net/2020/05/16/yLgEUpstHIvq3Du.png)

**第二次：**

![image-20200516225034588](https://i.loli.net/2020/05/16/sjJGYOSE8DVLztf.png)

**第三次：**

![image-20200516225115202](https://i.loli.net/2020/05/16/G5TKYUs1eOSizXt.png)

## 8.7 编写我们的自己的算法

先借用随机模板：

实现五次就换服务器

```java'
package com.yuan.myrule;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import org.springframework.boot.context.event.SpringApplicationEvent;
//import edu.umd.cs.findbugs.annotations.SuppressWarnings;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MyRandomRule extends AbstractLoadBalancerRule {

    private int total = 0;
    private int currentIndex = 0;
    public MyRandomRule() {
    }

    //@SuppressWarnings({"RCN_REDUNDANT_NULLCHECK_OF_NULL_VALUE"})
    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            return null;
        } else {
            Server server = null;

            while(server == null) {
                if (Thread.interrupted()) {
                    return null;
                }

                List<Server> upList = lb.getReachableServers();
                List<Server> allList = lb.getAllServers();
                int serverCount =upList.size();
                if (serverCount == 0) {
                    return null;
                }

                if (total<5&&currentIndex<serverCount-1){
                    total++;
                } else if (total==5&&currentIndex<serverCount-1){
                    total=0;
                    currentIndex++;
                }else if(currentIndex>=serverCount-1){
                    currentIndex=0;
                    total=0;
                }

                server = (Server)upList.get(currentIndex);
                if (server == null) {
                    Thread.yield();
                } else {
                    if (server.isAlive()) {
                        return server;
                    }

                    server = null;
                    Thread.yield();
                }
            }

            return server;
        }
    }

    /*protected int chooseRandomInt(int serverCount) {
        return ThreadLocalRandom.current().nextInt(serverCount);
    }*/

    public Server choose(Object key) {
        return this.choose(this.getLoadBalancer(), key);
    }

    public void initWithNiwsConfig(IClientConfig clientConfig) {
    }
}
```

## 8.8 注意

这里注意，当我们写自己的负载均衡算法的时候，一定不要和主启动类在同一级的包下

![image-20200516225314414](https://i.loli.net/2020/05/16/pG1CZj9OSkJrB7Y.png)

**否则会被扫描到，这样的话，这样一个算法就会变成公用的，而非针对我们选定的微服务**

## 8.9 自定义负载均衡算法配置

![image-20200516225418769](https://i.loli.net/2020/05/16/8vWdsTLHwjFxamD.png)

```java
//ribbon和eureka整合之后，客户端可以直接调用方法，不用关心ip地址和端口号
@SpringBootApplication
@EnableEurekaClient
//在微服务启动的时候就能去加载我们自定义的Ribbon类
@RibbonClient(name = "SPRINGCLOUD-PROVIDER-DEPT-8001",configuration = MyRandomRule.class)
public class DeptConsumer {
    public static void main(String[] args) {
        SpringApplication.run(DeptConsumer.class,args);
    }
}
```

这时候我们需要在主启动类上指定我们的负载均衡算法，并在RestTemplate的Bean的配置文件中引入我们的规则。

![image-20200516225508501](https://i.loli.net/2020/05/16/IQDPckWxCYMH6GR.png)

# 9. Feign

![image-20200516225719628](https://i.loli.net/2020/05/16/5os4XMKAtjJYUQ3.png)

