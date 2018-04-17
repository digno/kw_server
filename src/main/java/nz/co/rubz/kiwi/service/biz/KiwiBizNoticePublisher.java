package nz.co.rubz.kiwi.service.biz;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import nz.co.rubz.kiwi.apns.ApnsMessageHelper;
import nz.co.rubz.kiwi.bean.Child;
import nz.co.rubz.kiwi.bean.Classu;
import nz.co.rubz.kiwi.bean.ClassuMember;
import nz.co.rubz.kiwi.bean.Comment;
import nz.co.rubz.kiwi.bean.Notification;
import nz.co.rubz.kiwi.bean.Parent;
import nz.co.rubz.kiwi.bean.User;
import nz.co.rubz.kiwi.dao.ChildDao;
import nz.co.rubz.kiwi.dao.ClassDao;
import nz.co.rubz.kiwi.dao.ClientDao;
import nz.co.rubz.kiwi.dao.NotifyDao;
import nz.co.rubz.kiwi.dao.UserDao;
import nz.co.rubz.kiwi.event.NoticeEvent;
import nz.co.rubz.kiwi.event.PushMsgEvent;
import nz.co.rubz.kiwi.model.Client;
import nz.co.rubz.kiwi.model.Geometry;
import nz.co.rubz.kiwi.notify.KiwiApnsMessage;
import nz.co.rubz.kiwi.notify.KiwiIPPushMessage;
import nz.co.rubz.kiwi.notify.KiwiNoticeMail;
import nz.co.rubz.kiwi.notify.NoticeConstants;
import nz.co.rubz.kiwi.protocol.beans.Content;

@Component
public class KiwiBizNoticePublisher {

	private static Logger log = Logger.getLogger(KiwiBizNoticePublisher.class);
	
	private static ObjectMapper mapper = new ObjectMapper();
	@Autowired
	private NotifyDao notifyDao;

	@Autowired
	private ClassDao classDao;
	
	@Autowired
	private ChildDao childDao;


	@Autowired
	private UserDao userDao;

	@Autowired
	private ClientDao clientDao;

	private String defaultSubject = NoticeConstants.NOTICE_SUBJECT;
	@Autowired
	private ClassuDataWrapper dataWrapper;

	@Autowired
	private ApnsMessageHelper apnsHelper;
	@Autowired
	private ApplicationContext applicationContext;
	
	private static final String IOS_DEIVCE_TYPE="IC001";

	public void pubJoinClassNotice(HashMap<String, Object> contentMap) {
		Content content = new Content();
		String classId = (String) contentMap.get("class_id");
		Classu classu = classDao.findClassNameOwnerById(classId);
		User user = userDao.findById(classu.getOwner());
		if (user == null) {
			log.info("pubJoinClassNotice can not find owner "
					+ classu.getOwner() + " record.");
			return;
		}
		Set<String> recipients = new HashSet<String>();
		recipients.add(user.getId().toHexString());
		// recipients.add(classu.getOwner());
		content.setType(NoticeConstants.JOIN_CLASS_NOTI);
		content.setData(contentMap);
		KiwiNoticeMail cnm = new KiwiNoticeMail();
		cnm.setNoticeType(defaultSubject);
		cnm.setRecipients(recipients);
		cnm.setContent(content);
//		applicationContext.publishEvent(new NoticeEvent(cnm));
		applicationContext.publishEvent(new NoticeEvent(rebuildClassuNoticeMail(cnm)));
	}

	public void pubModifyClassNotice(String classId) {
		Classu classu = classDao.findClassById(classId, true);
		if (CollectionUtils.isEmpty(classu.getMembers())) {
			log.info("no one joined class [ " + classId + " ] .");
			return;
		}
		Content content = new Content();
		HashMap<String, Object> contentMap = dataWrapper
				.convertClassuData(classu);
		contentMap.remove("members");// 修改班级发送的通知不需要members
		content.setType(NoticeConstants.MODIFY_CLASS_NOTI);
		content.setData(contentMap);
		Set<String> recipients = new HashSet<String>();
		for (ClassuMember cm : classu.getMembers()) {
			if (!recipients.contains(cm.getUserId())) {
				recipients.add(cm.getUserId());
				// recipients.add(cm.getMobile());
			}
		}
		KiwiNoticeMail cnm = new KiwiNoticeMail();
		cnm.setNoticeType(defaultSubject);
		cnm.setRecipients(recipients);
		cnm.setContent(content);
//		applicationContext.publishEvent(new NoticeEvent(cnm));
		applicationContext.publishEvent(new NoticeEvent(rebuildClassuNoticeMail(cnm)));
	}

