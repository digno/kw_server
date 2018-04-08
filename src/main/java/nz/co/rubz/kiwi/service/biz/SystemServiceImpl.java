package nz.co.rubz.kiwi.service.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import nz.co.rubz.kiwi.KiwiBiz;
import nz.co.rubz.kiwi.MsgConstants;
import nz.co.rubz.kiwi.bean.Child;
import nz.co.rubz.kiwi.bean.Classu;
import nz.co.rubz.kiwi.bean.ClassuMember;
import nz.co.rubz.kiwi.bean.Comment;
import nz.co.rubz.kiwi.bean.Notification;
import nz.co.rubz.kiwi.bean.User;
import nz.co.rubz.kiwi.dao.ChildDao;
import nz.co.rubz.kiwi.dao.ClassDao;
import nz.co.rubz.kiwi.dao.CommentDao;
import nz.co.rubz.kiwi.dao.MessageDao;
import nz.co.rubz.kiwi.dao.NotifyDao;
import nz.co.rubz.kiwi.dao.UserDao;
import nz.co.rubz.kiwi.jedis.JedisOperator;
import nz.co.rubz.kiwi.protocol.beans.Content;
import nz.co.rubz.kiwi.protocol.converter.ResponseContentHelper;

@Service
public class SystemServiceImpl {
	private Logger log = Logger.getLogger(SystemServiceImpl.class);

	@Autowired
	private MessageDao messageDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ClassDao classDao;
	@Autowired
	private NotifyDao notifyDao;
	@Autowired
	private CommentDao commentDao;
	@Autowired
	private ChildDao childDao;

	@Autowired
	private JedisOperator jedis;
	
	@Autowired
	private ClassuDataWrapper dataWrapper;

	@KiwiBiz("tide")
	public Content genTideData(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultContent.setData(resultMap);
		return resultContent;
	}
	
	@KiwiBiz("ping")
	public Content genPingData(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
//		resultMap.put(MsgConstants.PONG, MsgConstants.ERROR_CODE_0);
		resultContent.setData(resultMap);
		return resultContent;
	}

	// @ClassuBiz("get_undeliver_msg")
	public Content getUndeliverMessages(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		String userId = (String) contentMap.get("user_id");
		String type = (String) contentMap.get("type");
		if (type == null) {
			type = "";
			DBCursor cursor = messageDao.findUndeliverMessages(userId, type);
			cursor.sort(new BasicDBObject("createDate", -1));
			while (cursor.hasNext()) {
				DBObject d = cursor.next();
				log.info("---------------\r\n" + d.toString());
				String id = (String) d.get("msg_id");
				messageDao.removeMessage(id);
			}
		}
		resultContent = ResponseContentHelper
				.genSimpleResponseContentWithoutType(MsgConstants.ERROR_CODE_0,
						"message will deliver.");
		return resultContent;
	}

	@KiwiBiz("init")
	public Content initUserData(HashMap<String, Object> contentMap) {
		Content resultContent = new Content();
		String userId = (String) contentMap.get("user_id");
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		if (!StringUtils.isBlank(userId)) {
			User user = userDao.findById(userId);
			List<Classu> joinedClasses = classDao.findJoinedClasses(userId);
			List<Classu> createdClasses = classDao.findCreatedClasses(userId);
			List<Child> children = childDao.findByParent(userId);
			List<Classu> classes = new ArrayList<Classu>();
			classes.addAll(joinedClasses);
			classes.addAll(createdClasses);
			List<String> classIds = new ArrayList<String>();
			for (Classu c : classes) {
				classIds.add(c.getClassId());
			}
			List<Notification> notis = notifyDao.findNotisByClassIds(classIds,0);
			List<Comment> comments = commentDao.findComment(userId);
			List<HashMap<String, Object>> joinedClassMapList = new ArrayList<>();
			List<HashMap<String, Object>> createdClassMapList = new ArrayList<>();
			List<HashMap<String, Object>> childrenMapList = new ArrayList<>();
			List<HashMap<String, Object>> notisMapList = new ArrayList<>();
			List<HashMap<String, Object>> commentsMapList = new ArrayList<>();
			if (createdClasses != null && createdClasses.size() > 0) {
				for (Classu c : createdClasses) {
					createdClassMapList.add(dataWrapper.convertClassuData(c));
				}
			}
			if (joinedClasses != null && joinedClasses.size() > 0) {
				for (Classu c : joinedClasses) {
					List<ClassuMember> clasuMembers = c.getMembers();
					// 只把本人信息放在members里面返回
					List<ClassuMember> filteredMembers = new ArrayList<>();
					for (ClassuMember cm : clasuMembers) {
						if (userId.equals(cm.getUserId())) {
							filteredMembers.add(cm);
						}
					}
					c.setMembers(filteredMembers);
					joinedClassMapList.add(dataWrapper.convertClassuData(c));
				}
			}
			if (children != null && children.size() > 0) {
				for (Child c : children) {
					childrenMapList.add(dataWrapper.convertChildData(c));
				}
			}
			if (notis != null && notis.size() > 0) {
				for (Notification n : notis) {
					if ("1".equals(n.getType()) && !userId.equals(n.getOwner())){
						continue;
					}
					if (CollectionUtils.isNotEmpty(n.getTargetIds())){
						if (!n.getTargetIds().contains(userId)){
							continue;
						}
					}
					HashMap<String, Object> result = dataWrapper.covertSingleNotificationData(n);
					result.put("confirmed", jedis.isConfirmedNoti(n.getId().toHexString(),userId)?"1":"0");
					result.put("checked_count", jedis.cardConfirmNoti(n.getId().toHexString()));
					notisMapList.add(result);
				}
			}
			if (comments != null && comments.size() > 0) {
				log.info("IN init method user comments size is : " + comments.size() + " user_id is :" + userId);
				for (Comment c : comments) {
					commentsMapList.add(dataWrapper.convertCommentData(c));
				}
			}
			resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
			resultMap.put(MsgConstants.USER, dataWrapper.convertUserData(user));
			resultMap.put(MsgConstants.JOINED_CLASSES, joinedClassMapList);
			resultMap.put(MsgConstants.CREATED_CLASSES, createdClassMapList);
			resultMap.put(MsgConstants.CHILDREN, childrenMapList);
			resultMap.put(MsgConstants.NOTIS, notisMapList);
			resultMap.put(MsgConstants.COMMENTS, commentsMapList);
			resultContent.setData(resultMap);
		} else {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_4,
							"can not init user data with null mobile.");

		}
		return resultContent;
	}

}
