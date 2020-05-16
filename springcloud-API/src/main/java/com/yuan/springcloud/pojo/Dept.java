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
