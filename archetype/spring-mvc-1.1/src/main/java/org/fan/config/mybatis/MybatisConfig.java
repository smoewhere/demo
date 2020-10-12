package org.fan.config.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.10.12 12:50
 */
@Configuration
@MapperScan(basePackages = {"org.fan.mapper"})
public class MybatisConfig {

  @Bean
  public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
    MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
    factoryBean.setDataSource(dataSource);
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
    factoryBean.setPlugins(interceptor);
    /*SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
    factoryBean.setDataSource(dataSource);
    factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
        .getResources("classpath*:mapper/*.xml"));
    return factoryBean.getObject();*/
    factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
        .getResources("classpath*:mapper/*.xml"));
    return factoryBean.getObject();
  }


}
