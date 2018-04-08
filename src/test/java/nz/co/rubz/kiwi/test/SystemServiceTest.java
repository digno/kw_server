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
public class SystemServiceTest {

	public static HashMap<String, ClassuClient> loginedClient = new HashMap<String, ClassuClient>();

	public static String[] mobiles = new String[] { "5539fbf7fd0ae815b09de7b5" };
	

	@BeforeClass
	public static void setUpClass() {
		try {
			URI uri = new URI("ws://localhost:10009/websocket");
//			URI uri = new URI("ws://121.42.141.134:10009/websocket");
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

	public ClassuClient getLoginedClient(String mobile) {

		return loginedClient.get(mobile);
	}

	@Test
	public void testAInit() {
		String mobile0 = mobiles[0];
		ClassuClient c = getLoginedClient(mobile0);
		c.sendMsg(TestMessageGenerator.genInitMessage(mobile0));
	}

	

}
