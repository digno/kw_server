package nz.co.rubz.kiwi.jobs.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.mongodb.morphia.Key;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nz.co.rubz.kiwi.bean.Schedule;
import nz.co.rubz.kiwi.dao.ScheduleDao;

/**
 * 定时任务
 *
 */
@Service
public class ScheduleJobServiceImpl implements ScheduleJobService {
	
	private Logger log = Logger.getLogger(ScheduleJobServiceImpl.class);

    /** 调度工厂Bean */
    @Autowired
    private Scheduler scheduler;

    @Autowired
    private ScheduleDao scheduleDao;
    
    @Autowired
    private ScheduleOperatorTools scheduleTools;

	public void initScheduleJob() {
		List<Schedule> scheduleJobList = scheduleDao.findValidSchedule();
		if (CollectionUtils.isEmpty(scheduleJobList)) {
			return;
		}
		for (Schedule scheduleJob : scheduleJobList) {

			CronTrigger cronTrigger = scheduleTools.getCronTrigger(scheduler,
					scheduleJob.getJobName(), scheduleJob.getJobGroup());

			// 不存在，创建一个
			if (cronTrigger == null) {
				boolean result = scheduleTools.createScheduleJob(scheduler,
						scheduleJob);
				if (result) {
					log.info("create schedule successed . [ "
							+ scheduleJob.getCronExpression() + ","
							+ scheduleJob.getActionTime() + ","
							+ scheduleJob.getJobGroup() + ","
							+ scheduleJob.getJobName() + " ]");
				}
			} else {
				// 已存在，那么更新相应的定时设置
				scheduleTools.updateScheduleJob(scheduler, scheduleJob);
			}
		}
	}

    public String insert(Schedule schedule) {
    	scheduleTools.createScheduleJob(scheduler, schedule);
        Key<Schedule> result =  scheduleDao.save(schedule);
        return result.getId().toString();
    }

    public void update(Schedule schedule ) {
    	scheduleTools.updateScheduleJob(scheduler, schedule);
        scheduleDao.updateSchedule(schedule);
    }

    public void delUpdate(Schedule scheduleJob) {
        //先删除
    	scheduleTools.deleteScheduleJob(scheduler, scheduleJob.getJobName(),
            scheduleJob.getJobGroup());
        //再创建
    	scheduleTools.createScheduleJob(scheduler, scheduleJob);
        //数据库直接更新即可
        scheduleDao.updateSchedule(scheduleJob);
    }

    public void delete(String  notiId) {
        Schedule scheduleJob = scheduleDao.findScheduleByNotiId(notiId);
        //删除运行的任务
        scheduleTools.deleteScheduleJob(scheduler, scheduleJob.getJobName(),
            scheduleJob.getJobGroup());
        //删除数据
        scheduleDao.deleteScheduleByNoti(notiId);
    }

    public void runOnce(String scheduleJobId) {

        Schedule scheduleJob = scheduleDao.findScheduleById(scheduleJobId);
        scheduleTools.runOnce(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());
    }

    public void pauseJob(String scheduleJobId) {

        Schedule scheduleJob = scheduleDao.findScheduleById(scheduleJobId);
        scheduleTools.pauseJob(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());

        //演示数据库就不更新了
    }

    public void resumeJob(String scheduleJobId) {
        Schedule scheduleJob = scheduleDao.findScheduleById(scheduleJobId);
        scheduleTools.resumeJob(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());

        //演示数据库就不更新了
    }


    public List<Schedule> queryList() {

        List<Schedule> scheduleJobs = scheduleDao.findValidSchedule();
        List<Schedule> schedulesWithTrigger = new ArrayList<>();
        try {
            for (Schedule vo : scheduleJobs) {
            	Schedule s = (Schedule) vo.clone(); 
                JobKey jobKey = scheduleTools.getJobKey(s.getJobName(), s.getJobGroup());
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                if (CollectionUtils.isEmpty(triggers)) {
                    continue;
                }

                //这里一个任务可以有多个触发器， 但是我们一个任务对应一个触发器，所以只取第一个即可，清晰明了
                Trigger trigger = triggers.iterator().next();
                s.setJobTrigger(trigger.getKey().getName());

                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                s.setJobStatus(triggerState.name());

                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    s.setCronExpression(cronExpression);
                }
                schedulesWithTrigger.add(s);
            }
        } catch (Exception e) {
            log.error("get all class job error .",e);
        }
        return schedulesWithTrigger;
    }

    public List<Schedule> queryExecutingJobList() {
        try {
            List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
            List<Schedule> jobList = new ArrayList<Schedule>(executingJobs.size());
            for (JobExecutionContext executingJob : executingJobs) {
                Schedule job = new Schedule();
                JobDetail jobDetail = executingJob.getJobDetail();
                JobKey jobKey = jobDetail.getKey();
                Trigger trigger = executingJob.getTrigger();
                job.setJobName(jobKey.getName());
                job.setJobGroup(jobKey.getGroup());
                job.setJobTrigger(trigger.getKey().getName());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                job.setJobStatus(triggerState.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    job.setCronExpression(cronExpression);
                }
                jobList.add(job);
            }
            return jobList;
        } catch (SchedulerException e) {
            return null;
        }

    }

}
