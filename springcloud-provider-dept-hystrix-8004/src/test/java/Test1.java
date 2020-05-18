import com.yuan.springcloud.pojo.Dept;
import com.yuan.springcloud.service.DeptServiceImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class Test1 {

    @Autowired
    DeptServiceImpl deptService;


    @Test
    public void test1(){
        Dept dept = deptService.queryById(1L);
        System.out.println(dept);
    }
}
