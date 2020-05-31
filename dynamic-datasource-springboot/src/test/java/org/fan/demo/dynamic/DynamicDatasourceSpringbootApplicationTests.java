package org.fan.demo.dynamic;

import org.fan.demo.dynamic.config.properties.TestProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class DynamicDatasourceSpringbootApplicationTests {


    @Resource
    private TestProperties testProperties;


    @Test
    void contextLoads() {
        System.out.println(testProperties.toString());
    }

}
