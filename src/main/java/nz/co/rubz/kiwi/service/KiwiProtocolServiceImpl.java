package nz.co.rubz.kiwi.service;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import nz.co.rubz.kiwi.ConcurrentContext;
import nz.co.rubz.kiwi.KiwiServiceKeyEnum;
import nz.co.rubz.kiwi.MsgConstants;
import nz.co.rubz.kiwi.ServerConstants;
import nz.co.rubz.kiwi.annotations.Config;
import nz.co.rubz.kiwi.protocol.beans.Content;
import nz.co.rubz.kiwi.protocol.beans.KiwiMessage;
import nz.co.rubz.kiwi.protocol.converter.ResponseContentHelper;
import nz.co.rubz.kiwi.server.KiwiChannel;
import nz.co.rubz.kiwi.service.biz.CartServiceImpl;
import nz.co.rubz.kiwi.service.biz.ClientServiceImpl;
import nz.co.rubz.kiwi.service.biz.CommentServiceImpl;
import nz.co.rubz.kiwi.service.biz.DeliveryServiceImpl;
import nz.co.rubz.kiwi.service.biz.FoodServiceImpl;
import nz.co.rubz.kiwi.service.biz.KiwiUserServiceImpl;
import nz.co.rubz.kiwi.service.biz.MenuServiceImpl;
import nz.co.rubz.kiwi.service.biz.MessageServiceImpl;
import nz.co.rubz.kiwi.service.biz.NotificationServiceImpl;
import nz.co.rubz.kiwi.service.biz.OrderServiceImpl;
import nz.co.rubz.kiwi.service.biz.PaymentServiceImpl;
import nz.co.rubz.kiwi.service.biz.RestaurantServiceImpl;
import nz.co.rubz.kiwi.service.biz.SystemServiceImpl;

@Service
public class KiwiProtocolServiceImpl implements IProtocolService {

	private static final Logger log = Logger
			.getLogger(KiwiProtocolServiceImpl.class);

	private ConcurrentHashMap<String, KiwiChannel> channelsMap = ConcurrentContext
			.getChannelMapInstance();

	private ConcurrentHashMap<Integer, KiwiChannel> cMap = ConcurrentContext
			.getAvailableChannelMapInstance();

	@Autowired
	private IBizService bizService;

	@Autowired
	private KiwiUserServiceImpl kiwiUserService;
	
	@Autowired
	private RestaurantServiceImpl restaurantService;
	
	@Autowired
	private OrderServiceImpl orderService;
	
	@Autowired
	private FoodServiceImpl foodService;
	
	@Autowired
	private MenuServiceImpl menuService;
	
	@Autowired
	private CartServiceImpl cartService;
	
	@Autowired
	private DeliveryServiceImpl deliveryService;
	
	@Autowired
	private PaymentServiceImpl payService;
	
	@Autowired
	private ClientServiceImpl clientService;
	
	@Autowired
	private SystemServiceImpl sysService;

	@Autowired
	private MessageServiceImpl messageService;
	
	@Autowired
	private NotificationServiceImpl notifyService;
	
	@Autowired
	private CommentServiceImpl commentService;
	
	
	
	// 假定，IO耗时占比50% 。理论将线程数设置为 cup个数*2 就可以将cpu利用率提至最高
	@Config("max_sync_req_threads")
	private int maxWorkers = Runtime.getRuntime().availableProcessors()*2;
	private ExecutorService es ;
	
	/**
	 * 所有的业务逻辑都在NETTY 的IO 线程上执行，在用户量小的情况下是OK 的。
	 * 但如果要支持高并发和多链接，则需要在此处实现多线程，用于异步处理（同步的）请求
	 */
	
