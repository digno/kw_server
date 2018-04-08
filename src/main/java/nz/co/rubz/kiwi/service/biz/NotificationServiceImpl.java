package nz.co.rubz.kiwi.service.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.mongodb.morphia.Key;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nz.co.rubz.kiwi.KiwiBiz;
import nz.co.rubz.kiwi.MsgConstants;
import nz.co.rubz.kiwi.bean.Classu;
import nz.co.rubz.kiwi.bean.Notification;
import nz.co.rubz.kiwi.dao.ClassDao;
import nz.co.rubz.kiwi.dao.CommentDao;
import nz.co.rubz.kiwi.dao.NotifyDao;
import nz.co.rubz.kiwi.jedis.JedisOperator;
import nz.co.rubz.kiwi.protocol.beans.Content;
import nz.co.rubz.kiwi.protocol.converter.ResponseContentHelper;

@Service
public class NotificationServiceImpl {

	private Logger log = Logger.getLogger(NotificationServiceImpl.class);

	@Autowired
	private NotifyDao notifyDao;
	
	@Autowired
	private ClassDao classDao;

	@Autowired
	private CommentDao commentDao;

	@Autowired
	private ClassuDataWrapper dataWrapper;

	@Autowired
	private BizNoticePublisher noticePublisher;
	
	 @Autowired
	 private JedisOperator jedis;

