package nz.co.rubz.kiwi.jobs;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import nz.co.rubz.kiwi.ServerConstants;
import nz.co.rubz.kiwi.bean.Schedule;

/**
 * 同步的任务工厂类
 *
 */

//@Component
public class KiwiJobSyncFactory implements Job {

    /* 日志对象 */
    private static final Logger LOG = Logger.getLogger(KiwiJobSyncFactory.class);

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        LOG.info("JobSyncFactory execute");

        JobDataMap mergedJobDataMap = jobExecutionContext.getMergedJobDataMap();
        Schedule scheduleJob = (Schedule) mergedJobDataMap.get(ServerConstants.JOB_PARAM_KEY);

        System.out.println("jobName:" + scheduleJob.getJobName() + "  " + scheduleJob);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
