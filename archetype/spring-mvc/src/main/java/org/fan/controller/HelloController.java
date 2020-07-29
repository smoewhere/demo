package org.fan.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.7.30 0:12
 */
@RestController
public class HelloController {

  @RequestMapping(value = "/hello")
  public String hello(){
    return "hello";
  }

}