	@KiwiBiz("send")
	public Content sendNotify(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		if (isValidNotification(contentMap)) {
			Notification noti = dataWrapper.boxNotificationData(contentMap);
			Key<Notification> result = notifyDao.save(noti); // 如果给很多班级发通知，则存储多个。Notification
			// 给所有家长发通知。离线的发APNS
			// TODO 在此需要做异步处理。
			noticePublisher.pubNotificationNotice(noti,true);
			if (result.getId() != null) {
				resultMap.put("noti_id", result.getId().toString());
				resultMap.put("class_id", noti.getClassIds()); // 数组 ["000000","0000001"]
				resultMap.put("class_name", noti.getClassNames()); // 数组 ["000000","0000001"]
				resultMap.put("ctime", noti.getCtime());
			}

			resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
			Object targetIds = contentMap.get("target_id");
			if (targetIds != null && targetIds instanceof List<?>) {
				resultMap.put("target_id", contentMap.get("target_id")); // 如果有 target 那么class_id 必定为某个班级
				resultMap.put("target_name", contentMap.get("target_name")); 
			}
			resultContent.setData(resultMap);
		} else {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_1,
							"can not send notification, need more params.");
		}
		return resultContent;
	}

	// @ClassuBiz("send1")
	// public Content saveNotify(HashMap<String, Object> contentMap)
	// throws Exception {
	// Content resultContent = new Content();
	// HashMap<String, Object> resultMap = new HashMap<String, Object>();
	// if (isValidNotification(contentMap)) {
	// List<Notification> notis =
	// dataWrapper.boxNotificationDataToList(contentMap);
	// List<HashMap<String, Object>> notiList = new ArrayList<>();
	// for (Notification n : notis) {
	// HashMap<String, Object> notiMap = new HashMap<>();
	// Key<Notification> result = notifyDao.save(n); //
	// 如果给很多班级发通知，则存储多个。Notification
	// // 给所有家长发通知。离线的发APNS
	// // TODO 在此需要做异步处理。
	// pubNotificationNotice(n);
	// if (result.getId() != null) {
	// notiMap.put("noti_id", result.getId().toString());
	// notiMap.put("class_id", n.getClassId());
	// notiMap.put("ctime", n.getCtime());
	// }
	// notiList.add(notiMap);
	// }
	//
	// resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
	// if (notiList.size()>0){
	// resultMap.put(MsgConstants.CLASSIDS, contentMap.get("class_id"));
	// }
	// if (!StringUtils.isBlank((String) contentMap.get("targets"))){
	// resultMap.put(MsgConstants.TARGETS, contentMap.get("targets"));
	// }
	// resultMap.put(MsgConstants.NOTIS, notiList);
	// resultContent.setData(resultMap);
	// } else {
	// resultContent = ResponseContentHelper
	// .genSimpleResponseContentWithoutType(
	// MsgConstants.ERROR_CODE_1,
	// "can not send notification, need more params.");
	// }
	// return resultContent;
	// }

	@KiwiBiz("find")
	public Content findNotification(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		String classId = (String) contentMap.get("class_id"); // 只能查询某个班 
		String userId = (String) contentMap.get("user_id");
		String offset = (String) contentMap.get("offset");

		if (StringUtils.isNotBlank(userId)) {
			List<Notification> resultList = new ArrayList<Notification>();
			if (StringUtils.isBlank(offset)){
				offset = "0";
			}
			if (StringUtils.isNotBlank(classId)){
				resultList = notifyDao.findNotisContainsClassId(classId,Integer.valueOf(offset));
			} else {
				List<Classu> classes = classDao.findMyClass(userId);
				List<String> classIds = new ArrayList<>();
				if (classes != null) {
					for (Classu c : classes) {
						classIds.add(c.getClassId());
					}
					if (StringUtils.isBlank(offset)) {
						offset = "0";
					}
					resultList = notifyDao.findNotisByClassIds(classIds,Integer.valueOf(offset));
				}
			}
			
			List<HashMap<String, Object>> notiList = dataWrapper.covertNotificationData(resultList, userId);
			resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
			resultMap.put(MsgConstants.NOTIS, notiList);
			resultContent.setData(resultMap);
		} else {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_1,
							"can not send notification, need more params.");
		}
		return resultContent;
	}
	
	@KiwiBiz("find_about_me")
	public Content findAllNotification(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		String userId = (String) contentMap.get("user_id");
		String offset = (String) contentMap.get("offset");

		if (StringUtils.isNotBlank(userId)) {
			List<Notification> resultList = new ArrayList<Notification>();
			List<Classu> classes = classDao.findMyClass(userId);
			List<String> classIds = new ArrayList<>();
			if (classes != null) {
				for (Classu c : classes) {
					classIds.add(c.getClassId());
				}
				if (StringUtils.isBlank(offset)) {
					offset = "0";
				}
				resultList = notifyDao.findNotisByClassIds(classIds,Integer.valueOf(offset));
			}
			List<HashMap<String, Object>> notiList = dataWrapper
					.covertNotificationData(resultList, userId);
			resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
			resultMap.put(MsgConstants.NOTIS, notiList);
			resultContent.setData(resultMap);

		} else {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_1,
							"can not send notification, need more params.");
		}
		return resultContent;
	}
	
	@KiwiBiz("confirm")
	public Content confirmNotification(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		String notiId = (String) contentMap.get("noti_id");
		String userId = (String) contentMap.get("user_id");
		String confirmed = (String) contentMap.get("confirmed");
		if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(confirmed)) {
			if ("0".equals(confirmed)) {
				Long result = jedis.addConfirmNoti(notiId, userId);
				if (result == 0) {
					log.info("user " + userId
							+ " has confirmed notification already.");
				}
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_0,
								"confirmed notification successed.");

			} else {
				Long result = jedis.removeConfirmNoti(notiId, userId);
				
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_0,
								"unconfirmed notification successed.");
			}

		} else {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_1,
							"can not confirm notification, need more params.");
		}
		return resultContent;
	}
	
	@KiwiBiz("get_views")
	public Content getViewsAndConfirms(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		Object notis = contentMap.get("noti_id");  
		if (notis instanceof List<?>) {
			List<?> notiIds = (List<?>) notis;
			List<HashMap<String, Object>> resultArray = new ArrayList<>();
			for (Object notiId: notiIds){
				HashMap<String, Object> tmp = new HashMap<String, Object>();
				String viewed = jedis.getViewedNoti((String) notiId);
				Set<String> confirmed = jedis.getConfirmNoti((String) notiId);
				tmp.put("noti_id", notiId);
				tmp.put(MsgConstants.VIEWED, viewed == null ? "0": viewed);
				tmp.put(MsgConstants.CONFIRMED, CollectionUtils.isEmpty(confirmed) ? new String[]{}:confirmed );
				resultArray.add(tmp);
			}
			resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
			resultMap.put(MsgConstants.VIEWS, resultArray);
			resultContent.setData(resultMap);
		} else {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_1,
							"can not get views, need more params.");
		}
		return resultContent;
	}
	

	@KiwiBiz("remove")
	public Content removeNotification(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		String notiId = (String) contentMap.get("noti_id");
		String userId = (String) contentMap.get("user_id");
		Notification noti = notifyDao.findNotiById(notiId);
		if (noti!=null &&StringUtils.isNotBlank(userId) ){
			if (!userId.equals(noti.getOwner())){
				return  ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_2,
								"you are not the owner of the Notification.");
			}
		}
		boolean result = notifyDao.deleteNoti(notiId);
		commentDao.deleteCommentByNotiId(notiId);  
		noticePublisher.pubNotificationNotice(noti,false);
		if (result) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_0,
							"delete Notification successed.");

		} else {
			log.error("delete Notification failed! result is " + result);
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_1, "Notification " + notiId
									+ " not exists.");
		}
		return resultContent;
	}

	private boolean isValidNotification(HashMap<String, Object> contentMap) {
		Object classIds =  contentMap.get("class_id");
		String content = (String) contentMap.get("content");
		Object attaches = contentMap.get("attaches");
		if (StringUtils.isBlank(content) && attaches == null) {
			log.info("content or attache is null");
			return false;
		}
		if (classIds == null || !(classIds instanceof List<?>)) {
			log.info("class_id is null or not array.");
			return false;
		}
		return true;
	}



	

}
