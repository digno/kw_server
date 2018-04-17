package nz.co.rubz.kiwi.model;

import java.util.List;
import java.util.Map;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;


@Entity(noClassnameStored = true)
public class MenuItem extends BaseEntity {

	private static final long serialVersionUID = 4642952066503319079L;

	/**
	 * 餐厅 id
	 */
	@Property("restaurant_id")
	private Integer restaurantId;

	/**
	 * 分类 id
	 */
	@Property("category_id")
	private Integer category_id;
	/**
	 * item id
	 */
	@Property("item_id")
	private String item_id;
	/**
	 * 名字
	 */
	private String name;
	/**
	 * 拼音
	 */
	@Property("pinyin_name")
	private String pinyin_name;
	/**
	 * 描述
	 */
	private String desciription;
	/**
	 * 概况说明, 例: “2评价 月售6份”
	 */
	private String tips;
	/**
	 * 属性信息, 次要的规格信息, 不决定价格, 例如: [{“name”: “甜度”, “values”: [“加糖”, “不加糖”]}, …]
	 */
	private List<Map<String, List<String>>> attrs;
	/**
	 * 美食标签信息 例: “招牌”, “辣”, “推荐”等
	 */
	private List<String> attributes;
	/**
	 * 月售份数
	 */
	@Property("month_sales")
	private Integer monthSales;
	/**
	 * 评分
	 */
	private Float rating;
	/**
	 * 评价数
	 */
	@Property("rating_count")
	private Integer ratingCount;
	/**
	 * 规格信息, 例如: [{“name”: “规格”, “values”: [“不辣”, “中辣”, “大辣” ]}, …]
	 */
	private List<Map<String, List<String>>> specifications;
	/**
	 * 是否必点分类, 分类下必点一个, 否则无法下单
	 */
	@Property("is_essential")
	private Boolean isEssential;
	/**
	 * 好评数, 评分 4,5 的数量
	 */
	@Property("satisfy_count")
	private Integer satisfyCount;
	/**
	 * 好评率
	 */
	@Property("satisfy_rate")
	private Integer satisfyRate;
	/**
	 * 图片地址
	 */
	@Property("image_url")
	private Integer imageUrl;
	/**
	 * 购买限制条件, 例如 {“text”: “新用户专享”, “color”: “ff8c33”}
	 */
	private Map<String, String> limitation;
	/**
	 * 最少起购分数, 默认: 1
	 */
	@Property("min_purchase")
	private Integer minPurchase;
	/**
	 * sku 信息, 参考 多属性菜单sku
	 */
	private List<MenuSku> specfoods;
	public Integer getRestaurantId() {
		return restaurantId;
	}
	public void setRestaurantId(Integer restaurantId) {
		this.restaurantId = restaurantId;
	}
	public Integer getCategory_id() {
		return category_id;
	}
	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}
	public String getItem_id() {
		return item_id;
	}
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPinyin_name() {
		return pinyin_name;
	}
	public void setPinyin_name(String pinyin_name) {
		this.pinyin_name = pinyin_name;
	}
	public String getDesciription() {
		return desciription;
	}
	public void setDesciription(String desciription) {
		this.desciription = desciription;
	}
	public String getTips() {
		return tips;
	}
	public void setTips(String tips) {
		this.tips = tips;
	}
	public List<Map<String, List<String>>> getAttrs() {
		return attrs;
	}
	public void setAttrs(List<Map<String, List<String>>> attrs) {
		this.attrs = attrs;
	}
	public List<String> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}
	public Integer getMonthSales() {
		return monthSales;
	}
	public void setMonthSales(Integer monthSales) {
		this.monthSales = monthSales;
	}
	public Float getRating() {
		return rating;
	}
	public void setRating(Float rating) {
		this.rating = rating;
	}
	public Integer getRatingCount() {
		return ratingCount;
	}
	public void setRatingCount(Integer ratingCount) {
		this.ratingCount = ratingCount;
	}
	public List<Map<String, List<String>>> getSpecifications() {
		return specifications;
	}
	public void setSpecifications(List<Map<String, List<String>>> specifications) {
		this.specifications = specifications;
	}
	public Boolean getIsEssential() {
		return isEssential;
	}
	public void setIsEssential(Boolean isEssential) {
		this.isEssential = isEssential;
	}
	public Integer getSatisfyCount() {
		return satisfyCount;
	}
	public void setSatisfyCount(Integer satisfyCount) {
		this.satisfyCount = satisfyCount;
	}
	public Integer getSatisfyRate() {
		return satisfyRate;
	}
	public void setSatisfyRate(Integer satisfyRate) {
		this.satisfyRate = satisfyRate;
	}
	public Integer getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(Integer imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Map<String, String> getLimitation() {
		return limitation;
	}
	public void setLimitation(Map<String, String> limitation) {
		this.limitation = limitation;
	}
	public Integer getMinPurchase() {
		return minPurchase;
	}
	public void setMinPurchase(Integer minPurchase) {
		this.minPurchase = minPurchase;
	}
	public List<MenuSku> getSpecfoods() {
		return specfoods;
	}
	public void setSpecfoods(List<MenuSku> specfoods) {
		this.specfoods = specfoods;
	}

	
	
}
