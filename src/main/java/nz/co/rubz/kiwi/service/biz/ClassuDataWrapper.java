package nz.co.rubz.kiwi.service.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nz.co.rubz.kiwi.bean.Attach;
import nz.co.rubz.kiwi.bean.Child;
import nz.co.rubz.kiwi.bean.Classu;
import nz.co.rubz.kiwi.bean.ClassuMember;
import nz.co.rubz.kiwi.bean.Client;
import nz.co.rubz.kiwi.bean.Comment;
import nz.co.rubz.kiwi.bean.Notification;
import nz.co.rubz.kiwi.bean.User;
import nz.co.rubz.kiwi.dao.ClassDao;
import nz.co.rubz.kiwi.dao.CommentDao;
import nz.co.rubz.kiwi.jedis.JedisOperator;

@Component
public class ClassuDataWrapper {

	private Logger log = Logger.getLogger(ClassuDataWrapper.class);

	@Autowired
	private CommentDao commentDao;

	@Autowired
	private ClassDao classDao;
	
	@Autowired
	private JedisOperator jedis;

	public HashMap<String, Object> convertClassuData(Classu classu) {
		HashMap<String, Object> temp = new HashMap<String, Object>();
		temp.put("class_id", classu.getClassId());
		temp.put("class_name", classu.getClassName());
		if (!StringUtils.isBlank(classu.getClassMail())) {
			temp.put("class_mail", classu.getClassMail());
		}
		if (!StringUtils.isBlank(classu.getOwner())) {
			temp.put("owner", classu.getOwner());
		}
		if (!StringUtils.isBlank(classu.getOwnerName())) {
			temp.put("owner_name", classu.getOwnerName());

		}
		if (classu.getContent() != null) {
			temp.put("content", classu.getContent());
		}

		if (classu.getMembers() != null) {
			List<HashMap<String, Object>> members = new ArrayList<>();
			for (ClassuMember cm : classu.getMembers()) {
				HashMap<String, Object> t = new HashMap<>();
				t.put("child_photo", cm.getChildPhoto());
				t.put("child_id", cm.getChildId());
				t.put("child_name", cm.getChildName());
				t.put("user_id", cm.getUserId());
				t.put("relation", cm.getRelation());
				members.add(t);
			}
			temp.put("members", members);
		}
		if (classu.getCtime() != null) {
			// temp.put("ctime", DateFormatUtils.format(classu.getCtime(),
			// "yyyy-MM-dd hh:mm:ss"));
			temp.put("ctime", classu.getCtime().getTime());
		}

		return temp;
	}

	public void boxClassData(Classu classu, HashMap<String, Object> contentMap) {
		Object classId = contentMap.get("class_id");
		Object className = contentMap.get("class_name");
		Object classMail = contentMap.get("class_mail");
		Object owner = contentMap.get("owner");
		Object ownerName = contentMap.get("owner_name");
		Object content = contentMap.get("content");
		if (classId != null) {
			classu.setClassId((String) classId);
		}
		if (className != null) {
			classu.setClassName((String) className);
		}
		if (classMail != null) {
			classu.setClassMail((String) classMail);
		}
		if (content != null) {
			classu.setContent((String) content);
		}

		if (owner != null) {
			classu.setOwner((String) owner);
		}
		if (ownerName != null) {
			classu.setOwnerName((String) ownerName);
		}
	}

	public Comment boxCommentData(HashMap<String, Object> contentMap) {
		Comment comment = new Comment();
		String relay = (String) contentMap.get("relay");
		String content = (String) contentMap.get("comment");
		String relayTo = (String) contentMap.get("relay_to");
		String notiId = (String) contentMap.get("noti_id");
		Object classIds =  contentMap.get("class_id");
		comment.setNotiId(notiId);
		comment.setComment(content);
		comment.setRelay(relay);
		if(classIds != null && classIds instanceof List<?>){
			comment.setClassIds((List<String>) classIds);
		}
		if (!StringUtils.isBlank(relayTo)) {
			comment.setRelayTo(relayTo);
		}
		comment.setCtime(new Date());
		return comment;
	}

