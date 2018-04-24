package nz.co.rubz.kiwi.service.biz;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.locale.converters.DateLocaleConverter;
import org.apache.log4j.Logger;
import org.mongodb.morphia.Key;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nz.co.rubz.kiwi.KiwiBiz;
import nz.co.rubz.kiwi.MsgConstants;
import nz.co.rubz.kiwi.dao.KiwiOrderDao;
import nz.co.rubz.kiwi.model.Order;
import nz.co.rubz.kiwi.model.enums.OrderStatusEnum;
import nz.co.rubz.kiwi.protocol.beans.Content;
import nz.co.rubz.kiwi.protocol.converter.ResponseContentHelper;

@Service
public class OrderServiceImpl {

	private Logger log = Logger.getLogger(OrderServiceImpl.class);

	@Autowired
	private KiwiDataWrapper dataWrapper;

	@Autowired
	private KiwiOrderDao orderDao;

	/**
	 * 生成订单 https://openapi-doc.faas.ele.me/v2/api/order.html#order-create
	 * 
	 * @param contentMap
	 *            order.java
	 * @return
	 * @throws Exception
	 */
	@KiwiBiz("create_order")
	public Content createOrder(HashMap<String, Object> cMap) throws Exception {
		Content resultContent = new Content();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Order saveOrder = new Order();
		BeanUtils.populate(saveOrder, cMap);
		// TODO 验证必填字段

		// 保存订单
		Key<Order> save = orderDao.save(saveOrder);

		resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
		resultMap.put(MsgConstants.MSG, "create order success!");
		resultMap.put(MsgConstants.ORDERID, save.getId());

		if (cMap.get("show_detail") != null && cMap.get("show_detail") == "1") {
			// 返回订单详情
			Order o = orderDao.findById(save.getId().toString());
			resultMap.put(MsgConstants.ORDER, o);
		}
		resultContent.setData(resultMap);
		return resultContent;
	}

	/**
	 * 订单查询 https://openapi-doc.faas.ele.me/v2/api/order.html#order-get
	 * 
	 * @param contentMap
	 *            by order_id
	 * @return
	 */
	@KiwiBiz("get_order")
	public Content getOrder(HashMap<String, Object> contentMap) {
		Content resultContent = new Content();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		Object orderId = contentMap.get("order_id");
		log.info("getOrder by orderId:" + orderId);
		if (orderId == null) {
			return ResponseContentHelper.genSimpleResponseContentWithoutType(MsgConstants.ERROR_CODE_1,
					"order_id cannot be empty!");
		}
		Order order = orderDao.findById(orderId.toString());
		resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
		resultMap.put(MsgConstants.MSG, "find order success!");
		resultMap.put(MsgConstants.ORDERID, orderId);
		resultMap.put(MsgConstants.ORDER, order);
		resultContent.setData(resultMap);
		return resultContent;
	}

	/**
	 * 批量获取订单 https://openapi-doc.faas.ele.me/v2/api/order.html#order-mget
	 * 
	 * @param contentMap
	 *            by order_ids
	 * @return
	 */
	@KiwiBiz("get_orders")
	public Content mgetOrders(HashMap<String, Object> contentMap) {
		Content resultContent = new Content();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		Object orderIds = contentMap.get("order_id");
		log.info("mgetOrders by orderIds:" + orderIds);
		if (orderIds == null) {
			return ResponseContentHelper.genSimpleResponseContentWithoutType(MsgConstants.ERROR_CODE_1,
					"order_id cannot be empty!");
		}
		List<Order> orders = orderDao.findByIds(orderIds.toString());
		resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
		resultMap.put(MsgConstants.MSG, "find orders success!");
		resultMap.put(MsgConstants.ORDERID, orderIds);
		resultMap.put(MsgConstants.ORDER, orders);
		resultContent.setData(resultMap);
		return resultContent;
	}

	/**
	 * 获取订单状态
	 * 
	 * https://openapi-doc.faas.ele.me/v2/api/order.html#order-status-get
	 * nz.co.rubz.kiwi.model.enums.OrderStatusEnum
	 * 
	 * @param contentMap
	 *            by order_id
	 * @return
	 */
	@KiwiBiz("get_status")
	public Content getOrderStatus(HashMap<String, Object> contentMap) {
		Content resultContent = new Content();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		Object orderId = contentMap.get("order_id");
		log.info("getOrderStatus by orderId:" + orderId);
		if (orderId == null) {
			return ResponseContentHelper.genSimpleResponseContentWithoutType(MsgConstants.ERROR_CODE_1,
					"order_id cannot be empty!");
		}
		Order order = orderDao.findById(orderId.toString());
		resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
		resultMap.put(MsgConstants.MSG, order != null ? "find order status success!" : "find order status failure!");
		resultMap.put(MsgConstants.ORDERID, orderId);
		resultMap.put(MsgConstants.ORDERSTATUS, order != null ? order.getStatusCode() : null);
		resultContent.setData(resultMap);
		return resultContent;
	}

	/**
	 * 用户确认订单（11）
	 * 
	 * @param contentMap
	 *            by order_id
	 * @return
	 */
	@KiwiBiz("confirm_order")
	public Content confirmOrder(HashMap<String, Object> contentMap) {
		Content resultContent = new Content();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		Object orderId = contentMap.get("order_id");
		log.info("confirmOrder by orderId:" + orderId);
		if (orderId == null) {
			return ResponseContentHelper.genSimpleResponseContentWithoutType(MsgConstants.ERROR_CODE_1,
					"order_id cannot be empty!");
		}
		boolean b = orderDao.updataStatus(orderId.toString(), OrderStatusEnum.STATUS_CODE_USER_CONFIRMED.getValue());
		resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
		resultMap.put(MsgConstants.MSG, b == true ? "confirmOrder success!" : "confirmOrder failure!");
		resultMap.put(MsgConstants.ORDERID, orderId);
		resultMap.put(MsgConstants.ORDERSTATUS,
				b == true ? OrderStatusEnum.STATUS_CODE_USER_CONFIRMED.getValue() : null);

		resultContent.setData(resultMap);
		return resultContent;
	}

	/**
	 * 商家取消订单（-1）
	 * 
	 * @param contentMap
	 *            by order_id
	 * @return
	 */
	@KiwiBiz("cancel_order")
	public Content cancelOrder(HashMap<String, Object> contentMap) {
		return new Content();
	}

	/**
	 * 通过订单id查询联系方式
	 * https://openapi-doc.faas.ele.me/v2/api/order.html#eleme-order-idorder-contact-info-get
	 * 
	 * @param contentMap
	 *            by order_id
	 * @return
	 */
	@KiwiBiz("get_contactInfo")
	public Content getContactInfoById(HashMap<String, Object> contentMap) {
		Content resultContent = new Content();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		Object orderId = contentMap.get("order_id");
		log.info("confirmOrder by orderId:" + orderId);
		if (orderId == null) {
			return ResponseContentHelper.genSimpleResponseContentWithoutType(MsgConstants.ERROR_CODE_1,
					"order_id cannot be empty!");
		}
		Order order = orderDao.findById(orderId.toString());
		resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
		resultMap.put(MsgConstants.MSG,
				order != null ? "find order ContactInfo success!" : "find order ContactInfo failure!");
		resultMap.put(MsgConstants.ORDERID, orderId);
		resultMap.put(MsgConstants.ORDERSTATUS, order != null ? order.getPhoneList() : null);
		resultContent.setData(resultMap);
		return resultContent;
	}

}
