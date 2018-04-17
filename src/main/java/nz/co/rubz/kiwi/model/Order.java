package nz.co.rubz.kiwi.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;

import nz.co.rubz.kiwi.model.attrs.PromiseDelivery;

@Entity(noClassnameStored = true)
public class Order extends BaseEntity {

	 
	private static final long serialVersionUID = 2093824461700315712L;
	/**
	 * 顾客送餐地址
	 */
	private String address;
	/**
	 * 下单时间 2017-08-08 09:00:00
	 */
	@Property("created_at")
	private Date createdAt;
	/**
	 * 支付时间, 未支付为空
	 */
	@Property("active_at")
	private Date activeAt;
	/**
	 * 配送费
	 */
	@Property("deliver_fee")
	private Float deliverFee;
	/**
	 * 送餐时间 2017-08-08 09:30:00
	 */
	@Property("deliver_time")
	private Date deliverTime;
	/**
	 * 配送状态（仅用于第三方配送）
	 * 
	 * https://openapi-doc.faas.ele.me/v2/appendix/enums.html#enum-new-delivery-state-code
	 * 1	待分配（物流系统已生成运单，待分配配送商）
	 * 2	待分配（配送系统已接单，待分配配送员）
	 * 3	待取餐（已分配给配送员，配送员未取餐）
	 * 4	到店（配送员已经到店）
	 * 5	配送中（配送员已取餐，正在配送）
	 * 6	配送成功（配送员配送完成）
	 * 7	配送取消（商家可以重新发起配送）
	 * 8	配送异常（配送过程出现异常，如食物丢失、配送员意外…）
	 * 
	 */
	@Property("deliver_status")
	private Integer deliverStatus;
	/**
	 * 订单备注
	 */
	private String description;
	/**
	 * 订单详细类目列表
	 */
	private List<Food> detail; // TODO
	/**
	 * 发票抬头
	 */
	private String invoice;
	/**
	 * 是否预订单
	 */
	@Property("is_book")
	private Boolean isBook;
	/**
	 * 是否在线支付
	 */
	@Property("is_online_paid")
	private Boolean isOnlinePaid;
	/**
	 * 饿了么订单id
	 */
	@Property("order_id")
	private Integer orderId;
	/**
	 * 顾客联系电话
	 */
	@Property("phone_list")
	private String phoneList;
	/**
	 * 餐厅id
	 */
	@Property("restaurant_id")
	private Integer restaurantId;
	/**
	 * 餐厅名称
	 */
	@Property("restaurant_name")
	private String restaurantName;
	/**
	 * 订单状态
	 */
	@Property("status_code")
	private Integer statusCode;
	/**
	 * 用户id
	 */
	@Property("user_id")
	private Integer userId;
	/**
	 * 用户名
	 */
	@Property("user_name")
	private String userName;
	/**
	 * 菜价加上配送费和打包费的价格
	 */
	@Property("original_price")
	private Float originalPrice;
	/**
	 * 订单总价(减去优惠后的价格)
	 */
	@Property("total_price")
	private Float totalPrice;
	/**
	 * 订单收货人，例如：张三
	 */
	private String consignee;
	/**
	 * 订单收货地址经纬度，例如：31.2538,121.4185
	 */
	@Property("delivery_geo")
	private String deliveryGeo;
	/**
	 * 餐厅 logo 地址
	 */
	@Property("restaurant_image_path")
	private String restaurantImagePath;
	/**
	 * 是否是品牌馆
	 */
	@Property("is_premium")
	private Boolean isPremium;
	/**
	 * 准时达信息
	 */
	@Property("on_time_info")
	private PromiseDelivery onTimeInfo;
	/**
	 * 订单其他属性信息
	 */
	@Property("attribute")
	private Map<String, String> attribute;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getActiveAt() {
		return activeAt;
	}

	public void setActiveAt(Date activeAt) {
		this.activeAt = activeAt;
	}

	public Float getDeliverFee() {
		return deliverFee;
	}

	public void setDeliverFee(Float deliverFee) {
		this.deliverFee = deliverFee;
	}

	public Date getDeliverTime() {
		return deliverTime;
	}

	public void setDeliverTime(Date deliverTime) {
		this.deliverTime = deliverTime;
	}

	public Integer getDeliverStatus() {
		return deliverStatus;
	}

	public void setDeliverStatus(Integer deliverStatus) {
		this.deliverStatus = deliverStatus;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Food> getDetail() {
		return detail;
	}

	public void setDetail(List<Food> detail) {
		this.detail = detail;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public Boolean getIsBook() {
		return isBook;
	}

	public void setIsBook(Boolean isBook) {
		this.isBook = isBook;
	}

	public Boolean getIsOnlinePaid() {
		return isOnlinePaid;
	}

	public void setIsOnlinePaid(Boolean isOnlinePaid) {
		this.isOnlinePaid = isOnlinePaid;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getPhoneList() {
		return phoneList;
	}

	public void setPhoneList(String phoneList) {
		this.phoneList = phoneList;
	}

	public Integer getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(Integer restaurantId) {
		this.restaurantId = restaurantId;
	}

	public String getRestaurantName() {
		return restaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Float getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(Float originalPrice) {
		this.originalPrice = originalPrice;
	}

	public Float getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Float totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getDeliveryGeo() {
		return deliveryGeo;
	}

	public void setDeliveryGeo(String deliveryGeo) {
		this.deliveryGeo = deliveryGeo;
	}

	public String getRestaurantImagePath() {
		return restaurantImagePath;
	}

	public void setRestaurantImagePath(String restaurantImagePath) {
		this.restaurantImagePath = restaurantImagePath;
	}

	public Boolean getIsPremium() {
		return isPremium;
	}

	public void setIsPremium(Boolean isPremium) {
		this.isPremium = isPremium;
	}

	public PromiseDelivery getOnTimeInfo() {
		return onTimeInfo;
	}

	public void setOnTimeInfo(PromiseDelivery onTimeInfo) {
		this.onTimeInfo = onTimeInfo;
	}

	public Map<String, String> getAttribute() {
		return attribute;
	}

	public void setAttribute(Map<String, String> attribute) {
		this.attribute = attribute;
	}

}