	public HashMap<String, Object> convertCommentData(Comment comment) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("cid", comment.getId().toHexString());
		map.put("class_id", CollectionUtils.isEmpty(comment.getClassIds())?new ArrayList<String>():comment.getClassIds());
		map.put("comment", comment.getComment());
		map.put("ctime", comment.getCtime());
		map.put("noti_id", comment.getNotiId());
		map.put("relay", comment.getRelay());
		map.put("relay_to", StringUtils.isBlank(comment.getRelayTo())?"" :comment.getRelayTo() );
		return map;
	}

	public List<HashMap<String, Object>> covertNotificationData(
			List<Notification> notis, String userId) throws Exception {

		List<HashMap<String, Object>> resultList = new ArrayList<>();
		if (notis != null && notis.size() > 0 ) {
			for (Notification n : notis) {
				HashMap<String, Object> t = new HashMap<>();

				t.put("class_id", CollectionUtils.isEmpty(n.getClassIds())?new String[]{}:n.getClassIds());
				t.put("class_name", CollectionUtils.isEmpty(n.getClassNames())?new String[]{}:n.getClassNames());
				t.put("content", n.getContent());
				t.put("ctime", n.getCtime());
				t.put("noti_id", n.getId().toHexString());
				t.put("owner", StringUtils.isBlank(n.getOwner()) ? "" : n.getOwner());
				t.put("owner_name", StringUtils.isBlank(n.getOwnerName()) ? "" : n.getOwnerName());
				t.put("target_id", CollectionUtils.isEmpty(n.getTargetIds())?new String[]{}:n.getTargetIds());
				t.put("target_name", CollectionUtils.isEmpty(n.getTargetNames())?new String[]{}:n.getTargetNames());
				t.put("type", n.getType());
				
				if (!StringUtils.isBlank(n.getActiontTime())){
					t.put("action_time", n.getActiontTime());
				}
				if (StringUtils.isNotBlank(n.getType())){ // 日程的type 不为空
					if (!userId.equals(n.getOwner())){
						continue;
					}
					t.put("type", n.getType());
				}
//				 t.put("viewed", jedis.cardViewedNoti(n.getId().toString()));
				 t.put("checked_count", jedis.cardConfirmNoti(n.getId().toString()));
				 t.put("noti_type", StringUtils.isBlank(n.getNotiType()) ? "" : n.getNotiType());
				List<Attach> as = n.getAttaches();
				if (as != null) {
					List<HashMap<String, Object>> atts = new ArrayList<>();
					for (Attach a : as) {
						HashMap<String, Object> t1 = new HashMap<String, Object>();
						t1.put("attach_type", a.getAttachType());
						t1.put("attach_url", a.getAttachUrl());
						if (StringUtils.isNotBlank(n.getNotiType()) && "4".equals(n.getNotiType())){
							t1.put("pic_height", a.getPicHeight());
							t1.put("pic_width", a.getPicWidth());
						}
						atts.add(t1);
					}
					t.put("attaches", atts);
				}
				List<Comment> commsList = commentDao.findCommentAboutMe(n
						.getId().toHexString(), n.getOwner(), userId);
				List<HashMap<String, Object>> ccs = new ArrayList<>();
				for (Comment c : commsList) {
					HashMap<String, Object> t1 = new HashMap<String, Object>();
					t1.put("cid", c.getId().toHexString());
					t1.put("class_id", c.getClassIds());
					t1.put("relay", c.getRelay());
					t1.put("relay_to", StringUtils.isBlank(c.getRelayTo())?"":c.getRelayTo());
					t1.put("comment", c.getComment());
					t1.put("ctime", c.getCtime());
					ccs.add(t1);
				}
				t.put("comments", ccs);
				resultList.add(t);
				jedis.incrViewedNoti(n.getId().toHexString());
			}
		}

		return resultList;
	}

