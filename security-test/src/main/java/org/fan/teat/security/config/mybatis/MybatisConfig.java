package org.fan.teat.security.config.mybatis;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * @version 1.0
 * @author Fan
 * @date 2020.10.12 12:50
 */
@Configuration
@MapperScan(basePackages = {"org.fan.teat.security.mapper"})
public class MybatisConfig {

  @Bean
  public DataSource dataSource(){
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl("jdbc:mariadb://localhost:3306/test");
    config.setPoolName("default");
    config.setUsername("root");
    config.setPassword("123456");
    config.setDriverClassName("org.mariadb.jdbc.Driver");
    return new HikariDataSource(config);
  }

  @Bean
  public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
    factoryBean.setDataSource(dataSource);
    factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
        .getResources("classpath*:mapper/*.xml"));
    return factoryBean.getObject();
  }




}
