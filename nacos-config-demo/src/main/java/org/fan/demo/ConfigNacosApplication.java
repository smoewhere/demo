package org.fan.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConfigNacosApplication {

  public static void main(String[] args) {
    /* 在SpringBoot 2.4之后，默认不加载bootstrap配置，
       因为在BootstrapApplicationListener类中，会判断是否加载bootstrap。
       当spring.cloud.bootstrap.enabled设置为true的时候，就加载
       不加载是因为bootstrap会启动一个容器去刷新配置
     */
    //System.setProperty("spring.cloud.bootstrap.enabled", "true");
    SpringApplication.run(ConfigNacosApplication.class, args);
  }

}