//	@Deprecated
//	public List<Notification> boxNotificationDataToList(
//			HashMap<String, Object> contentMap) throws Exception {
//
//		List<Notification> resultList = new ArrayList<>();
//		Notification noti = new Notification();
//		String classId = (String) contentMap.get("class_id");
//		String className = (String) contentMap.get("class_name");
//		String owner = (String) contentMap.get("owner");
//		String ownerName = (String) contentMap.get("owner_name");
//		String content = (String) contentMap.get("content");
//		Object attaches = contentMap.get("attaches");
//		Object targets = contentMap.get("targets");
//		noti.setContent(content);
//		noti.setOwner(owner);
//		noti.setClassName(className);
//		noti.setOwnerName(ownerName);
//		noti.setCtime(new Date());
//		if (targets != null) {
//			noti.setTargets((List<String>) targets);
//		}
//		List<Attach> attachList = new ArrayList<Attach>();
//		if (attaches != null) {
//			List<HashMap<String, Object>> attList = (List<HashMap<String, Object>>) attaches;
//			for (HashMap<String, Object> a : attList) {
//				Attach attach = new Attach();
//				attach.setAttachType((String) a.get("attach_type"));
//				attach.setAttachUrl((String) a.get("attach_url"));
//				attachList.add(attach);
//			}
//			noti.setAttaches(attachList);
//		}
//		StringTokenizer st = new StringTokenizer(classId, ",");
//		while (st.hasMoreTokens()) {
//			String singleClassId = st.nextToken();
//			Classu classu = classDao.findClassNameOwnerById(singleClassId);
//			// 如果没有这个班级，则发布失败，如果此班级的owner与上传的owner不符则发布失败
//			if (classu == null || !owner.equals(classu.getOwner())) {
//				continue;
//			}
//			Notification cloneNoti = (Notification) noti.clone(); // 浅层复制，attachList//
//																	// 为同一个。
//			cloneNoti.setClassId(singleClassId);
//			cloneNoti.setClassName(classu.getClassName());
//			resultList.add(cloneNoti);
//		}
//		return resultList;
//	}
	
	public Notification boxNotificationData(
			HashMap<String, Object> contentMap) throws Exception {
		Notification noti = new Notification();
		Object classIds =  contentMap.get("class_id");
		Object classNames =  contentMap.get("class_name");
		String owner = (String) contentMap.get("owner");
		String ownerName = (String) contentMap.get("owner_name");
		String content = (String) contentMap.get("content");
		Object attaches = contentMap.get("attaches");
		Object targetIds = contentMap.get("target_id");
		Object targetNames = contentMap.get("target_name");
		String actionTime = (String) contentMap.get("action_time");
		String notiType = (String) contentMap.get("noti_type");
		
		if (!StringUtils.isBlank(notiType)) {
			noti.setNotiType(notiType);
		}
		
		if (classIds != null && classIds instanceof List<?>) {
			noti.setClassIds((List<String>) classIds);
		}
		
		if (classNames != null && classNames instanceof List<?>) {
			noti.setClassNames((List<String>) classNames);
		}
		
		if (targetIds != null && targetIds instanceof List<?>) {
			noti.setTargetIds((List<String>) targetIds);
		}
		
		if (targetNames != null && targetNames instanceof List<?>) {
			noti.setTargetNames((List<String>) targetNames);
		}
		if (!StringUtils.isBlank(actionTime)) {
			noti.setActiontTime(actionTime);
		}
		
		
		noti.setContent(content);
		noti.setOwner(owner);
		noti.setOwnerName(ownerName);
		noti.setCtime(new Date());
		List<Attach> attachList = new ArrayList<Attach>();
		if (attaches != null) {
			List<HashMap<String, Object>> attList = (List<HashMap<String, Object>>) attaches;
			for (HashMap<String, Object> a : attList) {
				Attach attach = new Attach();
				attach.setAttachType((String) a.get("attach_type"));
				attach.setAttachUrl((String) a.get("attach_url"));
				if (!StringUtils.isBlank(notiType) && "4".equals(notiType)){
					String picWidth = (String) a.get("pic_width");
					String picHeight = (String) a.get("pic_height");
					attach.setPicWidth(picWidth);
					attach.setPicHeight(picHeight);
				}
				attachList.add(attach);
			}
			noti.setAttaches(attachList);
		}
		
		return noti;
	}

	public HashMap<String, Object> covertSingleNotificationData(Notification n) {
		HashMap<String, Object> t = new HashMap<>();
		t.put("class_id", n.getClassIds());
		t.put("noti_id", n.getId().toHexString());
		t.put("class_name",
				CollectionUtils.isEmpty(n.getClassNames()) ? new String[] {}
						: n.getClassNames());
		t.put("content",
				StringUtils.isBlank(n.getContent()) ? "" : n.getContent());
		t.put("type",
				StringUtils.isBlank(n.getType()) ? "" : n.getType());
		t.put("ctime", n.getCtime());
		t.put("owner", StringUtils.isBlank(n.getOwner()) ? "" : n.getOwner());
		t.put("owner_name",
				StringUtils.isBlank(n.getOwnerName()) ? "" : n.getOwnerName());
		t.put("target_id",
				CollectionUtils.isEmpty(n.getTargetIds()) ? new String[] {} : n
						.getTargetIds());
		t.put("target_name",
				CollectionUtils.isEmpty(n.getTargetNames()) ? new String[] {} : n
						.getTargetNames());
		if (!StringUtils.isBlank(n.getActiontTime())){
			t.put("action_time", n.getActiontTime());
		}
		
		// t.put("viewed", jedis.cardViewedNoti(n.getId().toString()));
		t.put("viewed", jedis.cardConfirmNoti(n.getId().toString()));
		t.put("noti_type", StringUtils.isBlank(n.getNotiType()) ? "" : n.getNotiType());
		List<Attach> as = n.getAttaches();
		if (as != null) {
			List<HashMap<String, Object>> atts = new ArrayList<>();
			for (Attach a : as) {
				HashMap<String, Object> t1 = new HashMap<String, Object>();
				t1.put("attach_type", a.getAttachType());
				t1.put("attach_url", a.getAttachUrl());
				if (StringUtils.isNotBlank(n.getNotiType()) && "4".equals(n.getNotiType())){
					t1.put("pic_height", a.getPicHeight());
					t1.put("pic_width", a.getPicWidth());
				}
				atts.add(t1);
			}
			t.put("attaches", atts);
		}
		jedis.incrViewedNoti(n.getId().toHexString());
		return t;
	}
	

	

	public Notification boxSingleNotificationData(
			HashMap<String, Object> contentMap) throws Exception {

		Notification noti = new Notification();
		Object classIds =   contentMap.get("class_id");
		Object classNames =   contentMap.get("class_name");
		String owner = (String) contentMap.get("owner");
		String ownerName = (String) contentMap.get("owner_name");
		String content = (String) contentMap.get("content");
		Object attaches = contentMap.get("attaches");
		Object targetIds = contentMap.get("target_id");
		Object targetNames = contentMap.get("target_name");
		String notiType = (String) contentMap.get("noti_type");
		
		if (!StringUtils.isBlank(notiType)) {
			noti.setNotiType(notiType);
			
		}
		if (classIds != null && classIds instanceof List<?>) {
			noti.setClassIds((List<String>) classIds);
		}
		
		if (classNames != null && classNames instanceof List<?>) {
			noti.setClassNames((List<String>) classNames);
		}
		if (!StringUtils.isBlank(content)) {
			noti.setContent(content);
		}
		if (!StringUtils.isBlank(owner)) {
			noti.setOwner(owner);
		}
		if (!StringUtils.isBlank(ownerName)) {
			noti.setOwnerName(ownerName);
		}
		
		noti.setCtime(new Date());
		if (targetIds != null && targetIds instanceof List<?>) {
			noti.setTargetIds((List<String>) targetIds);
		}
		if (targetNames != null && targetNames instanceof List<?>) {
			noti.setTargetNames((List<String>) targetNames);
		}
		List<Attach> attachList = new ArrayList<Attach>();
		if (attaches != null) {
			List<HashMap<String, Object>> attList = (List<HashMap<String, Object>>) attaches;
			for (HashMap<String, Object> a : attList) {
				Attach attach = new Attach();
				attach.setAttachType((String) a.get("attach_type"));
				attach.setAttachUrl((String) a.get("attach_url"));
				if (!StringUtils.isBlank(notiType) && "4".equals(notiType)){
					String picWidth = (String) a.get("pic_width");
					String picHeight = (String) a.get("pic_height");
					attach.setPicWidth(picWidth);
					attach.setPicHeight(picHeight);
				}
				attachList.add(attach);
			}
			noti.setAttaches(attachList);
		}
		return noti;
	}

	
	
	
	
	public HashMap<String, Object> convertChildData(Child child) {

		HashMap<String, Object> temp = new HashMap<String, Object>();
		if (child != null) {
			temp.put("child_id", child.getId().toHexString());
			temp.put("child_name", child.getChildName());
			temp.put("child_photo", child.getChildPhoto());
			temp.put("parents", child.getParents());
		}
		return temp;
	}

	public HashMap<String, Object> convertUserData(User user) {
		HashMap<String, Object> temp = new HashMap<String, Object>();
		temp.put("name", user.getName());
		temp.put("mobile", user.getMobile());
		temp.put("type", user.getType());
		if (user.getCtime() != null) {
			temp.put("ctime", user.getCtime());
//			DateFormatUtils.format(user.getCtime(),"yyyy-MM-dd hh:mm:ss"));
		}
		temp.put("gender", String.valueOf(user.getGender()));
		temp.put("email", StringUtils.isBlank(user.getEmail())?"":user.getEmail());
		temp.put("pushflag", user.getPushflag());
		temp.put("smsflag", user.getSmsflag());
		temp.put("emailflag", user.getEmailflag());
		temp.put("user_id", user.getId().toHexString());
		return temp;
	}

	public User boxUserData(HashMap<String, Object> params) {
		User user = new User();
		String mobile = (String) params.get("mobile");
		String name = (String) params.get("name");
		String pushflag = (String) params.get("pushflag");
		String smsflag = (String) params.get("smsflag");
		String emailflag = (String) params.get("emailflag");
		String gender = (String) params.get("gender");
		String password = (String) params.get("password");
		String type = (String) params.get("type");

		
		if (mobile != null) {
			user.setMobile(mobile);
		}
		if (name != null) {
			user.setName(name);
		}
		if (gender != null) {
			user.setGender(gender);
		}
		if (password != null) {
			user.setPassword(password);
		}
		if (type != null) {
			user.setType(type);
		}
		if (pushflag != null) {
			user.setPushflag(pushflag);
		}
		if (smsflag != null) {
			user.setSmsflag(smsflag);
		}
		if (emailflag != null) {
			user.setEmailflag(emailflag);
		}
		return user;
	}

	
	public Client convertMapToClient(HashMap<String, Object> contentMap){
		Client saveClient = new Client();
		saveClient.setClientName((String) contentMap.get("client_name"));
		saveClient.setClientVer((String) contentMap.get("client_ver"));
		saveClient.setDeviceId((String) contentMap.get("device_id"));
		saveClient.setDeviceToken((String) contentMap.get("device_token"));
		saveClient.setUserId((String) contentMap.get("user_id"));
		saveClient.setPhoneType((String) contentMap.get("phone_type"));
		return saveClient;
	}
}
