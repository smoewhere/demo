package org.fan.demo.dynamic.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.fan.demo.dynamic.config.properties.CompositePropertySourceFactory;

import org.fan.demo.dynamic.config.properties.DruidDataSourceProperties;
import org.fan.demo.dynamic.enums.DataBaseType;
import org.fan.demo.dynamic.model.Person;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;


/**
 * @author Fan
 * @version 1.0
 * @date 2020.5.31 22:59
 */
@PropertySource(value = "classpath:config/dataSource.yaml", factory = CompositePropertySourceFactory.class)
@Configuration
public class DataSourceConfig {

    @Bean(value = "dataSource1", initMethod = "init")
    public DruidDataSource dataSource1(@Qualifier("dataSourceProperties1") DruidDataSourceProperties dataSourceProperties) throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        wrapDruidDataSource(druidDataSource, dataSourceProperties);
        return druidDataSource;
    }

    @Bean(value = "dataSource2", initMethod = "init")
    public DruidDataSource dataSource2(@Qualifier("dataSourceProperties2") DruidDataSourceProperties dataSourceProperties) throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        wrapDruidDataSource(druidDataSource, dataSourceProperties);
        return druidDataSource;
    }

    @Bean("dynamicDataSource")
    public DataSource dynamicDataSource(@Qualifier("dataSource1") DruidDataSource ds1,
                                        @Qualifier("dataSource2") DruidDataSource ds2) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        HashMap<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(DataBaseType.DS1, ds1);
        dataSourceMap.put(DataBaseType.DS2, ds2);
        dynamicDataSource.setDefaultTargetDataSource(ds1);
        dynamicDataSource.setTargetDataSources(dataSourceMap);
        return dynamicDataSource;
    }

    /**
     * 从配置文件中读取数据源配置信息，前缀为prefix指定
     *
     * @return 数据源配置类
     */
    @Bean("dataSourceProperties1")
    @ConfigurationProperties(prefix = "jdbc.data-source.druid.ds1")
    public DruidDataSourceProperties dataSourceProperties1() {
        return new DruidDataSourceProperties();
    }

    /**
     * 从配置文件中读取数据源配置信息，前缀为prefix指定
     *
     * @return 数据源配置类
     */
    @Bean("dataSourceProperties2")
    @ConfigurationProperties(prefix = "jdbc.data-source.druid.ds2")
    public DruidDataSourceProperties dataSourceProperties2() {
        return new DruidDataSourceProperties();
    }

    /**
     * 配置数据源
     *
     * @param dataSource 数据源
     * @param properties 数据源参数
     * @throws SQLException 配置filter会抛出异常
     */
    private void wrapDruidDataSource(DruidDataSource dataSource, DruidDataSourceProperties properties) throws SQLException {
        dataSource.setName(properties.getName());
        dataSource.setDriverClassName(properties.getDriverClassName());
        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUserName());
        dataSource.setPassword(properties.getPassword());
        dataSource.setTestWhileIdle(properties.getProp().getTestWhileIdle());
        dataSource.setInitialSize(properties.getProp().getInitialSize());
        dataSource.setMinIdle(properties.getProp().getMinIdle());
        dataSource.setMaxActive(properties.getProp().getMaxActive());
        dataSource.setMaxWait(properties.getProp().getMaxWait());
        dataSource.setTimeBetweenEvictionRunsMillis(properties.getProp().getTimeBetweenEvictionRunsMillis());
        dataSource.setMinEvictableIdleTimeMillis(properties.getProp().getMinEvictableIdleTimeMillis());
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(properties.getProp()
                .getMaxPoolPreparedStatementPerConnectionSize());
        dataSource.setConnectionErrorRetryAttempts(properties.getProp().getConnectionErrorRetryAttempts());
        dataSource.setTimeBetweenConnectErrorMillis(properties.getProp().getTimeBetweenConnectErrorMillis());
        dataSource.setFilters(properties.getProp().getFilters());
        dataSource.setRemoveAbandonedTimeout(properties.getProp().getRemoveAbandonedTimeout());
        dataSource.setTransactionQueryTimeout(properties.getProp().getTransactionQueryTimeout());
        dataSource.setValidationQuery(properties.getProp().getValidationQuery());
        dataSource.setTestOnReturn(properties.getProp().getTestOnReturn());
        dataSource.setTestOnBorrow(properties.getProp().getTestOnBorrow());
        dataSource.setPoolPreparedStatements(properties.getProp().getPoolPreparedStatements());
        dataSource.setBreakAfterAcquireFailure(properties.getProp().getBreakAfterAcquireFailure());
        dataSource.setAsyncInit(properties.getProp().getAsyncInit());
        dataSource.setRemoveAbandoned(properties.getProp().getRemoveAbandoned());
    }
}
