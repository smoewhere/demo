package org.fan.demo.dynamic.service;

import org.fan.demo.dynamic.model.User;

import java.util.List;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.6.1 16:50
 */
public interface HelloService2 {

    void println(String name);

    List<User> getUsers();
}