	public void pubRemoveMembersNotice(HashMap<String, Object> contentMap) {
		User user = userDao.findById((String) contentMap.get("user_id"));
		if (user == null) {
			log.info("pubRemoveMembersNotice can not find mobile "
					+ contentMap.get("user_id").toString() + " in db.");
			return;
		}
		Content content = new Content();
		content.setType(NoticeConstants.REMOVE_MEMBER_NOTI);
		content.setData(contentMap);
		Set<String> recipients = new HashSet<String>();
		recipients.add(user.getId().toHexString());
		// recipients.add(user.getMobile());
		KiwiNoticeMail cnm = new KiwiNoticeMail();
		cnm.setNoticeType(defaultSubject);
		cnm.setRecipients(recipients);
		cnm.setContent(content);
//		applicationContext.publishEvent(new NoticeEvent(cnm));
		applicationContext.publishEvent(new NoticeEvent(rebuildClassuNoticeMail(cnm)));
	}

	public void pubRemoveClassNotice(String classId) {
		Classu classu = classDao.findClassById(classId, true);
		if (classu.getMembers() == null) {
			log.info("no one joined class [ " + classId + " ] .");
			return;
		}
		Content content = new Content();
		HashMap<String, Object> contentMap = new HashMap<String, Object>();
		contentMap.put("class_id", classId);
		contentMap.put("class_name", classu.getClassName());
		content.setType(NoticeConstants.REMOVE_CLASS_NOTI);
		content.setData(contentMap);
		Set<String> recipients = new HashSet<String>();
		for (ClassuMember cm : classu.getMembers()) {
			recipients.add(cm.getUserId());
			// recipients.add(cm.getMobile());
		}
		KiwiNoticeMail cnm = new KiwiNoticeMail();
		cnm.setNoticeType(defaultSubject);
		cnm.setRecipients(recipients);
		cnm.setContent(content);
//		applicationContext.publishEvent(new NoticeEvent(cnm));
		applicationContext.publishEvent(new NoticeEvent(rebuildClassuNoticeMail(cnm)));
	}

	public void pubExitClassNotice(String classId,String userId) {
//		List<Classu> classes = classDao.findClassByClassIdAndMember(classId,
//				mobile);
//		if (CollectionUtils.isEmpty(classes)) {
//			log.info("can not find class with class_id [ " + classId
//					+ " ] and mobile [ " + mobile + " ].");
//			return;
//		}
//		Classu classu = classes.iterator().next();
		Classu classu = classDao.findClassById(classId, false);
		if (classu == null){
			log.info("can not find class with class_id [ " + classId
					+ " ] .");
			return;
		}
		HashMap<String, Object> contentMap = dataWrapper
				.convertClassuData(classu);
		contentMap.put("user_id", userId);
//		User user = userDao.findById(userId);
//		if (user == null) {
//			log.info("pubExitClassNotice can not find user "
//					+ userId + " record.");
//			return;
//		}
		Content content = new Content();
		Set<String> recipients = new HashSet<String>();
		recipients.add(classu.getOwner());
		// recipients.add(user.getMobile());
		content.setType(NoticeConstants.EXIT_CLASS_NOTI);
		content.setData(contentMap);
		KiwiNoticeMail cnm = new KiwiNoticeMail();
		cnm.setNoticeType(defaultSubject);
		cnm.setRecipients(recipients);
		cnm.setContent(content);
//		applicationContext.publishEvent(new NoticeEvent(cnm));
		applicationContext.publishEvent(new NoticeEvent(rebuildClassuNoticeMail(cnm)));
	}

