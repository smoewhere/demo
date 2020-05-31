package org.fan.demo.dynamic.config.datasource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.5.31 22:59
 */
@Configuration
public class DataSourceConfig {

    /*@Bean
    public DataSource dataSourceNormal(){
        DataSourceBuilder.create().build();
    }*/
}
