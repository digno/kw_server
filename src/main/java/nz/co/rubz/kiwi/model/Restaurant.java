package nz.co.rubz.kiwi.model;

import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;

 

@Entity(noClassnameStored = true)
public class Restaurant extends BaseEntity {

	private static final long serialVersionUID = 1689768433352425424L;

	/**
	 * 餐厅地址
	 */
	@Property("address_text")
	private String addressText;
	/**
	 * 配送费
	 */
	@Property("agent_fee")
	private Float agentFee;
	/**
	 * 餐厅繁忙状态，具体参考：餐厅营业状态
	 */
	@Property("busy_level")
	private Integer busyLevel;
	/**
	 * 城市id，详见附录
	 */
	@Property("city_id")
	private Integer cityId;
	/**
	 * 关闭原因
	 */
	@Property("close_description")
	private String closeDescription;
	/**
	 * 起送价
	 */
	@Property("deliver_amount")
	private Float deliverAmount;
	/**
	 * 2周内的平均送餐时间
	 */
	@Property("deliver_spent")
	private Integer deliverSpent;
	/**
	 * 餐厅描述
	 */
	private String description;
	/**
	 * 餐厅口味
	 */
	private String flavors;
	/**
	 * 餐厅Logo地址
	 */
	@Property("image_url")
	private String imageUrl;
	/**
	 * 是否支持开发票: 1, 是; 2, 否.
	 */
	private Integer invoice;
	/**
	 * 最小发票金额
	 */
	@Property("invoice_min_amount")
	private Float invoiceMinAmount;
	/**
	 * 是否支持预定
	 */
	@Property("is_bookable")
	private Integer isBookable;
	/**
	 * 预定时间选项 预订单说明
	 */
	@Property("deliver_times")
	private List<String> deliverTimes;
	/**
	 * 是否正在营业, 在营业的前提下，再通过total_status字段判断商家的具体营业状态 餐厅整体营业状态
	 */
	@Property("is_open")
	private Boolean isOpen;
	/**
	 * 是否隐藏电话号码
	 */
	@Property("is_phone_hidden")
	private Integer isPhoneHidden;
	/**
	 * 是否品牌馆餐厅
	 */
	@Property("is_premium")
	private Integer isPremium;
	/**
	 * 纬度
	 */
	private Float latitude;
	/**
	 * 经度
	 */
	private Float longitude;
	/**
	 * 接受訂單的電話號碼
	 */
	private String mobile;
	/**
	 * 免配送费的最低消费额度
	 */

	@Property("no_agent_fee_total")
	private Float noAgentFeeTotal;
	/**
	 * 餐厅评价，依次为1-5星评价的数目
	 */
	@Property("num_ratings")
	private List<Integer> numRatings;
	/**
	 * 订单模式，详见附录
	 */

