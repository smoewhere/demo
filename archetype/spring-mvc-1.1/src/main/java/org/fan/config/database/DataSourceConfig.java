package org.fan.config.database;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.10.12 13:20
 */
@Configuration
public class DataSourceConfig {

  @Bean
  public DruidDataSource dataSource(){
    DruidDataSource dataSource = new DruidDataSource();
    dataSource.setUrl("jdbc:mysql:///test1?serverTimezone=GMT%2B8&characterEncoding=utf8");
    dataSource.setUsername("root");
    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    dataSource.setPassword("123456");
    return dataSource;
  }

}
