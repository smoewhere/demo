package org.fan.demo.provider.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.fan.demo.provider.model.User;
import org.fan.demo.provider.service.HelloService;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.11.8 0:56
 */
public class HelloServiceImpl implements HelloService {

  @Override
  public String hello() {
    return "HelloServiceImpl";
  }

  @Override
  public List<User> getAll() {
    List<User> users = new ArrayList<>();
    users.add(new User().setName("jack").setAddress("address"));
    throw new RuntimeException("rururur");
  }
}
