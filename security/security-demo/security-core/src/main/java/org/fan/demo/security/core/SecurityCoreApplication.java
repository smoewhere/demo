package org.fan.demo.security.core;

import com.fasterxml.jackson.annotation.JsonView;
import org.fan.demo.security.core.dto.User;
import org.fan.demo.security.core.dto.User.UserDetail;
import org.fan.demo.security.core.dto.User.UserSimple;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.10.20 22:20
 */
@SpringBootApplication
@RestController
public class SecurityCoreApplication {

  public static void main(String[] args) {
    SpringApplication.run(SecurityCoreApplication.class, args);
  }

  @RequestMapping(path = "/hello")
  @JsonView(UserSimple.class)
  public User getUser(){
    return new User().setAddress("address").setAge(18).setName("name").setPassword("123456");
  }

  @RequestMapping(path = "/hello2")
  @JsonView(UserDetail.class)
  public User getUser2(){
    return new User().setAddress("address").setAge(18).setName("name").setPassword("123456");
  }
}
