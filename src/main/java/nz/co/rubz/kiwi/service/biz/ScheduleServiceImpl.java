package nz.co.rubz.kiwi.service.biz;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.mongodb.morphia.Key;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.WriteResult;

import nz.co.rubz.kiwi.KiwiBiz;
import nz.co.rubz.kiwi.MsgConstants;
import nz.co.rubz.kiwi.bean.Notification;
import nz.co.rubz.kiwi.bean.Schedule;
import nz.co.rubz.kiwi.dao.NotifyDao;
import nz.co.rubz.kiwi.dao.ScheduleDao;
import nz.co.rubz.kiwi.jobs.service.ScheduleJobService;
import nz.co.rubz.kiwi.protocol.beans.Content;
import nz.co.rubz.kiwi.protocol.converter.ResponseContentHelper;
import nz.co.rubz.kiwi.utils.DateUtils;
import nz.co.rubz.kiwi.utils.MyStringUtils;

@Service
public class ScheduleServiceImpl {

	private Logger log = Logger.getLogger(ScheduleServiceImpl.class);

	@Autowired
	private NotifyDao notifyDao;
	
	@Autowired
	private ScheduleDao scheduleDao;

	@Autowired
	private ScheduleJobService scheduleJobService;
	 
	@Autowired
	private ClassuDataWrapper dataWrapper;

	@Autowired
	private BizNoticePublisher noticePublisher;
	// @Autowired
	// private JedisOperator jedis;

	@KiwiBiz("save")
	public Content saveSchedule(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		if (isValidSchedule(contentMap)) {
			Notification noti = dataWrapper.boxNotificationData(contentMap);
			noti.setType("1"); // 1 : 日程  
			Key<Notification> result = notifyDao.save(noti); // 只有content 没有 attaches
			if (result.getId() != null) {
				String notiId = result.getId().toString();
				resultMap.put("noti_id", notiId);
				resultMap.put("class_id", noti.getClassIds()); // 数组 ["000000","000001"]
				resultMap.put("ctime", noti.getCtime());
				List<String> classIds = noti.getClassIds();
				String classId = MyStringUtils.convertList2Str(classIds, "|");
				String actionTime = (String) contentMap.get("action_time");
				Schedule schedule = newSchedule(notiId,classId,actionTime);
				Key<Schedule> schResult = scheduleDao.save(schedule);
				if(schResult.getId()!=null){
					scheduleJobService.insert(schedule);
					log.info("save schedule with id [ "+schResult.getId().toString()+" ] successed.");
				} else {
					log.warn("save schedule failed.");
				}
			}

			resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
			resultContent.setData(resultMap);
		} else {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_1,
							"can not save schedule, need more params.");
		}
		return resultContent;
	}

	
	private String genCronExpression(String actionTime) {
		Date date = DateUtils.getDateByPatternString(actionTime,
				"yyyyMMddHHmmss");
		if (date != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);
			int week = c.get(Calendar.DAY_OF_WEEK);
			StringBuilder sb = new StringBuilder();
			sb.append("0");
			sb.append(" ");
			sb.append(minute);
			sb.append(" ");
			sb.append(hour);
			sb.append(" ");
			sb.append(day);
			sb.append(" ");
			sb.append(month+1);
			sb.append(" ");
			sb.append("?");
			sb.append(" ");
			sb.append(year);
			log.info("cronExpress is : [" + sb.toString() + " ]");
			return sb.toString();
		} else {
			return null;
		}
	}
	
	
	private Schedule newSchedule(String notiId,String classId,String actionTime){
		Schedule schedule = new Schedule();
//		schedule.setAliasName("");
		schedule.setActionTime( DateUtils.getDateByPatternString(actionTime,
				"yyyyMMddHHmmss"));
		schedule.setDescription("class [ "+classId+" ] job.");
		schedule.setIsSync("false");
		schedule.setJobGroup(classId);
		schedule.setJobName(notiId);
		schedule.setCronExpression(genCronExpression(actionTime));
//		schedule.setJobStatus("");
//		schedule.setJobTrigger("");
//		schedule.setNotiId(notiId);
		schedule.setUpdateTime(new Date());
		
		return schedule;
	}
	

	@KiwiBiz("modify")
	@Deprecated
	public Content modifySchedule(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		String notiId = (String) contentMap.get("noti_id"); 
		String userId = (String) contentMap.get("user_id");
		String actionTime = (String) contentMap.get("action_time");

		if (!StringUtils.isBlank(userId) && !StringUtils.isBlank(actionTime)) {
			Schedule schedule = scheduleDao.findScheduleByNotiId(notiId);
			String cronExpression = genCronExpression(actionTime);
			if (cronExpression!=null){
				schedule.setCronExpression(cronExpression);
				boolean result = scheduleDao.updateSchedule(schedule);
				if (result){
					scheduleJobService.update(schedule);
					return ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_0,
							"modify schedule successed .new action time is " + cronExpression);
				} else {
					return ResponseContentHelper
							.genSimpleResponseContentWithoutType(
									MsgConstants.ERROR_CODE_0,
									"modify schedule failed .");
				}
			}
		} else {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_1,
							"can not modify schedule, need more params.");
		}
		return resultContent;
	}

	@KiwiBiz("remove")
	public Content removeSchedule(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		String notiId = (String) contentMap.get("noti_id");
		Notification n = notifyDao.findNotiById(notiId);
		
		WriteResult result = notifyDao.delete(n);
		boolean result1 = scheduleDao.deleteScheduleByNoti(notiId);
		if (result.getN()>0 && result1) {
			noticePublisher.pubNotificationNotice(n, false);
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_0,
							"delete schedule successed.");

		} else {
			log.error("delete schedule failed! result is " + result);
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_2, "schedule " + notiId
									+ " not exists.");
		}
		return resultContent;
	}

	private boolean isValidSchedule(HashMap<String, Object> contentMap) {
		Object classIds =  contentMap.get("class_id");
		String content = (String) contentMap.get("content");
		String actionTime = (String) contentMap.get("action_time");
		if (StringUtils.isBlank(content) || StringUtils.isBlank(actionTime)) {
			return false;
		}
		if (classIds == null || !(classIds instanceof List<?>)) {
			log.info("class_id is null or not array.");
			return false;
		}
		return true;
	}



	

}
