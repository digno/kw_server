package nz.co.rubz.kiwi.service.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.UpdateResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nz.co.rubz.kiwi.KiwiBiz;
import nz.co.rubz.kiwi.MsgConstants;
import nz.co.rubz.kiwi.bean.Child;
import nz.co.rubz.kiwi.bean.Classu;
import nz.co.rubz.kiwi.bean.ClassuMember;
import nz.co.rubz.kiwi.bean.Notification;
import nz.co.rubz.kiwi.bean.Parent;
import nz.co.rubz.kiwi.bean.User;
import nz.co.rubz.kiwi.dao.ChildDao;
import nz.co.rubz.kiwi.dao.ClassDao;
import nz.co.rubz.kiwi.dao.CommentDao;
import nz.co.rubz.kiwi.dao.NotifyDao;
import nz.co.rubz.kiwi.dao.ScheduleDao;
import nz.co.rubz.kiwi.dao.UserDao;
import nz.co.rubz.kiwi.protocol.beans.Content;
import nz.co.rubz.kiwi.protocol.converter.ResponseContentHelper;

@Service
@Deprecated
public class ClassServiceImpl {

	private Logger log = Logger.getLogger(ClassServiceImpl.class);

	@Autowired
	private ClassDao classDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private ChildDao childDao;
	
	@Autowired
	private NotifyDao notiDao;
	
	@Autowired
	private ScheduleDao scheduleDao;
	
	@Autowired
	private CommentDao commentDao;
	
	
	@Autowired
	private BizNoticePublisher noticePublisher;

	@Autowired
	private ClassuDataWrapper dataWrapper;


	// @PostConstruct
	// public void init() {
	// log.info("  inited  ");
	// }

	@KiwiBiz("create")
	public Content createClass(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		Classu saveClassu = new Classu();
		dataWrapper.boxClassData(saveClassu, contentMap);
//		populateClass(saveClassu, contentMap);
		saveClassu.setCtime(new Date());
		if (StringUtils.isEmpty(saveClassu.getClassId())) {
			String classId = RandomStringUtils.randomNumeric(6);
			saveClassu.setClassId(classId);
			saveClassu.setClassMail(classId + "@classu.com");
		}
		Key<Classu> akey = classDao.save(saveClassu);
		Object id = akey.getId();
		if (id != null) {
//			HashMap<String, Object> classuMap = convertClassuData(saveClassu);
			HashMap<String, Object> classuMap = dataWrapper.convertClassuData(saveClassu);
			resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
			resultMap.put(MsgConstants.CLASSU, classuMap);
			resultContent.setData(resultMap);
		}
		return resultContent;
	}

	@KiwiBiz("find")
	public Content findClass(HashMap<String, Object> contentMap)
			throws Exception {
		return findClassById(contentMap);
	}

