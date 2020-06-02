package org.fan.demo.dynamic.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.fan.demo.dynamic.annotation.ChooseDataSource;
import org.fan.demo.dynamic.config.datasource.DataSourceType;
import org.fan.demo.dynamic.dao.User2Mapper;
import org.fan.demo.dynamic.dao.UserMapper;
import org.fan.demo.dynamic.enums.DataBaseType;
import org.fan.demo.dynamic.model.User;
import org.fan.demo.dynamic.model.UserExample;
import org.fan.demo.dynamic.service.HelloService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.6.1 15:36
 */
@Slf4j
@Service("helloService")
public class HelloServiceImpl {

    @Resource
    private UserMapper userMapper;

    @Resource
    private User2Mapper user2Mapper;

    public void print(String name) {
        log.info("do print() {}", name);
    }

    @ChooseDataSource(DataBaseType.DS1)
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public List<User> getUsers() {
        return userMapper.selectByExample(new UserExample());
    }


    @ChooseDataSource(DataBaseType.DS1)
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public int insert1(User user) {
        int insert = userMapper.insert(user);
        int i = 1 / 0;
        return insert;
    }

    @ChooseDataSource(DataBaseType.DS2)
    public List<User> getUsers2() {
        return user2Mapper.getUsers();
    }

    @ChooseDataSource(DataBaseType.DS2)
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public int insert(User user) {
        return user2Mapper.insert(user);
    }
}