	public void pubCommentNotificationNotice(Comment comment) {
		Content content = new Content();
		Set<String> recipients = new HashSet<String>();
		content.setType(NoticeConstants.COMMENT_NOTIFICATION_NOTI);
		HashMap<String, Object> contentMap = dataWrapper
				.convertCommentData(comment);
		content.setData(contentMap);
		KiwiNoticeMail cnm = new KiwiNoticeMail();
		cnm.setNoticeType(defaultSubject);
		cnm.setContent(content);

		Notification n = notifyDao.findOne("_id",
				new ObjectId(comment.getNotiId()));
		if (n==null){
			 return;
		}
		String notiOwner = n.getOwner();
		if (!StringUtils.isBlank((comment.getRelayTo()))) {
			// 如果relayTo 不为空，则一定是回给某个人（家长或者老师）仅给此人发通知
			User user = userDao.findById(comment.getRelayTo());
			if (user != null) {
				recipients.add(user.getId().toHexString());
				log.info("pub comment notice to [ "
						+ user.getId().toHexString() + " ].");
			} else {
				log.info("can not find user " + comment.getRelayTo());
			}

			cnm.setRecipients(recipients);
			applicationContext.publishEvent(new NoticeEvent(cnm));
			return;
		}
		// 如果relayTo 为空，relay为老师则发给全班人员，否则则是家长回复，通知老师即可
		if (comment.getRelay().equals(notiOwner)) {
			for (String cid : n.getClassIds()) {
				// 如果是老师回复，则发给全班人员
				Classu classu = classDao.findClassById(cid, true);
				if (classu!=null && !CollectionUtils.isEmpty(classu.getMembers())) {
					for (ClassuMember cm : classu.getMembers()) {
						recipients.add(cm.getUserId());
					}
					log.info("pub comment notice to class "
							+ classu.getClassName() + " all members "
							+ recipients.toArray());
				}
			}

		} else {
			// 家长回复，通知老师
			User user = userDao.findById(n.getOwner());
			recipients.add(user.getId().toHexString());
			log.info("pub comment notice to class owner  [ "
					+ user.getId().toHexString() + " ].");
		}
		cnm.setRecipients(recipients);
//		applicationContext.publishEvent(new NoticeEvent(cnm));
		applicationContext.publishEvent(new NoticeEvent(rebuildClassuNoticeMail(cnm)));
	}

	public void pubNotifyApns(String notiType, String className,
			String content, Set<String> deviceTokens) {
		KiwiApnsMessage cam = new KiwiApnsMessage();
		cam.setDeviceTokens(deviceTokens);
		cam.setPayload(apnsHelper.buildApnsPayload(notiType, content,className));
		applicationContext.publishEvent(new PushMsgEvent(cam));
	}
	
	public void pubNotifyIppush(String notiType, String className,
			String content, Set<String> deviceTokens) {
		KiwiIPPushMessage cam = new KiwiIPPushMessage();
		cam.setDeviceTokens(deviceTokens);
		cam.setTitle("");
		cam.setContent(apnsHelper.buildIppushContent(notiType, content,className));
		applicationContext.publishEvent(new PushMsgEvent(cam));
	}
	