	@KiwiBiz("join")
	public Content joinClass(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		String classId = (String) contentMap.get("class_id");
		String userId = (String) contentMap.get("user_id");
		String childName = (String) contentMap.get("child_name");
		String relation = (String) contentMap.get("relation");
		User user = userDao.findById(userId);
		if (user == null) {
			return ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_2, "user " + userId
									+ " does not exsit, join class " + classId
									+ " failed.");
		}
		ClassuMember cm = new ClassuMember();
		cm.setChildName(childName);
		cm.setRelation(relation);
		cm.setUserId(userId);
		Child child = childDao
				.findByChildNameAndParent(childName, userId);
		HashMap<String,Object> resultMap = new HashMap<String,Object>();
		if (child==null) {
			log.info("save new child :[ " + childName + " ].");
			Parent parent = new Parent();
			parent.setPid(userId);
			parent.setName(user.getName());
			parent.setRelation(relation);
			UpdateResults r1 = childDao.modifyChildPhoto(childName, "", parent); // 更新孩子信息，但是没有头像
			resultMap.put("child_id", r1.getWriteResult().getUpsertedId().toString());
			cm.setChildId(r1.getWriteResult().getUpsertedId().toString()); //如果是新增孩子获取孩子ID并加入classMember
		}else {
			resultMap.put("child_id", child.getId().toHexString());
			if (!child.getId().toHexString().equals(userId)){
				cm.setChildId(child.getId().toHexString());
			}
			
		}
		UpdateResults result = classDao.addClassuMember(classId, cm);
		if (result.getUpdatedCount() > 0) {
			resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
			resultMap.put(MsgConstants.MSG, "user " + userId
					+ " join class " + classId + " successed.");
			resultContent.setData(resultMap);
//			resultContent = ResponseContentHelper
//					.genSimpleResponseContentWithoutType(
//							MsgConstants.ERROR_CODE_0, "user " + userId
//									+ " join class " + classId + " successed.");
			//  给老师发通知
			noticePublisher.pubJoinClassNotice(contentMap);
		} else {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_1, "user " + userId
									+ " join class " + classId + " failed.");
		}
		return resultContent;
	}

	@KiwiBiz("modify")
	public Content modifyClass(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		String classId = (String) contentMap.get("class_id");
		Classu classu = classDao.findOne("class_id", classId);
		if (classu != null) {
//			populateClass(classu, contentMap);
			dataWrapper.boxClassData(classu, contentMap);
			// TODO 给家长发通知
			if (classDao.updateClassu(classId, classu)) {
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_0,
								"update Class success!");
				noticePublisher.pubModifyClassNotice(classId);
			} else {
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_1,
								"update class failed!");
			}

		} else {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_2, "no such class!");
		}

		return resultContent;
	}

	@KiwiBiz("children")
	public Content getClassChildren(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		String classId = (String) contentMap.get("class_id");
		List<String> children = classDao.findChildren(classId);
		resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
		resultMap.put(MsgConstants.CHILDREN, children.toArray());
		resultContent.setData(resultMap);
		return resultContent;
	}

	@KiwiBiz("remove_members")
	public Content removeClassMembers(HashMap<String, Object> contentMap)
			throws Exception {
		// TODO 是否需要判断是否删除用户权限?
		Content resultContent = new Content();
		String classId = (String) contentMap.get("class_id");
		String owner = (String) contentMap.get("owner");
		String userId = (String) contentMap.get("user_id");
		boolean result = classDao.removeClassuMember(classId, owner,userId);
		if (result) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_0, "remove  Class "
									+ classId + " user " + userId
									+ " successed!");
			noticePublisher.pubRemoveMembersNotice(contentMap);
		} else {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_1, "remove  Class "
									+ classId + " user " + userId + " failed!");
		}
		return resultContent;
	}

	@KiwiBiz("remove")
	public Content removeClass(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		String classId = (String) contentMap.get("class_id");
		String owner = (String) contentMap.get("owner");
		// 通知先发，否则不知道原班级成员
		noticePublisher.pubRemoveClassNotice(classId);
		boolean result = classDao.deleteClass(classId,owner);
		if (result) {
			List<Notification> notis = notiDao.findNotisByClassId(classId,-1);
			for (Notification n : notis){
				notiDao.delete(n);
				scheduleDao.deleteScheduleByNoti(n.getId().toHexString());
				commentDao.deleteCommentByNotiId(n.getId().toHexString());
			}
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_0,
							"delete Class successed.");
		} else {
			log.error("delete Class failed! remove Class is " + result);
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_2, "Class " + classId
									+ " not exists.");
		}
		return resultContent;
	}

	// see remove_members
	@KiwiBiz("exit")
	public Content exitClass(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		String classId = (String) contentMap.get("class_id");
		String userId = (String) contentMap.get("user_id");
		boolean result = classDao.removeClassuMember(classId, "",userId);
//		boolean result = true;
		if (result) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_0, "user " + userId
									+ " exit Class " + classId + " successed!");
			noticePublisher.pubExitClassNotice(classId,userId);
		} else {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_1, "user " + userId
									+ " exit Class" + classId + " failed!");
		}
		return resultContent;
	}

	@KiwiBiz("info")
	public Content getClassInfo(HashMap<String, Object> contentMap)
			throws Exception {
		return findClassById(contentMap);
	}

	@KiwiBiz("myclass")
	public Content getMyClassInfo(HashMap<String, Object> contentMap)
			throws Exception {
		// 我的班级有2个概念，第一个是 我创建的班级和我加入的班级。
		// 1 ：findClassByOwner 2: findClassByMember
		// db.Classu.find({"$or":[{"owner":"mobile"},{"members.mobile":"mobile"}]})
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		String userId = (String) contentMap.get("user_id");
		if (!StringUtils.isBlank(userId)) {
			List<Classu> joined = classDao.findClassByMember(userId);
			List<Classu> created = classDao.findClassByOwner(userId);
			if (joined != null || created != null) {
				List<HashMap<String, Object>> joinedList = new ArrayList<>();
				List<HashMap<String, Object>> createdList = new ArrayList<>();
				for (Classu c : joined) {
					HashMap<String, Object> tmp = dataWrapper.convertClassuData(c);
					joinedList.add(tmp);
				}
				for (Classu c : created) {
					HashMap<String, Object> tmp = dataWrapper.convertClassuData(c);
					createdList.add(tmp);
				}

				resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
				resultMap.put(MsgConstants.JOINED_CLASSES, joinedList);
				resultMap.put(MsgConstants.CREATED_CLASSES, createdList);
				resultContent.setData(resultMap);
			} else {
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_1, userId
										+ " not join/create any classes .");
			}
		} else {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_4,
							"can not query with null field.");
		}
		return resultContent;
	}

	@KiwiBiz("members")
	public Content getClassMembers(HashMap<String, Object> contentMap)
			throws Exception {
		return findClassById(contentMap);
	}

	private Content findClassById(HashMap<String, Object> contentMap) {
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		boolean withMembers = false;
		String classId = (String) contentMap.get("class_id");
		Object type = contentMap.get("type");
		if (type != null && BizConstants.TEACHER_TYPE.equals(type.toString())) {
			withMembers = true;
		}
		if (!StringUtils.isBlank(classId)) {
			Classu result = classDao.findClassById(classId, withMembers);
			if (result != null) {
				HashMap<String, Object> classuMap = dataWrapper.convertClassuData(result);
				resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
				resultMap.put(MsgConstants.CLASSU, classuMap);
				resultContent.setData(resultMap);
			} else {
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_1, "no such class ."
										+ classId);
			}

		} else {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_4,
							"can not query with null field.");
		}
		return resultContent;
	}

	



	
}