	@Property("order_mode")
	private Integer orderMode;
	/**
	 * 是否支持在线支付
	 */
	@Property("online_payment")
	private Integer onlinePayment;
	/**
	 * 促销信息或者商家公告
	 */
	@Property("promotion_info")
	private String promotionInfo;
	/**
	 * 最近一个月美食销量
	 */
	@Property("recent_food_popularity")
	private Integer recentFoodPopularity;
	/**
	 * 餐厅名称
	 */
	@Property("restaurant_name")
	private String restaurantName;
	/**
	 * 营业时间
	 */
	@Property("serving_time")
	private List<String> servingTime;
	/**
	 * 是否支持在线订餐
	 */
	@Property("support_online")
	private Boolean supportOnline;
	/**
	 * 餐厅整体营业状态 餐厅整体营业状态
	 * https://openapi-doc.faas.ele.me/v2/appendix/enums.html#enum-restaurant-total-status
	 * 
	 * 1	餐厅营业中
	 * 4	餐厅休息中
	 * 6	只接受电话订餐
	 * 7	餐厅休假中
	 */
	@Property("total_status")
	private Integer totalStatus;
	/**
	 * 是否支持优惠券
	 */
	@Property("support_coupon")
	private Boolean supportCoupon;
	/**
	 * 最近一个月订单量
	 */
	@Property("recent_order_num")
	private Integer recentOrderNum;
	/**
	 * 餐厅支持的支付方式, 参考 餐厅支付方式说明
	 */
	@Property("payment_method")
	private Integer paymentMethod;
	/**
	 * json 餐厅执照，内部字段 { business_license_image: 营业执照图片
	 * ,catering_service_license_image: 餐厅服务许可证图片}
	 */
	@Property("restaurant_license")
	private String restaurantLicense;
	/**
	 * 是否新店
	 */
	@Property("new_restaurant")
	private Boolean newRestaurant;
	/**
	 * 活动, 参考餐厅活动
	 */
	private List<Activity> activities;
	/**
	 * 店铺唯一名称alias
	 */
	@Property("name_for_url")
	private String nameForUrl;
	/**
	 * 店铺联系方式
	 */
	@Property("phone_list")
	private List<String> phoneList;
	/**
	 * 店铺 ID
	 */
	@Property("restaurant_id")
	private Integer restaurantId;
	/**
	 * 饿了么 web 地址
	 */
	@Property("restaurant_url")
	private String restaurantUrl;
	/**
	 * 餐厅分类, 参考餐厅分类
	 */
	@Property("category_ids")
	private List<Integer> categoryIds;
	public String getAddressText() {
		return addressText;
	}
	public void setAddressText(String addressText) {
		this.addressText = addressText;
	}
	public Float getAgentFee() {
		return agentFee;
	}
	public void setAgentFee(Float agentFee) {
		this.agentFee = agentFee;
	}
	public Integer getBusyLevel() {
		return busyLevel;
	}
	public void setBusyLevel(Integer busyLevel) {
		this.busyLevel = busyLevel;
	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public String getCloseDescription() {
		return closeDescription;
	}
	public void setCloseDescription(String closeDescription) {
		this.closeDescription = closeDescription;
	}
	public Float getDeliverAmount() {
		return deliverAmount;
	}
	public void setDeliverAmount(Float deliverAmount) {
		this.deliverAmount = deliverAmount;
	}
	public Integer getDeliverSpent() {
		return deliverSpent;
	}
	public void setDeliverSpent(Integer deliverSpent) {
		this.deliverSpent = deliverSpent;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFlavors() {
		return flavors;
	}
	public void setFlavors(String flavors) {
		this.flavors = flavors;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Integer getInvoice() {
		return invoice;
	}
	public void setInvoice(Integer invoice) {
		this.invoice = invoice;
	}
	public Float getInvoiceMinAmount() {
		return invoiceMinAmount;
	}
	public void setInvoiceMinAmount(Float invoiceMinAmount) {
		this.invoiceMinAmount = invoiceMinAmount;
	}
	public Integer getIsBookable() {
		return isBookable;
	}
	public void setIsBookable(Integer isBookable) {
		this.isBookable = isBookable;
	}
	public List<String> getDeliverTimes() {
		return deliverTimes;
	}
	public void setDeliverTimes(List<String> deliverTimes) {
		this.deliverTimes = deliverTimes;
	}
	public Boolean getIsOpen() {
		return isOpen;
	}
	public void setIsOpen(Boolean isOpen) {
		this.isOpen = isOpen;
	}
	public Integer getIsPhoneHidden() {
		return isPhoneHidden;
	}
	public void setIsPhoneHidden(Integer isPhoneHidden) {
		this.isPhoneHidden = isPhoneHidden;
	}
	public Integer getIsPremium() {
		return isPremium;
	}
	public void setIsPremium(Integer isPremium) {
		this.isPremium = isPremium;
	}
	public Float getLatitude() {
		return latitude;
	}
	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}
	public Float getLongitude() {
		return longitude;
	}
	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Float getNoAgentFeeTotal() {
		return noAgentFeeTotal;
	}
	public void setNoAgentFeeTotal(Float noAgentFeeTotal) {
		this.noAgentFeeTotal = noAgentFeeTotal;
	}
	public List<Integer> getNumRatings() {
		return numRatings;
	}
	public void setNumRatings(List<Integer> numRatings) {
		this.numRatings = numRatings;
	}
	public Integer getOrderMode() {
		return orderMode;
	}
	public void setOrderMode(Integer orderMode) {
		this.orderMode = orderMode;
	}
	public Integer getOnlinePayment() {
		return onlinePayment;
	}
	public void setOnlinePayment(Integer onlinePayment) {
		this.onlinePayment = onlinePayment;
	}
	public String getPromotionInfo() {
		return promotionInfo;
	}
	public void setPromotionInfo(String promotionInfo) {
		this.promotionInfo = promotionInfo;
	}
	public Integer getRecentFoodPopularity() {
		return recentFoodPopularity;
	}
	public void setRecentFoodPopularity(Integer recentFoodPopularity) {
		this.recentFoodPopularity = recentFoodPopularity;
	}
	public String getRestaurantName() {
		return restaurantName;
	}
	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}
	public List<String> getServingTime() {
		return servingTime;
	}
	public void setServingTime(List<String> servingTime) {
		this.servingTime = servingTime;
	}
	public Boolean getSupportOnline() {
		return supportOnline;
	}
	public void setSupportOnline(Boolean supportOnline) {
		this.supportOnline = supportOnline;
	}
	public Integer getTotalStatus() {
		return totalStatus;
	}
	public void setTotalStatus(Integer totalStatus) {
		this.totalStatus = totalStatus;
	}
	public Boolean getSupportCoupon() {
		return supportCoupon;
	}
	public void setSupportCoupon(Boolean supportCoupon) {
		this.supportCoupon = supportCoupon;
	}
	public Integer getRecentOrderNum() {
		return recentOrderNum;
	}
	public void setRecentOrderNum(Integer recentOrderNum) {
		this.recentOrderNum = recentOrderNum;
	}
	public Integer getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(Integer paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getRestaurantLicense() {
		return restaurantLicense;
	}
	public void setRestaurantLicense(String restaurantLicense) {
		this.restaurantLicense = restaurantLicense;
	}
	public Boolean getNewRestaurant() {
		return newRestaurant;
	}
	public void setNewRestaurant(Boolean newRestaurant) {
		this.newRestaurant = newRestaurant;
	}
	public List<Activity> getActivities() {
		return activities;
	}
	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}
	public String getNameForUrl() {
		return nameForUrl;
	}
	public void setNameForUrl(String nameForUrl) {
		this.nameForUrl = nameForUrl;
	}
	public List<String> getPhoneList() {
		return phoneList;
	}
	public void setPhoneList(List<String> phoneList) {
		this.phoneList = phoneList;
	}
	public Integer getRestaurantId() {
		return restaurantId;
	}
	public void setRestaurantId(Integer restaurantId) {
		this.restaurantId = restaurantId;
	}
	public String getRestaurantUrl() {
		return restaurantUrl;
	}
	public void setRestaurantUrl(String restaurantUrl) {
		this.restaurantUrl = restaurantUrl;
	}
	public List<Integer> getCategoryIds() {
		return categoryIds;
	}
	public void setCategoryIds(List<Integer> categoryIds) {
		this.categoryIds = categoryIds;
	}
	
	

}
