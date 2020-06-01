package org.fan.demo.dynamic.config.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.util.pattern.PathPatternRouteMatcher;

import javax.sql.DataSource;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.6.1 16:00
 */
@Configuration
@MapperScan(basePackages = {"org.fan.demo.dynamic.dao"})
public class MybatisConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:mapper/*"));
        return sqlSessionFactoryBean.getObject();
    }
}
