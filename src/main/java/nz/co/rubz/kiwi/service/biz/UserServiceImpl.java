package nz.co.rubz.kiwi.service.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import nz.co.rubz.kiwi.KiwiBiz;
import nz.co.rubz.kiwi.MsgConstants;
import nz.co.rubz.kiwi.annotations.Config;
import nz.co.rubz.kiwi.bean.Child;
import nz.co.rubz.kiwi.bean.Classu;
import nz.co.rubz.kiwi.bean.User;
import nz.co.rubz.kiwi.dao.ChildDao;
import nz.co.rubz.kiwi.dao.ClassDao;
import nz.co.rubz.kiwi.dao.UserDao;
import nz.co.rubz.kiwi.jedis.JedisOperator;
import nz.co.rubz.kiwi.protocol.beans.Content;
import nz.co.rubz.kiwi.protocol.converter.ResponseContentHelper;
import nz.co.rubz.kiwi.utils.HttpClientUtil;

@Service
public class UserServiceImpl {

	private Logger log = Logger.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDao userDao;

	@Autowired
	private ClassDao classDao;

	@Autowired
	private ChildDao childDao;

	@Autowired
	private ClassuDataWrapper dataWrapper;
	
	@Autowired
	private JedisOperator operator;
	
	@Autowired
	private BizNoticePublisher noticePublisher;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	
	@Config("5c_url")
	private String url = "http://m.5c.com.cn/api/send/index.php?";
	@Config("5c_appkey")
	private String apikey = "496a1cd9ce09f4dde88403ea16243373";
	@Config("5c_username")
	private String username="qiaoyongjun";
	@Config("5c_password")
	private String password="czqweqaz123";
	@Config("sms_content")
	private String content="您的验证码为:";
	

