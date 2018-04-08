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
public class ClassServiceTest {

	public static HashMap<String, ClassuClient> loginedClient = new HashMap<String, ClassuClient>();

	public static String[] mobiles = new String[] {
			"55307c8ee7a053b6702be9bb",
			"5539fbf7fd0ae815b09de7b5",
			"5583f230fd0ae81d14ee606a",
			"55b723c5a6941f1bb0ff9a3e"};

	@BeforeClass
	public static void setUpClass() {
		try {
			URI uri = new URI("ws://localhost:10009/websocket");
//			URI uri = new URI("ws://121.42.141.134:10009/websocket");
			for (String mobile : mobiles){
				ClassuClient c = new ClassuClient(uri);
				c.init();
//				c.sendMsg(TestMessageGenerator.genLoginMessage(mobile)); // 自动登录
//				c.sendMsg(TestMessageGenerator.genGetUndelieverMessage(mobile, "")); // 自动获取离线消息
				loginedClient.put(mobile, c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		Thread.sleep(5000);
		for (String  mobile : mobiles){
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
	public void testACreateClass() {
		String mobile0 = mobiles[0];
		ClassuClient c = getLoginedClient(mobile0);
		c.sendMsg(TestMessageGenerator.genCreateClassuMessage(mobile0,
				"000000", "三年级1班", "吕齐"));
		c.sendMsg(TestMessageGenerator.genCreateClassuMessage(mobile0,
				"000001", "三年级2班", "吕齐"));
		c.sendMsg(TestMessageGenerator.genCreateClassuMessage(mobile0,
				"000002", "三年级3班", "吕齐"));
		c.sendPing();
	}

	@Test
	public void testBFindClass() {
		String mobile0 =  mobiles[0];
		ClassuClient c = getLoginedClient(mobile0);
		c.sendMsg(TestMessageGenerator.genFindClassMessage(mobile0, "000000",
				"0"));
		c.sendMsg(TestMessageGenerator.genFindClassMessage(mobile0, "000000",
				"1"));
	}

	@Test
	public void testCJoinClass() {
		String mobile1 =  mobiles[1];
		ClassuClient c = getLoginedClient(mobile1);
		c.sendMsg(TestMessageGenerator.genJoinClassMessage(mobile1, "000000",
				"吕一", "0"));
		String mobile2 =  mobiles[2];
		ClassuClient c1 = getLoginedClient(mobile2);
		c1.sendMsg(TestMessageGenerator.genJoinClassMessage(mobile2, "000001",
				"张雨萱", "0"));
		String mobile3 =  mobiles[2];
		ClassuClient c2 = getLoginedClient(mobile3);
		c2.sendMsg(TestMessageGenerator.genJoinClassMessage(mobile3, "000002",
				"葛小宇", "1"));
		
		c.sendMsg(TestMessageGenerator.genJoinedClassesMessage(mobile1));
	}

	@Test
	public void testDModifyClass() {
		String mobile0 =  mobiles[0];
		ClassuClient c = getLoginedClient(mobile0);
		c.sendMsg(TestMessageGenerator.genModifyClassMessage(mobile0, "000000",
				"三年级4班"));
	}

	@Test
	public void testEGetClassChildren() {
		String mobile0 =  mobiles[0];
		ClassuClient c = getLoginedClient(mobile0);
		c.sendMsg(TestMessageGenerator.genGetChildrenMessage(mobile0, "000000"));
	}

	@Test
	public void testFRemoveClassMembers() {
		String mobile0 =  mobiles[0];
		ClassuClient c = getLoginedClient(mobile0);
		String mobile1 =  mobiles[1];
		c.sendMsg(TestMessageGenerator.genRemoveClassMembersMessage(mobile0,
				"000001", mobile1));
	}

	@Test
	public void testGExitClass() {
		String mobile0 =  mobiles[2];
		ClassuClient c = getLoginedClient(mobile0);
		c.sendMsg(TestMessageGenerator.genExitClassMessage(mobile0, "000002"));
	}

	@Test
	public void testHGetClassInfo() {
		String mobile0 =  mobiles[1];
		ClassuClient c = getLoginedClient(mobile0);
		c.sendMsg(TestMessageGenerator.genGetClassMessage(mobile0, "000000",
				"1"));
	}

	@Test
	public void testIGetMyClassInfo() {
		String mobile0 =  mobiles[0];
		ClassuClient c = getLoginedClient(mobile0);
		c.sendMsg(TestMessageGenerator.genGetClassMessage(mobile0, "000000",
				"0"));
	}

	@Test
	public void testJGetClassMembers() {
		String mobile0 =  mobiles[0];
		ClassuClient c = getLoginedClient(mobile0);
		c.sendMsg(TestMessageGenerator.genGetClassMessage(mobile0, "000000",
				"0"));
	}

//	@Test
	public void testKRemoveClass() {
		String mobile0 =  mobiles[3];
		ClassuClient c = getLoginedClient(mobile0);
		c.sendMsg(TestMessageGenerator.genRemoveClassMessage(mobile0, "474617"));

	}
}
