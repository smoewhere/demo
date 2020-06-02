package org.fan.demo.dynamic.service;

import org.fan.demo.dynamic.annotation.ChooseDataSource;
import org.fan.demo.dynamic.enums.DataBaseType;
import org.fan.demo.dynamic.model.User;

import java.util.List;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.6.1 15:34
 */
public interface HelloService {


    void print(String name);

    List<User> getUsers();

    @ChooseDataSource(DataBaseType.DS2)
    List<User> getUsers2();
}
