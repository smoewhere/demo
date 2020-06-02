package org.fan.demo.dynamic;

import lombok.extern.slf4j.Slf4j;
import org.fan.demo.dynamic.model.User;
import org.fan.demo.dynamic.service.HelloService;
import org.fan.demo.dynamic.service.impl.HelloServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@SpringBootTest
class DynamicDatasourceSpringbootApplicationTests {


    @Resource
    private HelloServiceImpl helloService;


    @Test
    public void testHelloService() {
        User user = new User();
        user.setAddress("北京");
        user.setName("jack");
        int insert = helloService.insert(user);
        log.info("primary key is {}",insert);
        user.setId(null);
        helloService.insert1(user);
    }

}
