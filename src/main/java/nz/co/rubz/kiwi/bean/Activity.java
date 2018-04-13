package nz.co.rubz.kiwi.bean;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;

@Entity(noClassnameStored = true)
public class Activity extends BaseEntity {

	private static final long serialVersionUID = 92326029885531083L;

	/**
	 * 活动ID
	 */
	private Integer aid;

	/**
	 * 活动名称
	 */
	private Integer name;

	/**
	 * 活动开始日期
	 */
	@Property("begin_date")
	private String beginDate;

	/**
	 * 活动结束日期
	 */
	@Property("end_date")
	private String endDate;

	/**
	 * 活动类型： 1：餐厅活动 2：美食活动
	 */
	private Integer type;

	/**
	 * 具体的活动类型，参考 餐厅活动类型 和 美食活动类型 餐厅活动类型 7 新用户折扣（可以和其他活动共享） 102 活动折扣（类似满减的活动折扣） 103
	 * 新用户折扣（不可以和其他活动共享）
	 * 
	 * 美食活动 1 折扣价（按百分比折扣） 2 减价（按金额进行折扣） 3 第N份折扣价（第N件按百分比折扣） 4
	 * 固定价格（不管原价多少都按固定价格售卖，特价菜） 5 赠品（购物某个美食附送赠品，可以与其它美食活动并存）
	 */
	@Property("detail_type")
	private Integer detailType;

	public Integer getAId() {
		return aid;
	}

	public void setAId(Integer id) {
		this.aid = id;
	}

	public Integer getName() {
		return name;
	}

	public void setName(Integer name) {
		this.name = name;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getDetailType() {
		return detailType;
	}

	public void setDetailType(Integer detailType) {
		this.detailType = detailType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 活动文本描述
	 */
	private String description;

}
