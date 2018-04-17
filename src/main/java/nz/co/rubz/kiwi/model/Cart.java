package nz.co.rubz.kiwi.model;

import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;

import nz.co.rubz.kiwi.model.attrs.FeeDetail;
import nz.co.rubz.kiwi.model.attrs.PromiseDelivery;

@Entity(noClassnameStored = true)
public class Cart extends BaseEntity {

	/**
	 * 购物车id
	 */
	private Integer cid;
	/**
	 * 创建购物车时间戳
	 */
	@Property("create_time")
	private Float createTime;

	/**
	 * 购物车金额
	 */
	private Float total;
	/**
	 * 优惠前的价格
	 */
	@Property("original_total")
	private Float originalTotal;
	/**
	 * 起送价
	 */
	@Property("deliver_amount")
	private Float deliverAmount;
	/**
	 * 手机号
	 */
	private String phone;
	/**
	 * 购物车食物信息
	 * https://openapi-doc.faas.ele.me/v2/appendix/cart-detail.html#food
	 */
	 //TODO
	private List<Food> group;
	/**
	 * 是否在线支付
	 */
	@Property("is_online_paid")
	private Integer isOnlinePaid;
	/**
	 * 准时达信息: { “promise_delivery_time”: 50, //承诺送达时间,分钟 “platform_compensate”: 1,
	 * // 是否参加, 参考 是否参与准时达 }
	 */
	@Property("on_time_info")
	private PromiseDelivery onTimeInfo;
	/**
	 * 服务费明细, 服务费已经计算在配送费中, 参考 服务费明细
	 */
	@Property("service_fee_included")
	private FeeDetail serviceFeeIncluded;
	/**
	 * 是否在配送范围内
	 */
	@Property("is_in_delivery_area")
	private Boolean isInDeliveryArea;
	/**
	 * 预计配送时间 单位(分钟), 当前时间 + 预计时间 = 预计送达时间, 订单支付后: 支付时间 + 预计时间
	 */
	@Property("predict_deliver_time")
	private Integer predictDeliverTime;

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public Float getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Float createTime) {
		this.createTime = createTime;
	}

	public Float getTotal() {
		return total;
	}

	public void setTotal(Float total) {
		this.total = total;
	}

	public Float getOriginalTotal() {
		return originalTotal;
	}

	public void setOriginalTotal(Float originalTotal) {
		this.originalTotal = originalTotal;
	}

	public Float getDeliverAmount() {
		return deliverAmount;
	}

	public void setDeliverAmount(Float deliverAmount) {
		this.deliverAmount = deliverAmount;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<Food> getGroup() {
		return group;
	}

	public void setGroup(List<Food> group) {
		this.group = group;
	}

	public Integer getIsOnlinePaid() {
		return isOnlinePaid;
	}

	public void setIsOnlinePaid(Integer isOnlinePaid) {
		this.isOnlinePaid = isOnlinePaid;
	}

 
	public PromiseDelivery getOnTimeInfo() {
		return onTimeInfo;
	}

	public void setOnTimeInfo(PromiseDelivery onTimeInfo) {
		this.onTimeInfo = onTimeInfo;
	}

	public FeeDetail getServiceFeeIncluded() {
		return serviceFeeIncluded;
	}

	public void setServiceFeeIncluded(FeeDetail serviceFeeIncluded) {
		this.serviceFeeIncluded = serviceFeeIncluded;
	}

	public Boolean getIsInDeliveryArea() {
		return isInDeliveryArea;
	}

	public void setIsInDeliveryArea(Boolean isInDeliveryArea) {
		this.isInDeliveryArea = isInDeliveryArea;
	}

	public Integer getPredictDeliverTime() {
		return predictDeliverTime;
	}

	public void setPredictDeliverTime(Integer predictDeliverTime) {
		this.predictDeliverTime = predictDeliverTime;
	}

}
