package org.fan.demo.dynamic;

import lombok.extern.slf4j.Slf4j;
import org.fan.demo.dynamic.config.properties.DruidDataSourceProperties;
import org.fan.demo.dynamic.config.properties.TestProperties;
import org.fan.demo.dynamic.model.User;
import org.fan.demo.dynamic.service.HelloService;
import org.fan.demo.dynamic.service.HelloService2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@SpringBootTest
class DynamicDatasourceSpringbootApplicationTests {

    @Resource
    private HelloService2 helloService2;



    @Test
    public void testHelloService(){
        List<User> users = helloService2.getUsers();
        log.info("user is {}",users);
    }

}
