package org.fan.demo.scheduledservice.jobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.6.20 14:16
 */
public abstract class BaseJob extends QuartzJobBean {

    protected String description = "do task at 12:00 every day";

    protected String cronExpression = "0 0 12 * * ?";

    protected abstract void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException;

    public abstract String getDescription();

    public abstract String getCronExpression();

    public abstract void setDescription(String description);

    public abstract void setCronExpression(String cronExpression);


}
