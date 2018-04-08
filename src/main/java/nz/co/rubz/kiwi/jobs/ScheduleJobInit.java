package nz.co.rubz.kiwi.jobs;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

import nz.co.rubz.kiwi.jobs.service.ScheduleJobService;

/**
 * 定时任务初始化
 *
 */
@Component
public class ScheduleJobInit implements ApplicationListener<ContextStartedEvent>{

	/** 日志对象 */
	private static final Logger log = Logger.getLogger(ScheduleJobInit.class);

	/** 定时任务service */
	@Autowired
	private ScheduleJobService scheduleJobService;

	/**
	 * 项目启动时初始化
	 */
//	@PostConstruct
	public void init() {
		log.info("init");
		scheduleJobService.initScheduleJob();
		log.info("end");
	}

	@Override
	public void onApplicationEvent(ContextStartedEvent event) {
		init();
	}

}
