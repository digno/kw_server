package nz.co.rubz.kiwi.bean;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

@Embedded
public class FeeDetail {

	/**
	 * 夜间服务费
	 */
	@Property("night_service_fee")
	private Float nightServiceFee;
	/**
	 * 远距离服务费
	 */
	@Property("distance_service_fee")
	private Float distanceServiceFee;
	/**
	 * 原始配送费
	 */
	@Property("original_deliver_fee")
	private Float originalDeliverFee;
	/**
	 * 天气加价
	 */
	@Property("weather_fee")
	private Float weatherFee;
	/**
	 * 重量加价
	 */
	@Property("weight_fee")
	private Float weightFee;

	public Float getNightServiceFee() {
		return nightServiceFee;
	}

	public void setNightServiceFee(Float nightServiceFee) {
		this.nightServiceFee = nightServiceFee;
	}

	public Float getDistanceServiceFee() {
		return distanceServiceFee;
	}

	public void setDistanceServiceFee(Float distanceServiceFee) {
		this.distanceServiceFee = distanceServiceFee;
	}

	public Float getOriginalDeliverFee() {
		return originalDeliverFee;
	}

	public void setOriginalDeliverFee(Float originalDeliverFee) {
		this.originalDeliverFee = originalDeliverFee;
	}

	public Float getWeatherFee() {
		return weatherFee;
	}

	public void setWeatherFee(Float weatherFee) {
		this.weatherFee = weatherFee;
	}

	public Float getWeightFee() {
		return weightFee;
	}

	public void setWeightFee(Float weightFee) {
		this.weightFee = weightFee;
	}

}
