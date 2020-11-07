package org.fan.demo.provider.service;

import java.util.List;
import org.fan.demo.provider.model.User;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.11.8 1:36
 */
public interface HelloService {

  String hello();

  List<User> getAll();

}