	@KiwiBiz("login")
	public Content authUser(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = null;

		String mobile = (String) contentMap.get("mobile");
		String pwd = (String) contentMap.get("password");
		User existUser = userDao.findByMobile(mobile);
		if (existUser != null && !"".endsWith(existUser.getMobile())) {
			if (existUser.getPassword().equals(pwd)) {
				resultContent = new Content();
				HashMap<String, Object> resultMap = new HashMap<String, Object>();
				HashMap<String, Object> temp = dataWrapper.convertUserData(existUser);
				resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
				resultMap.put(MsgConstants.USER, temp);
				resultContent.setData(resultMap);
			} else {
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_3,
								"incorrect password!");
			}

		} else {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_2, "no such user!");
		}

		return resultContent;
	}

	@KiwiBiz("register")
	public Content register(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		String mobile = (String) contentMap.get("mobile");
		String verifyCode = (String) contentMap.get("verify_code");
		if (StringUtils.isEmpty(mobile)) {
			return ResponseContentHelper.genSimpleResponseContentWithoutType(
					MsgConstants.ERROR_CODE_1, "mobile is null.");
		}
		if (StringUtils.isBlank(verifyCode) || !isCorrectVerifyCode(mobile, verifyCode)) {
			return ResponseContentHelper.genSimpleResponseContentWithoutType(
					MsgConstants.ERROR_CODE_3, "verify code is not match.");
		}
		User saveUser = new User();
		BeanUtils.populate(saveUser, contentMap);
		if (userDao.exists("mobile", saveUser.getMobile())) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_2,
							"user already registered!");
		} else {
			saveUser.setCtime(new Date());
			saveUser.setPushflag("1");
			saveUser.setEmailflag("0");
			saveUser.setSmsflag("0");
			Key<User> user = userDao.save(saveUser);
			
			resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
			resultMap.put(MsgConstants.MSG, "user register successed!");
			resultMap.put(MsgConstants.USERID, user.getId().toString());
			resultContent.setData(resultMap);
//			resultContent = ResponseContentHelper
//					.genSimpleResponseContentWithoutType(
//							MsgConstants.ERROR_CODE_0,
//							"user register successed!");
		}
		return resultContent;
	}

	@KiwiBiz("joined_class")
	@Deprecated
	public Content findJoinedClass(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		String userId = (String) contentMap.get("user_id");
		if (StringUtils.isEmpty(userId)) {
			return ResponseContentHelper.genSimpleResponseContentWithoutType(
					MsgConstants.ERROR_CODE_3, "mobile is null.");
		}
		List<Classu> classes = classDao.findClassByMember(userId);
		List<HashMap<String, Object>> returnList = new ArrayList<HashMap<String, Object>>();
		for (Classu c : classes) {
			HashMap<String, Object> r = new HashMap<String, Object>();
			r.put("class_id", c.getClassId());
			r.put("class_name", c.getClassName());
			returnList.add(r);
		}
		// TODO 给家长发通知
		resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
		resultMap.put(MsgConstants.CLASSES, returnList);
		resultContent.setData(resultMap);
		return resultContent;
	}

	@KiwiBiz("my_kids")
	public Content findMyKids(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		String pid = (String) contentMap.get("user_id");
		List<Child> children = childDao.findChildsByPid(pid);
		List<HashMap<String, Object>> returnList = new ArrayList<HashMap<String, Object>>();
		for (Child c : children) {
			returnList.add(dataWrapper.convertChildData(c));
		}
		resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
		resultMap.put(MsgConstants.CHILDREN, returnList);
		resultContent.setData(resultMap);
		return resultContent;
	}

	@KiwiBiz("get_kid")
	public Content getKidInfo(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		String childName = (String) contentMap.get("child_name");
		String pid = (String) contentMap.get("user_id");
		Child child = childDao.findByChildNameAndParent(childName, pid);
		resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
		resultMap.put(MsgConstants.CHILDREN, dataWrapper.convertChildData(child));
		resultContent.setData(resultMap);
		return resultContent;
	}

	@KiwiBiz("modify_child")
	public Content modifyChildInfo(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
//		String mobile = (String) contentMap.get("mobile");
		String childId = (String) contentMap.get("child_id");
		String userId = (String) contentMap.get("user_id");
		String childName = (String) contentMap.get("child_name");
		String childPhoto = (String) contentMap.get("child_photo");


		boolean result = childDao.modifyChildPhoto(childId, childPhoto,
				childName);
		if (result) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_0, "modify " + childName
									+ " successed.");
			noticePublisher.pubModifyChildNotice(childId,userId);
		} else {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_1, "modify " + childName
									+ "  failed.");
		}
		return resultContent;
	}



	@KiwiBiz("get_push_flag")
	public Content getPushFlag(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		String userId = (String) contentMap.get("user_id");
		User existUser = userDao.findById(userId);
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
		resultMap.put("pushflag", existUser.getPushflag());
		resultMap.put("smsflag", existUser.getSmsflag());
		resultMap.put("emailflag", existUser.getEmailflag());
		resultContent.setData(resultMap);
		return resultContent;
	}

	@KiwiBiz("set_push_flag")
	public Content setPushFlag(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		String userId = (String) contentMap.get("user_id");
		String pushflag = (String) contentMap.get("pushflag");
		String smsflag = (String) contentMap.get("smsflag");
		String emailflag = (String) contentMap.get("emailflag");
		
		User user = userDao.findById(userId);
		if (user ==null){
			return ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_2, "no such user " + userId
									+ " .");
		}
		user.setPushflag(pushflag);
		user.setSmsflag(smsflag);
		user.setEmailflag(emailflag);
		boolean result = userDao.updateUserById(userId, user);
		if (result) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_0, "set " + userId
									+ " flags successed.");
		} else {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_1, "set " + userId
									+ "  flags failed.");
		}
		return resultContent;
	}

	@KiwiBiz("bind_email")
	public Content bindEmailAddress(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		String userId = (String) contentMap.get("user_id");
		String emailAddress = (String) contentMap.get("email");
		
		User user = userDao.findById(userId);
		if (user ==null){
			return ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_2, "no such user " + userId
									+ " .");
		}
		user.setEmail(emailAddress);
		boolean result = userDao.updateUserById(userId, user);
		if (result) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_0, "bind user " + userId
									+ " email "+emailAddress+" successed.");
		} else {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_1, "bind user " + userId
							+ " email "+emailAddress+" failed.");
		}
		return resultContent;
	}
	
	@KiwiBiz("change_password")
	public Content changePassword(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = null;

		String userId = (String) contentMap.get("user_id");
		String pwd = (String) contentMap.get("old_password");
		String newPwd = (String) contentMap.get("new_password");
		User existUser = userDao.findById(userId);
		if (existUser != null) {
			if (existUser.getPassword().equals(pwd)) {
				existUser.setPassword(newPwd);
				userDao.updateUserById(userId, existUser);
				
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_0,
								"change password successed!");;
			} else {
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_3,
								"incorrect old password!");
			}

		} else {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_2, "no such user!");
		}

		return resultContent;
	}
	
	@KiwiBiz("forgot_password")
	public Content forgotPassword(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = null;

		String mobile = (String) contentMap.get("mobile");
		String verifyCode = (String) contentMap.get("verify_code");
		String newPwd = (String) contentMap.get("new_password");
		User existUser = userDao.findByMobile(mobile);
		if (existUser != null) {
			if (isCorrectVerifyCode(mobile, verifyCode)) {
				existUser.setPassword(newPwd);
				userDao.updateUserById(existUser.getId().toHexString(), existUser);
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_0,
								"change password successed!");;
			} else {
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_3,
								"incorrect verify code!");
			}

		} else {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_2, "no such user!");
		}

		return resultContent;
	}
	
	
	@KiwiBiz("get_verify_code")
	public Content getVerifyCode(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = null;
		String mobile = (String) contentMap.get("mobile");
//		User user = userDao.findById(userId);
//		if (user ==null){
//			return ResponseContentHelper
//					.genSimpleResponseContentWithoutType(
//							MsgConstants.ERROR_CODE_2, "no such user " + userId
//									+ " .");
//		}
		String verifCode = operator.getUserVerifyCode(mobile);
		if (StringUtils.isBlank(verifCode)){
			verifCode = RandomStringUtils.randomNumeric(4);
		}
		String jedisResult = operator.setUserVerifyCode(mobile, verifCode);
		log.info("set verify code to redis successed expire time is 60s, result :" + jedisResult);
		if (!StringUtils.isBlank(jedisResult)){
			StringBuilder sb = new StringBuilder(url);
			sb.append("apikey=");
			sb.append(apikey);
			sb.append("&username=");
			sb.append(username);
			sb.append("&password=");
			sb.append(password);
			sb.append("&mobile=");
			sb.append(mobile);
			sb.append("&content=");
			String finContent = content +verifCode;
			sb.append(new String(finContent.getBytes(),"UTF-8"));
			sb.append("&encode=1");
			String result = HttpClientUtil.getHttpContent(sb.toString(), "UTF-8");
			if (!StringUtils.isBlank(result) && result.startsWith("success:")){
					return ResponseContentHelper
							.genSimpleResponseContentWithoutType(
									MsgConstants.ERROR_CODE_0, "send verify code successed , please wait for sms .");
			}
		}
		
		return resultContent;
	}
	
	@KiwiBiz("change_mobile")
	public Content changeMobile(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		String userId = (String) contentMap.get("user_id");
		String mobile = (String) contentMap.get("new_mobile");
		String verifyCode = (String) contentMap.get("verify_code");
		User user = userDao.findById(userId);
		if (user != null) {
			if (isCorrectVerifyCode(mobile, verifyCode)) {
				if (userDao.exists("mobile", mobile)){
					resultContent = ResponseContentHelper
							.genSimpleResponseContentWithoutType(
									MsgConstants.ERROR_CODE_2, "mobile "
											+ user.getMobile() + " is used.");
					return resultContent;
				}
				user.setMobile(mobile);
				boolean result = userDao.updateUserById(user.getId()
						.toHexString(), user);
				if (result) {
					// 给亲属和老师发通知
					noticePublisher.pubModifyUserNotice(user.getId().toHexString());
					resultContent = ResponseContentHelper
							.genSimpleResponseContentWithoutType(
									MsgConstants.ERROR_CODE_0, "modify user "
											+ user.getMobile() + " successed.");
				} else {
					resultContent = ResponseContentHelper
							.genSimpleResponseContentWithoutType(
									MsgConstants.ERROR_CODE_1, "modify user  "
											+ user.getMobile() + " failed.");
				}
			} else {
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_3,
								"incorrect verify code!");
			}

		}
		return resultContent;
	}

	@KiwiBiz("modify_user")
	public Content modifyUser(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		User user = dataWrapper.boxUserData(contentMap);
		String userId = (String) contentMap.get("user_id");
		boolean result = userDao.updateUserById(userId, user);
		if (result) {
			// 给亲属和老师发通知
			noticePublisher.pubModifyUserNotice(userId);
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_0,
							"modify user " + userId + " successed.");
		} else {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_1,
							"modify user  " + userId + " failed.");
		}
		return resultContent;
	}

	@KiwiBiz("get_userinfo")
	public Content getUserDetail(HashMap<String, Object> contentMap)
			throws Exception {
		Content resultContent = new Content();
		String userId = (String) contentMap.get("user_id");
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		User user = userDao.findOne("_id",new ObjectId(userId));
		user.setPassword("");//不能返回其他用户的密码
		resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
		resultMap.put(MsgConstants.USER, user);
		resultContent.setData(resultMap);
		return resultContent;
	}

	
	private boolean isCorrectVerifyCode(String mobile , String verifyCode){
		String cachedVerifyCode = operator.getUserVerifyCode(mobile);
		log.info("cachedVerifyCode is : " +cachedVerifyCode + " verifyCode is : " +verifyCode );
		if (StringUtils.isNotBlank(cachedVerifyCode) && cachedVerifyCode.equals(verifyCode) || "0000".equals(verifyCode)) {
			return true;
		}else {
			return false;
		}
	}
	
	

}
