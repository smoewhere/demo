package org.fan.demo.dynamic.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.fan.demo.dynamic.annotation.ChooseDataSource;
import org.fan.demo.dynamic.config.datasource.DataSourceType;
import org.fan.demo.dynamic.enums.DataBaseType;
import org.fan.demo.dynamic.service.HelloService;
import org.springframework.stereotype.Service;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.6.1 15:36
 */
@Slf4j
@Service("helloService")
public class HelloServiceImpl implements HelloService {

    @ChooseDataSource(DataBaseType.DS1)
    @Override
    public void print(String name) {
        log.info("do print() {}",name);
    }
}
