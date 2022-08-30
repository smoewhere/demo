package org.fan.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.fan.demo.properties.HelloProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Fan
 * @version 1.0
 * @date 2022/8/29 16:33
 */
@Slf4j
@RestController
public class HelloController {

  @Autowired
  private HelloProperty helloProperty;

  @GetMapping("/hello")
  public String hello() {
    log.info("[HelloController.hello] age is {}", helloProperty.getAge());
    return helloProperty.getName();
  }
}
