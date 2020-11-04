package org.fan.test.controller;

import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.11.4 10:56
 */
@RestController
public class HelloController {

  private static final Logger log = LoggerFactory.getLogger(HelloController.class);

  @RequestMapping("/hello")
  public String hello(){
    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    log.info("[HelloController.hello] this Thread is {}", Thread.currentThread().getName());
    return "hello world";
  }

}