	public void pubNotificationNotice(Notification n,boolean isNew) {
		Content content = new Content();
		Set<String> iosDeviceTokens = new HashSet<String>();
		Set<String> androidDeviceTokens = new HashSet<String>();
		HashMap<String, Object> contentMap = new HashMap<>();
		if(isNew){
			content.setType(NoticeConstants.NEW_NOTIFICATION_NOTI);	
			contentMap = dataWrapper
					.covertSingleNotificationData(n);
			// 日程通知 会有执行时间和类型，但是这会影响客户端的展示
			contentMap.remove("action_time");
			contentMap.put("type","");
			content.setData(contentMap);
		}else {
			content.setType(NoticeConstants.DEL_NOTIFICATION_NOTI);	
			contentMap.put("noti_id", n.getId().toHexString());
			
		}
		content.setData(contentMap);
		KiwiNoticeMail cnm = new KiwiNoticeMail();
		cnm.setNoticeType(defaultSubject);
		cnm.setContent(content);
//		boolean hasAttaches = false;
//		if (!CollectionUtils.isEmpty(n.getAttaches())) {
//			hasAttaches = true;
//		}
		
		if (n.getTargetIds() != null) {
			Set<String> recipients = new HashSet<String>();
			List<User> users = userDao.findByIds(n.getTargetIds());
			if (!CollectionUtils.isEmpty(users)) {
				for (User u : users) {
					recipients.add(u.getId().toHexString());
					Client clients = clientDao
							.findByUserId(u.getId().toHexString());
					if (clients!=null) {
						if (IOS_DEIVCE_TYPE.endsWith(clients.getPhoneType())){
							iosDeviceTokens.add(clients.getDeviceToken());
						}else {
							androidDeviceTokens.add(clients.getDeviceToken());
						}
						
					} else {
						log.info("cannot find " + u.getMobile()
								+ "'s deviceToken infos.");
					}
				}
			}
			cnm.setRecipients(recipients);
//			applicationContext.publishEvent(new NoticeEvent(cnm));
			applicationContext.publishEvent(new NoticeEvent(rebuildClassuNoticeMail(cnm)));
			if (!iosDeviceTokens.isEmpty()
					&& CollectionUtils.isNotEmpty(n.getClassNames())) {
				if(!isNew){
					return;
				}
				pubNotifyApns(n.getNotiType(), n.getClassNames().iterator().next(),
						n.getContent(), iosDeviceTokens);
			}
			if (!androidDeviceTokens.isEmpty()
					&& CollectionUtils.isNotEmpty(n.getClassNames())) {
				if(!isNew){
					return;
				}
				pubNotifyIppush(n.getNotiType(), n.getClassNames().iterator().next(),
						n.getContent(), androidDeviceTokens);
			}
			return;
		}

		for (String cid : n.getClassIds()) {
			Set<String> recipients = new HashSet<String>();
			Classu classu = classDao.findClassById(cid, true);
			if (classu ==null || CollectionUtils.isEmpty(classu.getMembers())) {
				log.info("class " + cid + " has no members.");
				return;
			}
			for (ClassuMember cm : classu.getMembers()) {
				recipients.add(cm.getUserId());
				Client clients = clientDao.findByUserId(cm.getUserId());
				if (clients!=null) {
					if (IOS_DEIVCE_TYPE.endsWith(clients.getPhoneType())){
						iosDeviceTokens.add(clients.getDeviceToken());
					}else {
						androidDeviceTokens.add(clients.getDeviceToken());
					}
				} else {
					log.info("cannot find " + cm.getUserId()
							+ "'s deviceToken infos.");
				}
			}
			cnm.setRecipients(recipients);
//			applicationContext.publishEvent(new NoticeEvent(cnm));
			applicationContext.publishEvent(new NoticeEvent(rebuildClassuNoticeMail(cnm)));
			if (!iosDeviceTokens.isEmpty()
					&& CollectionUtils.isNotEmpty(n.getClassNames())) {
				if(!isNew){
					return;
				}
				pubNotifyApns(n.getNotiType(), n.getClassNames().iterator().next(),
						n.getContent(), iosDeviceTokens);
			}
			if (!androidDeviceTokens.isEmpty()
					&& CollectionUtils.isNotEmpty(n.getClassNames())) {
				if(!isNew){
					return;
				}
				pubNotifyIppush(n.getNotiType(), n.getClassNames().iterator().next(),
						n.getContent(), androidDeviceTokens);
			}
		}

	}
	
	
	public void pubApnsMessageWithNotiId(String notiId,boolean isNew ) {
		Notification n = notifyDao.findNotiById(notiId);
		if (n!=null){
			pubNotificationNotice(n,isNew);
		}else{
			log.warn("pubApnsMessageWithNotiId : can not find notification with id [ "+ notiId +" ], isNew [ " +isNew+" ]");
		}
	}
	
	
	public void pubModifyChildNotice(String childId,String parentId) {
//		List<Child> children = childDao.findByChildNameAndParent(childName, parentId);
//		if(CollectionUtils.isEmpty(children) ){
//			log.info("no child named ["+ childName +"]");
//			return ;
//		}
//		Child child = children.iterator().next();
		
		Child child = childDao.findChildById(childId);
		List<Classu> joinedClasses = classDao.findClassByMember(parentId);
		List<Parent> parents = child.getParents();
		Set<String> recipients = new HashSet<String>();
		HashMap<String,Object> contentMap = new HashMap<>();
		for (Parent p : parents){
			if (parentId.equals(p.getPid())){
				contentMap.put("relation", p.getRelation());
				continue;
			}
			recipients.add(p.getPid());
//			User user = userDao.findById(p.getPid());
//			recipients.add(p.getMobile());
		}
		if (CollectionUtils.isNotEmpty(joinedClasses)){
			for (Classu c : joinedClasses){
				recipients.add(c.getOwner());
			}
		}
		
		
//		contentMap.put("mobile", mobile);
		contentMap.put("pid", parentId);
		contentMap.put("child_id", child.getId().toHexString());
		contentMap.put("child_name", child.getChildName());
		contentMap.put("child_photo", child.getChildPhoto());
		Content content = new Content();
		content.setType(NoticeConstants.MODIFY_CHILD_NOTI);
		content.setData(contentMap);
		KiwiNoticeMail cnm = new KiwiNoticeMail();
		cnm.setNoticeType(defaultSubject);
		cnm.setRecipients(recipients);
		cnm.setContent(content);
//		applicationContext.publishEvent(new NoticeEvent(cnm));
		applicationContext.publishEvent(new NoticeEvent(rebuildClassuNoticeMail(cnm)));
	}
	
