package nz.co.rubz.kiwi.test;

import java.net.URI;
import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import nz.co.rubz.kiwi.websocket.ClassuClient;
import nz.co.rubz.kiwi.websocket.TestMessageGenerator;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceTest {

	public static HashMap<String, ClassuClient> loginedClient = new HashMap<String, ClassuClient>();
	
	// 1390000052 , 1390000053
	public static String[] userIds = new String[] { "5539fbf7fd0ae815b09de7b5", "5583f230fd0ae81d14ee606a" };
	public static URI uri = null; 

	@BeforeClass
	public static void setUpClass() {
		try {
			uri = new URI("ws://localhost:10009/websocket");
//			URI uri = new URI("ws://121.42.141.134:10009/websocket");
			for (String userId : userIds) {
				ClassuClient c = new ClassuClient(uri);
				c.init();
//				c.sendMsg(TestMessageGenerator.genLoginMessage(mobile)); // 自动登录
//				c.sendMsg(TestMessageGenerator.genGetUndelieverMessage(mobile,"")); // 自动获取离线消息
				loginedClient.put(userId, c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		Thread.sleep(5000);
		for (String mobile : userIds) {
			ClassuClient c = loginedClient.get(mobile);
			c.closeChannel();
		}
		loginedClient = null;
	}

	public ClassuClient getLoginedClient(String mobile) {
		return loginedClient.get(mobile);
	}

	@Test
	public void testBRegister() throws Exception {
		String mobile0 = "13910766840";
		if (uri!=null){
			ClassuClient c = new ClassuClient(uri);
			c.init();
			c.sendMsg(TestMessageGenerator.genRegMessage(mobile0, "吕齐"));
		}
	}

	@Test
	public void testCFindJoinedClass() {
		String mobile = userIds[0];
		ClassuClient c = getLoginedClient(mobile);
		c.sendMsg(TestMessageGenerator.genJoinedClassesMessage(mobile));
	}

	@Test
	public void testDFindMyKids() {
		String mobile = userIds[0];
		ClassuClient c = getLoginedClient(mobile);
		c.sendMsg(TestMessageGenerator.genFindChlidMessage(mobile));
	}

//	@Test
//	public void testEGetKidInfo() {
//		String mobile = mobiles[0];
//		ClassuClient c = getLoginedClient(mobile);
//		c.sendMsg(TestMessageGenerator.genFindChlidByNameMessage(mobile, "王天天"));
//	}

	@Test
	public void testFModifyChildInfo() {
		String mobile = userIds[0];
		ClassuClient c = getLoginedClient(mobile);
		c.sendMsg(TestMessageGenerator.genModifyChlidMessage(mobile,"55b729b2321c6c29d6c54009", "小吕",
				"http://baidu.com/icon.png"));
	}
	@Test
	public void testFNGetKidInfo() {
		String mobile = userIds[0];
		ClassuClient c = getLoginedClient(mobile);
		c.sendMsg(TestMessageGenerator.genFindChlidByNameMessage(mobile, "王天天"));
	}
	@Test
	public void testGetPushFlag() {
		String mobile = userIds[1];
		ClassuClient c = getLoginedClient(mobile);
		c.sendMsg(TestMessageGenerator.genGetPushflagMessage(mobile));
	}

	@Test
	public void testHSetPushFlag() {
		String mobile = userIds[0];
		ClassuClient c = getLoginedClient(mobile);
		c.sendMsg(TestMessageGenerator.genSetPushflagMessage(mobile, "1"));
	}

	@Test
	public void testIChangeMobile() {
		String mobile = userIds[1];
		ClassuClient c = getLoginedClient(mobile);
		c.sendMsg(TestMessageGenerator.genChangeMobileMessage(mobile, "13910777777"));
	}

	@Test
	public void testJModifyUser() {
		String mobile = userIds[1];
		ClassuClient c = getLoginedClient(mobile);
		c.sendMsg(TestMessageGenerator.genModifyUserMessage(mobile, "吕齐111"));
	}

	@Test
	public void testKGetUserDetail() {
		String mobile = userIds[0];
		ClassuClient c = getLoginedClient(mobile);
		c.sendMsg(TestMessageGenerator.genGetUserDetailMessage(mobile,
				"5583f230fd0ae81d14ee606a"));
	}
	
	@Test
	public void testLBindEmail() {
		String mobile = userIds[0];
		ClassuClient c = getLoginedClient(mobile);
		c.sendMsg(TestMessageGenerator.genBindEmailMessage(mobile, "2@2.com"));
	}
	
	@Test
	public void testMGetVerifyCode() {
		String mobile = userIds[1];
		ClassuClient c = getLoginedClient(mobile);
		c.sendMsg(TestMessageGenerator.genGetVerifyCodeMessage(mobile,"13910766840"));
	}
	
	@Test
	public void testNChangePassword() {
		String mobile = userIds[1];
		ClassuClient c = getLoginedClient(mobile);
		c.sendMsg(TestMessageGenerator.genChangePasswordMessage(mobile, "1234", "111111"));
	}
	
	@Test
	public void testOResetPassword() {
		String mobile = userIds[1];
		ClassuClient c = getLoginedClient(mobile);
		c.sendMsg(TestMessageGenerator.genResetPasswordMessage(mobile, "13910766840","4863", "222222"));
	}
	
	

}
