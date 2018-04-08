package nz.co.rubz.kiwi.websocket;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import nz.co.rubz.kiwi.protocol.beans.KiwiMessage;
import nz.co.rubz.kiwi.protocol.beans.Content;
import nz.co.rubz.kiwi.protocol.converter.ProtocolConverter;
import nz.co.rubz.kiwi.utils.DateUtils;

public class TestMessageGenerator {

	private static Logger log = Logger.getLogger(TestMessageGenerator.class);
	
	private static String getMessageid(String userId, String datestr) {

		String baseStr = userId + "1234" + datestr;
//		 String baseStr = userId + "q123456" + datestr;
		log.info(baseStr +" " + DigestUtils.md5Hex(baseStr.getBytes()) );
		return DigestUtils.md5Hex(baseStr.getBytes());
	}

	// GEN MSGS
	public static KiwiMessage genBasicMessage(String subject, String from,
			String to) {
		KiwiMessage message = new KiwiMessage();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		String dateStr = sdf.format(new Date());
		message.setCreateDate(dateStr);
		message.setFrom(from);
		message.setMsg_id(getMessageid(from, dateStr));
		message.setStatus("0");
		message.setSubject(subject);
		message.setTo(to);
		message.setVersion("1.0");
		return message;

	}

	// 用户登陆
	public static String genLoginMessage(String userId,String mobile) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("user", userId, "server");
		Content content = new Content();
		content.setType("login");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("mobile", mobile);
		map.put("password", "1234");
		content.setData(map);
		message.setContent(content);
		message.setMsg_id("-1");
		return ProtocolConverter.unmarshallMsg(message);
	}

	// 用户注册
	public static String genInitMessage(String userId) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("system", userId, "server");
		Content content = new Content();
		content.setType("init");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("user_id",userId);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	// 用户注册
	public static String genRegMessage(String mobile, String nickname) {
		KiwiMessage message = genBasicMessage("user", "-1", "server");
		Content content = new Content();
		content.setType("register");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("mobile",mobile);
		map.put("password", "1234");
		map.put("name", nickname);
		map.put("gender", "0");
		map.put("type", "0");
		content.setData(map);
		message.setContent(content);
		message.setMsg_id("-1");
		return ProtocolConverter.unmarshallMsg(message);
	}

	// 获取用户列表
	public static String genGetUserListMessage(String userId) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("user", userId, "server");
		Content content = new Content();
		content.setType("getUserList");
		HashMap<String, Object> map = new HashMap<String, Object>();
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	// 获取用户信息
	public static String genGetUserDetailMessage(String userId,
			String targetMobile) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("user", userId, "server");
		Content content = new Content();
		content.setType("get_userinfo");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", targetMobile);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	// 绑定邮箱
	public static String genBindEmailMessage(String userId, String emailAddress) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("user", userId, "server");
		Content content = new Content();
		content.setType("bind_email");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userId);
		map.put("email", emailAddress);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	// 修改密码
	public static String genChangePasswordMessage(String userId,
			String oldPass, String newPass) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("user", userId, "server");
		Content content = new Content();
		content.setType("change_password");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userId);
		map.put("old_password", oldPass);
		map.put("new_password", newPass);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	// 忘记密码
	public static String genResetPasswordMessage(String userId,String mobile,
			String verifyCode, String newPassword) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("user", userId, "server");
		Content content = new Content();
		content.setType("forgot_password");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("mobile", mobile);
		map.put("verify_code", verifyCode);
		map.put("new_password", newPassword);
		content.setData(map);
		message.setContent(content);
		
		return ProtocolConverter.unmarshallMsg(message);
	}

	// 获取验证码
	public static String genGetVerifyCodeMessage(String userId,String mobile) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("user", userId, "server");
		Content content = new Content();
		content.setType("get_verify_code");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("mobile", mobile);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}
	
	

	// 忘记密码
	public static String genGetViewsMessage(String userId,
			String notiId) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("notify", userId, "server");
		Content content = new Content();
		content.setType("get_views");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("noti_id", notiId);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	

	// 对勾
	public static String genConfirmedMessage(String userId,
			String notiId,String confirmed) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("notify", userId, "server");
		Content content = new Content();
		content.setType("confirm");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userId);
		map.put("noti_id", notiId);
		map.put("confirmed", confirmed);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}
	
	


	// 更新用户信息
	public static String genModifyClassMessage(String userId, String cid,
			String className) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("class", userId, "server");
		Content content = new Content();
		content.setType("modify");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("class_id", cid);
		map.put("class_name", className);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	public static String genGetChildrenMessage(String userId, String cid) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("class", userId, "server");
		Content content = new Content();
		content.setType("children");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("class_id", cid);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	// 更新用户信息
	public static String genModifyUserMessage(String userId, String nickname) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("user", userId, "server");
		Content content = new Content();
		content.setType("modify_user");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userId);
		map.put("name", nickname);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	public static String genCreateClassuMessage(String userId, String aid,
			String className, String ownerName) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("class", userId, "server");
		Content content = new Content();
		content.setType("create");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("class_name", className);
		map.put("owner_name", ownerName);
		map.put("owner", userId);
		map.put("class_id", aid);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	public static String genGetClassMessage(String userId, String cid,
			String type) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("class", userId, "server");
		Content content = new Content();
		content.setType("find");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("class_id", cid);
		map.put("type", type);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	// 查询一个班级
	public static String genFindClassMessage(String userId, String cid,
			String type) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("class", userId, "server");
		Content content = new Content();
		content.setType("find");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("class_id", cid);
		map.put("type", type);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	public static String genFindChlidMessage(String userId) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("user", userId, "server");
		Content content = new Content();
		content.setType("my_kids");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userId);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	@Deprecated
	public static String genFindChlidByNameMessage(String userId,
			String childName) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("user", userId, "server");
		Content content = new Content();
		content.setType("get_kid");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("child_name", childName);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	public static String genModifyChlidMessage(String userId, String childId,String childName, String photo) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("user", userId, "server");
		Content content = new Content();
		content.setType("modify_child");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("child_name", childName);
		map.put("child_photo", photo);
		map.put("user_id", userId);
		map.put("child_id", childId);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	public static String genGetPushflagMessage(String userId) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("user", userId, "server");
		Content content = new Content();
		content.setType("get_push_flag");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userId);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	public static String genSetPushflagMessage(String userId, String pushFlag) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("user", userId, "server");
		Content content = new Content();
		content.setType("set_push_flag");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userId);
		map.put("pushflag", pushFlag);
		map.put("smsflag", "0");
		map.put("emailflag", "0");
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}
	
	public static String genChangeMobileMessage(String userId, String newMobile) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("user", userId, "server");
		Content content = new Content();
		content.setType("change_mobile");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userId);
		map.put("new_mobile", newMobile);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	// 加入班级列表
	public static String genJoinClassMessage(String userId, String cid,
			String childName, String relation) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("class", userId, "server");
		Content content = new Content();
		content.setType("join");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("class_id", cid);
		map.put("user_id", userId);
		map.put("child_name", childName);
		map.put("relation", relation);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	public static String genExitClassMessage(String userId, String aid) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("class", userId, "server");
		Content content = new Content();
		content.setType("exit");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("class_id", aid);
		map.put("user_id", userId);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	public static String genRemoveClassMembersMessage(String userId,
			String cid, String target) {
		// String[] m = mobile.split("\\|");
//		String[] m1 = target.split("\\|");
		KiwiMessage message = genBasicMessage("class", userId, "server");
		Content content = new Content();
		content.setType("remove_members");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("class_id", cid);
		map.put("owner", userId);
		map.put("user_id", target);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	public static String genSuggestMessage(String userId) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("client", userId, "server");
		Content content = new Content();
		content.setType("suggest");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userId);
		map.put("title", "标题");
		map.put("content", "建议内容 ");
		map.put("type", "0");
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	public static String genReportClientInfoMessage(String userId) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("client", userId, "server");
		Content content = new Content();
		content.setType("report");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userId);
		map.put("phone_type", "IC101");
		map.put("device_id", "100000000000000000");
		map.put("device_token", "10000 000000 000 00000");
		map.put("client_name", "CLASSUAPP_IC101");
		map.put("client_ver", "1.0");
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	public static String genRemoveClassMessage(String userId, String aid) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("class", userId, "server");
		Content content = new Content();
		content.setType("remove");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("class_id", aid);
		map.put("owner", userId);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	public static String genGetUndelieverMessage(String userId, String type) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("system", userId, "server");
		Content content = new Content();
		content.setType("get_undeliver_msg");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userId);
		map.put("type", type);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	public static String genJoinedClassesMessage(String userId) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("user", userId, "server");
		Content content = new Content();
		content.setType("joined_class");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userId);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	public static String genSaveScheduleMessage(String userId,
			String[] classid, String ccc,int delayMints) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("schedule", userId, "server");
		Content content = new Content();
		content.setType("save");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("class_id", classid);
		map.put("owner", userId);
		map.put("content", ccc);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, delayMints);

		map.put("action_time", DateUtils.getCurrentDate(c, "yyyyMMddHHmmss"));
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	public static String genSendNotifyMessage(String userId, String[] classid,
			String[] className, String ccc) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("notify", userId, "server");
		Content content = new Content();
		content.setType("send");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("class_id", classid);
		map.put("class_name", className);
		map.put("owner", userId);
		map.put("content", ccc);
		HashMap<String, String> ttt = new HashMap<>();
		ttt.put("attach_url", "http://xdfdfadf");
		ttt.put("attach_type", "image");
		HashMap<String, String> ttt1 = new HashMap<>();
		ttt1.put("attach_url", "http://xdfdfadf");
		ttt1.put("attach_type", "image");
		List<HashMap<String, String>> attaches = new ArrayList<>();
		attaches.add(ttt1);
		attaches.add(ttt);
		map.put("attaches", attaches);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	public static String genSendNotifyMessage(String userId, String[] classid,
			String[] className, String[] targetIds, String[] targetNames,
			String ccc) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("notify", userId, "server");
		Content content = new Content();
		content.setType("send");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("class_id", classid);
		map.put("class_name", className);
		map.put("target_id", targetIds);
		map.put("target_name", targetNames);
		map.put("owner", userId);
		map.put("content", ccc);
		HashMap<String, String> ttt = new HashMap<>();
		ttt.put("attach_url", "http://xdfdfadf");
		ttt.put("attach_type", "image");
		HashMap<String, String> ttt1 = new HashMap<>();
		ttt1.put("attach_url", "http://xdfdfadf");
		ttt1.put("attach_type", "image");
		List<HashMap<String, String>> attaches = new ArrayList<>();
		attaches.add(ttt1);
		attaches.add(ttt);
		map.put("attaches", attaches);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	public static String genSendCommentMessage(String[] classIds,String userId, String notiId,
			String relayTo, String ccc) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("comment", userId, "server");
		Content content = new Content();
		content.setType("save");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("noti_id", notiId);
		map.put("relay", userId);
		map.put("relay_to", relayTo);
		map.put("comment", ccc);
		map.put("class_id", classIds);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	public static String genFindNotification(String userId, String classId) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("notify", userId, "server");
		Content content = new Content();
		content.setType("find");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("class_id", classId);
		map.put("user_id", userId);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}


	
	public static String genRemoveScheduleMessage(String userId,
			String notiId ) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("schedule", userId, "server");
		Content content = new Content();
		content.setType("remove");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userId);
		map.put("noti_id", notiId);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}
	
	public static String genRemoveNotiMessage(String userId,
			String notiId ) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("notify", userId, "server");
		Content content = new Content();
		content.setType("remove");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userId);
		map.put("noti_id", notiId);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	public static String genRemoveComment(String userId, String cid) {
		// String[] m = mobile.split("\\|");
		KiwiMessage message = genBasicMessage("comment", userId, "server");
		Content content = new Content();
		content.setType("remove");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("cid", cid);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

}