	@Override
	public Content dispatch(KiwiMessage message) {
		Content result = null;
		try {
			Future<Content> c = getExecutorService().submit(new Callable<Content>(){
				@Override
				public Content call() throws Exception {
					log.info("dispatch sync request with Async way.");
					return dispatchWithThreads(message);
				}
			});
			result = c.get();
		} catch (Exception e) {
			result = ResponseContentHelper.genSimpleResponseContentWithoutType(
					MsgConstants.SERVER_INNER_ERROR_CODE, MsgConstants.SERVER_INNER_ERROR_MSG);
			result.setType(MsgConstants.SERVER);
			return result;
		}
		return result;
	}
	
	
	public Content dispatchWithThreads(KiwiMessage message) {
		Content result = null;
		String subject = message.getSubject();
		if (StringUtils.isBlank(subject)) {
			result = ResponseContentHelper.genSimpleResponseContentWithoutType(
					MsgConstants.ERROR_CODE_2, "unsupport subject");
			result.setType(MsgConstants.SERVER);
			return result;

		}
		KiwiServiceKeyEnum mk = KiwiServiceKeyEnum.getEnum(subject.toUpperCase());
		Content requestContent = message.getContent();
		switch (mk) {
		case USER:
			result = bizService.process(requestContent, kiwiUserService);
			break;
		case RESTAURANT:
			result = bizService.process(requestContent, restaurantService);
			break;
		case FOOD:
			result = bizService.process(requestContent, foodService);
			break;
		case ORDER:
			result = bizService.process(requestContent, orderService);
			break;
		case CART:
			result = bizService.process(requestContent, cartService);
			break;
		case DELIVERY:
			result = bizService.process(requestContent, deliveryService);
			break;
		case PAYMENT:
			result = bizService.process(requestContent, payService);
			break;
		case NOTIFY:
			result = bizService.process(requestContent, notifyService);
			break;
		case CLIENT:
			result = bizService.process(requestContent, clientService);
			break;
		case COMMENT:
			result = bizService.process(requestContent, commentService);
			break;
		case SYSTEM:
			if (requestContent.getType() == null) {
				requestContent.setType(MsgConstants.TIDE);
			}
			result = bizService.process(requestContent, sysService);
			break;
		default:
			result = ResponseContentHelper.genSimpleResponseContentWithoutType(
					MsgConstants.ERROR_CODE_2, "unsupport subject");
			result.setType(MsgConstants.SERVER);
		}
		return result;

	}

	@Override
	public boolean boardcast(Integer channelId, String from, String to,
			String request) {
		if (ServerConstants.TO_ALL.equalsIgnoreCase(to)) {
			// for (String email : channelsMap.keySet()) {
			// if (from.equalsIgnoreCase(email)) {
			// continue;
			// }
			// RiderChannel channel = channelsMap.get(email);
			// writeToChannel(channel, request);
			// }
			for (Integer id : cMap.keySet()) {

				if (channelId == id) {
					continue;
				}
				KiwiChannel channel = cMap.get(id);
				writeToChannel(channel, request);
			}
		} else {
			KiwiChannel channel = channelsMap.get(to);
			writeToChannel(channel, request);
		}
		return true;
	}

 

	@Override
	public boolean route(String from, String to, String message) {

		KiwiChannel channel = channelsMap.get(to);
		if (channel != null) {
			writeToChannel(channel, message);
		} else {
			messageService.saveUndeliverMessage(to, message);
		}

		return true;
	}

	public boolean routeOld(String from, String to, String message) {
		if (ServerConstants.TO_ALL.equalsIgnoreCase(to)) {
			for (String mobile : channelsMap.keySet()) {
				if (from.equalsIgnoreCase(mobile)) {
					continue;
				}
				KiwiChannel channel = channelsMap.get(mobile);
				writeToChannel(channel, message);
			}
		} else {
			KiwiChannel channel = channelsMap.get(to);
			writeToChannel(channel, message);
		}
		return true;
	}

	// 如果客户端响应了ping 请求则服务器会同步数据给客户端
	// 就如同潮水一样，一波一波的。^_^

	private void writeToChannel(KiwiChannel channel, String request) {
		try {
			channel.getChannel().write(new TextWebSocketFrame(request));
			channel.getChannel().flush();
			// channel.getChannel().writeAndFlush(new
			// TextWebSocketFrame(request), promise)
			// if (channel.isLogined()) {
			// channel.getChannel().write(new TextWebSocketFrame(request));
			// } else {
			// log.info("BAD REQUEST. (not logined)");
			// }
		} catch (Exception e) {
			log.error(e);
		}
	}

	// 业务线程池 单例
	private ExecutorService getExecutorService(){
		if (es == null){
			creatExecutorService(maxWorkers);
		}
		return es;
	}
	
	private  synchronized void creatExecutorService(int maxWorkers){
		if (es == null){
			es = Executors.newFixedThreadPool(maxWorkers);
		}
	}
}
