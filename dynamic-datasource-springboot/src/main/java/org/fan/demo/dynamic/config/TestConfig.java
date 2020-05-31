package org.fan.demo.dynamic.config;

import org.fan.demo.dynamic.config.properties.TestProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.5.31 15:07
 */
@Configuration
@EnableConfigurationProperties(TestProperties.class)
public class TestConfig {

    private TestProperties testProperties;

    public TestConfig(TestProperties testProperties) {
        this.testProperties = testProperties;
    }

    public void print(){
        System.out.println(testProperties.toString());
    }
}
