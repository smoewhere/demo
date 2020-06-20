package org.fan.demo.scheduledservice.common;

import lombok.extern.slf4j.Slf4j;
import org.fan.demo.scheduledservice.jobs.BaseJob;
import org.quartz.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Map;

/**
 * 监听容器启动，等启动完成之后，往quartz中添加默认任务
 *
 * @author Fan
 * @version 1.0
 * @date 2020.6.20 14:34
 */
@Slf4j
@Component
public class AppOnloadListener implements ApplicationListener<ContextRefreshedEvent> {

    @Resource
    private Scheduler scheduler;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("start add task");
        ApplicationContext context = contextRefreshedEvent.getApplicationContext();
        Map<String, BaseJob> beans = context.getBeansOfType(BaseJob.class);
        for (Map.Entry<String, BaseJob> entry : beans.entrySet()) {
            JobKey jobKey = new JobKey(entry.getKey(), "default");
            TriggerKey triggerKey = TriggerKey.triggerKey(entry.getKey(), "default");
            JobDetail jobDetail = JobBuilder.newJob(entry.getValue().getClass())
                    .withIdentity(jobKey)
                    .withDescription(entry.getValue().getDescription())
                    .build();
            CronTriggerFactoryBean bean = new CronTriggerFactoryBean();
            bean.setCronExpression(entry.getValue().getCronExpression());
            bean.setName(entry.getKey());
            bean.setGroup("default");
            try {
                bean.afterPropertiesSet();
                if (scheduler.checkExists(triggerKey)) {
                    // 暂停触发器
                    scheduler.pauseTrigger(triggerKey);
                    // 移除触发器
                    scheduler.unscheduleJob(triggerKey);
                    // 删除任务
                    scheduler.deleteJob(jobKey);
                }
                scheduler.scheduleJob(jobDetail, bean.getObject());
                log.info("add task {}", entry.getKey());
            } catch (ParseException | SchedulerException e) {
                log.error("catch an exception", e);
            }
        }
    }
}
