package org.fan.demo.scheduledservice.config;

import org.quartz.Scheduler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.6.20 0:49
 */
@Configuration
public class QuartzConfig {

    @Resource
    private DataSource dataSource;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(ApplicationContext context) throws Exception {
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        SpringBeanJobFactory jobFactory = new SpringBeanJobFactory();
        jobFactory.setApplicationContext(context);
        factoryBean.setDataSource(dataSource);
        factoryBean.setJobFactory(jobFactory);
        factoryBean.setOverwriteExistingJobs(true);
        factoryBean.setAutoStartup(true);
        factoryBean.setConfigLocation(new ClassPathResource("conf/quartz.properties"));
        factoryBean.afterPropertiesSet();
        factoryBean.getScheduler().clear();
        return factoryBean;
    }

    @Bean
    public Scheduler scheduler(ApplicationContext context) throws Exception {
        return schedulerFactoryBean(context).getScheduler();
    }
}
