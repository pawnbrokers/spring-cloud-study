package tsetAll;

import com.yuan.springcloud.pojo.Dept;
import com.yuan.springcloud.service.DeptServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@SpringBootTest
public class TestDemo {
    @Autowired
    DeptServiceImpl deptService;

    @Autowired
    DataSource dataSource;


    @Test
    public void test(){

//        System.out.println(dataSource.getClass());
        List<Dept> depts = deptService.queryAll();

        for (Dept dept : depts) {
            System.out.println(dept);
        }
    }



}
