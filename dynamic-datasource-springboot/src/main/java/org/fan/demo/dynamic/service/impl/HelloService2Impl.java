package org.fan.demo.dynamic.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.fan.demo.dynamic.annotation.ChooseDataSource;
import org.fan.demo.dynamic.dao.UserMapper;
import org.fan.demo.dynamic.enums.DataBaseType;
import org.fan.demo.dynamic.model.User;
import org.fan.demo.dynamic.model.UserExample;
import org.fan.demo.dynamic.service.HelloService2;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.6.1 16:50
 */
@Slf4j
@Service
public class HelloService2Impl implements HelloService2 {

    @Resource
    private UserMapper userMapper;

    @Override
    public void println(String name) {
        log.info("do println() name is {}",name);
    }

    @Override
    public List<User> getUsers() {
        UserExample userExample = new UserExample();
        List<User> users = userMapper.selectByExample(userExample);
        return users;
    }
}