	public void pubModifyUserNotice(String userId) {
//		List<Child> children = childDao.findByChildNameAndParent(childName, parentId);
//		if(CollectionUtils.isEmpty(children) ){
//			log.info("no child named ["+ childName +"]");
//			return ;
//		}
//		Child child = children.iterator().next();
		
		// 如果是家长，通知所有关注孩子的人，通知孩子所在班级的老师
		User user = userDao.findById(userId);
		List<Child> children = childDao.findByParent(userId);
		List<Classu> joinedClasses = classDao.findClassByMember(userId);
		Set<String> recipients = new HashSet<String>();
		if (CollectionUtils.isNotEmpty(joinedClasses)){
		for (Child c: children){
			List<Parent> parents = c.getParents();
			for (Parent p : parents){
				if (userId.equals(p.getPid())){
					continue;
				}
				recipients.add(p.getPid());
//				User user = userDao.findById(p.getPid());
//				recipients.add(p.getMobile());
			}
		}}
		
		if (CollectionUtils.isNotEmpty(joinedClasses)){
			for (Classu c : joinedClasses){
				recipients.add(c.getOwner());
			}
		}
		// 如果是老师 则还需要通知班级里的所有成员
		if ("1".equals(user.getType())){
			List<Classu> createdClasses = classDao.findClassByOwner(userId);
			if (CollectionUtils.isNotEmpty(createdClasses)){
				for (Classu c : createdClasses){
					if (CollectionUtils.isNotEmpty(c.getMembers())){
						for (ClassuMember cm : c.getMembers()){
							recipients.add(cm.getUserId());
						}
					}
				}
			}
			
		}
		
		HashMap<String, Object> contentMap = new HashMap<String, Object>();
		contentMap.put("name", user.getName());
		contentMap.put("mobile", user.getMobile());
		contentMap.put("user_id", user.getId().toHexString());
		contentMap.put("type", user.getType());
		Content content = new Content();
		content.setType(NoticeConstants.MODIFY_USER_NOTI);
		content.setData(contentMap);
		KiwiNoticeMail cnm = new KiwiNoticeMail();
		cnm.setNoticeType(defaultSubject);
		cnm.setRecipients(recipients);
		cnm.setContent(content);
//		applicationContext.publishEvent(new NoticeEvent(cnm));
		applicationContext.publishEvent(new NoticeEvent(rebuildClassuNoticeMail(cnm)));
	}
	
	public void pubGeoUpdateNotice(String did, String loc_type,Geometry geometry){
		
	}
	
	private KiwiNoticeMail rebuildClassuNoticeMail(KiwiNoticeMail cnm){
		Set<String> recipients = cnm.getRecipients();
		HashSet<String> androidTokens = new HashSet<>();
		for (String userId : recipients){
			Client c = clientDao.findByUserId(userId);
			if (c!=null) {
				if (!IOS_DEIVCE_TYPE.equals(c.getPhoneType())){
					androidTokens.add(c.getDeviceToken());
				}
			}
			
		}
		try {
			pubNotifyIppush("6",NoticeConstants.NOTICE_SUBJECT,mapper.writeValueAsString(cnm.getContent()),androidTokens);
		} catch (JsonProcessingException e) {
			log.error("pubNotifIppsuh with xinge tunnel failed." ,e);
		}
		return cnm;
	}
	
}
