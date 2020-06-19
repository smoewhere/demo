package org.fan.demo.scheduledservice.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.quartz.utils.ConnectionProvider;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.6.20 0:53
 */
@Component
public class DruidConnectionProvider implements ConnectionProvider {

    @Resource
    private DataSource dataSource;

    private DruidDataSource druidDataSource;

    @Override
    public Connection getConnection() throws SQLException {
        return druidDataSource.getConnection();
    }

    @Override
    public void shutdown() throws SQLException {
        this.druidDataSource.close();
    }

    @Override
    public void initialize() throws SQLException {
        this.druidDataSource = (DruidDataSource)dataSource;
    }
}
