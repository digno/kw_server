package nz.co.rubz.kiwi.jobs;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nz.co.rubz.kiwi.ServerConstants;
import nz.co.rubz.kiwi.bean.Schedule;
import nz.co.rubz.kiwi.service.biz.BizNoticePublisher;

/**
 * 任务工厂类,非同步
 *
 */
@DisallowConcurrentExecution
//@Component
public class KiwiJobFactory implements Job {
//
//	@Autowired
//	private BizNoticePublisher publisher;


	/* 日志对象 */
	private static final Logger log = Logger.getLogger(KiwiJobFactory.class);

	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		log.info("JobFactory execute");

		Schedule scheduleJob = (Schedule) context.getMergedJobDataMap().get(
				ServerConstants.JOB_PARAM_KEY);
		
		BizNoticePublisher publisher =   (BizNoticePublisher) context.getMergedJobDataMap().get("publisher");

		log.info("jobGroup: " + scheduleJob.getJobGroup() + " jobName: "
				+ scheduleJob.getJobName() + " cronExpression: "
				+ scheduleJob.getCronExpression() + " isSync: "
				+ scheduleJob.getIsSync()
				// + " jobTrigger: " + scheduleJob.getJobTrigger()
				// + " jobStatus: " + scheduleJob.getJobStatus()
				// + " notiId: " + scheduleJob.getNotiId() 
				+ " createTime: " + scheduleJob.getActionTime() + " " + scheduleJob);
		publisher.pubApnsMessageWithNotiId(scheduleJob.getJobName(),true);

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	

}
