package nz.co.rubz.kiwi.test;

import java.net.URI;
import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import nz.co.rubz.kiwi.websocket.ClassuClient;
import nz.co.rubz.kiwi.websocket.TestMessageGenerator;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NotifiServiceTest {
	public static HashMap<String, ClassuClient> loginedClient = new HashMap<String, ClassuClient>();

	public static String[] mobiles = new String[] { "55307c8ee7a053b6702be9bb","5539fbf7fd0ae815b09de7b5","5583f230fd0ae81d14ee606a" };

	@BeforeClass
	public static void setUpClass() {
		try {
			 URI uri = new URI("ws://localhost:10009/websocket");
//			URI uri = new URI("ws://121.42.141.134:10009/websocket");
//			URI uri = new URI("ws://115.28.242.48:10009/websocket");
			for (String mobile : mobiles) {
				ClassuClient c = new ClassuClient(uri);
				c.init();
//				c.sendMsg(TestMessageGenerator.genLoginMessage(mobile)); // 自动登录
//				c.sendMsg(TestMessageGenerator.genGetUndelieverMessage(mobile,"")); // 自动获取离线消息
				loginedClient.put(mobile, c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		Thread.sleep(5000);
		for (String mobile : mobiles) {
			ClassuClient c = loginedClient.get(mobile);
			c.closeChannel();
		}
		loginedClient = null;
	}

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
	}

	public ClassuClient getLoginedClient(String mobile) {

		return loginedClient.get(mobile);
	}

	@Test 
	public void testASendNotify() {
		String mobile0 = mobiles[0];
		ClassuClient c = getLoginedClient(mobile0);
		c.sendMsg(TestMessageGenerator.genSendNotifyMessage(mobile0,
				new String[]{"000000","000001"},new String[]{"一班","二班"} , "新通知内容")); //608786,000001,000000,
		c.sendPing();
	}
	
	@Test
	public void testASendNotifyWithTargets() {
		String mobile0 = mobiles[0];
		ClassuClient c = getLoginedClient(mobile0);
		c.sendMsg(TestMessageGenerator.genSendNotifyMessage(mobile0,new String[]{"000001"},new String[]{"一班"},
				new String[]{"5539fbf7fd0ae815b09de7b5","5583f230fd0ae81d14ee606a"},new String[]{"张三","李四"} , "新通知内容")); //608786,000001,000000,
		c.sendPing();
	}

	@Test
	public void testBSendComment() {
		String mobile0 =  mobiles[1];
		ClassuClient c = getLoginedClient(mobile0);
		c.sendMsg(TestMessageGenerator.genSendCommentMessage(new String[]{"000001"},mobile0, "55b74b03a6941f1850b2e182",   "", "我是家长，我发表了回复"));
		c.sendPing();
	}
	
	@Test
	public void testCSendComment() {
		String mobile0 =  mobiles[0];
		ClassuClient c = getLoginedClient(mobile0);
		c.sendMsg(TestMessageGenerator.genSendCommentMessage(new String[]{"000001"},mobile0, "55b74b03a6941f1850b2e182",  "5539fbf7fd0ae815b09de7b5", "我是老师，我回复了家长"));
		c.sendPing();
	}
	
//	@Test
//	public void testDSendComment() {
//		String mobile0 =  mobiles[2];
//		ClassuClient c = getLoginedClient(mobile0);
//		c.sendMsg(TestMessageGenerator.genSendCommentMessage(mobile0, "558d0008fd0ae813586ad2d9",  "1390000051", "我是家长1390000053，我回复了老师"));
//		c.sendPing();
//	}
//	
//	@Test
//	public void testESendComment() {
//		String mobile0 =  mobiles[1];
//		ClassuClient c = getLoginedClient(mobile0);
//		c.sendMsg(TestMessageGenerator.genSendCommentMessage(mobile0, "558d0008fd0ae813586ad2d9",  "1390000051", "我是家长1390000052，我回复了老师"));
//		c.sendPing();
//	}

	@Test
	public void testFFindNotification() {
		String mobile0 =  mobiles[0];
		ClassuClient c = getLoginedClient(mobile0);
		// 老师应该看到所有的回复信息
		c.sendMsg(TestMessageGenerator.genFindNotification(mobile0,
				"000000")); //3 回复
		c.sendPing();
	}
	
	@Test
	public void testGFindNotification() {
		String mobile0 =  mobiles[1];
		ClassuClient c = getLoginedClient(mobile0);
		// 老师应该看到所有的回复信息
		c.sendMsg(TestMessageGenerator.genFindNotification(mobile0,
				"000000")); //2
		c.sendPing();
	}

	
	@Test
	public void testIFindNotification() {
		String mobile0 =  mobiles[2];
		ClassuClient c = getLoginedClient(mobile0);
		// 家长只能看到跟自己相关的信息
		c.sendMsg(TestMessageGenerator.genFindNotification(mobile0,
				"000000")); //1
		c.sendPing();
	}
	
	@Test
	public void testJFindNotification() {
		String mobile0 =  mobiles[1];
		ClassuClient c = getLoginedClient(mobile0);
		// 家长只能看到跟自己相关的信息
		c.sendMsg(TestMessageGenerator.genFindNotification(mobile0,
				"000001")); //1
		c.sendPing();
	}
	
//	@Test
	public void testKDeleteComment() {
		String mobile0 =  mobiles[0];
		ClassuClient c = getLoginedClient(mobile0);
		// 家长只能看到跟自己相关的信息
		c.sendMsg(TestMessageGenerator.genRemoveComment(mobile0,
				"5582965311348a2c5c69810c")); 
		c.sendPing();
	}
	

	
	@Test
	public void testMSaveSchedule() {
		String mobile0 =  mobiles[0];
		ClassuClient c = getLoginedClient(mobile0);
		c.sendMsg(TestMessageGenerator.genSaveScheduleMessage(mobile0,
				new String[]{"000000","000001"},"这是一个日程",1));
		c.sendPing();
	}
	
	@Test
	public void testNConfirm() {
		String mobile0 =  mobiles[1];
		ClassuClient c = getLoginedClient(mobile0);
		c.sendMsg(TestMessageGenerator.genConfirmedMessage(mobile0, "55a8da95fd0ae82af4fd8d95","1"));
		c.sendPing();
	}
	
	@Test
	public void testOGetViews() {
		String mobile0 =  mobiles[1];
		ClassuClient c = getLoginedClient(mobile0);
		c.sendMsg(TestMessageGenerator.genGetViewsMessage(mobile0, "55a8da95fd0ae82af4fd8d95"));
		c.sendPing();
	}
	@Test
	public void testPRemoveNoti() {
		String mobile0 =  mobiles[0];
		ClassuClient c = getLoginedClient(mobile0);
		c.sendMsg(TestMessageGenerator.genRemoveNotiMessage(mobile0, "55b09364fd0ae82100838a20"));
		c.sendPing();
	}
	
	@Test
	public void testQRemoveSchedule() {
		String mobile0 =  mobiles[0];
		ClassuClient c = getLoginedClient(mobile0);
		c.sendMsg(TestMessageGenerator.genRemoveScheduleMessage(mobile0, "55a8de75fd0ae82a4082df83"));
		c.sendPing();
	}
	
//	@Test
//	public void testRGetViews() {
//		String mobile0 =  mobiles[1];
//		ClassuClient c = getLoginedClient(mobile0);
//		c.sendMsg(TestMessageGenerator.genGetViewsMessage(mobile0, "55a8da95fd0ae82af4fd8d95"));
//		c.sendPing();
//	}

}
