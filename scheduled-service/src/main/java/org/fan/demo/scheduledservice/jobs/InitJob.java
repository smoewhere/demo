package org.fan.demo.scheduledservice.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.6.20 14:27
 */
@Slf4j
@Component
@DisallowConcurrentExecution
public class InitJob extends BaseJob{

    private String description = "do task at 12:00 every day";

    private String cronExpression = "0/10 * * * * ?";

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("start initJob");
        try {
            TimeUnit.SECONDS.sleep(12);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("end initJob");
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getCronExpression() {
        return this.cronExpression;
    }
}
