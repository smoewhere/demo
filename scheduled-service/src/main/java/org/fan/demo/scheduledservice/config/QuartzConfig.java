package org.fan.demo.scheduledservice.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.6.20 0:49
 */
@Configuration
public class QuartzConfig {

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(ApplicationContext context){
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        SpringBeanJobFactory jobFactory = new SpringBeanJobFactory();
        jobFactory.setApplicationContext(context);
        factoryBean.setJobFactory(jobFactory);
        factoryBean.setOverwriteExistingJobs(true);
        factoryBean.setAutoStartup(true);
        factoryBean.setConfigLocation(new ClassPathResource("conf/quartz.properties"));
        return factoryBean;

    }
}
