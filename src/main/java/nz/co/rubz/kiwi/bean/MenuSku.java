package nz.co.rubz.kiwi.bean;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;

@Entity(noClassnameStored = true)
public class MenuSku extends BaseEntity {

	 
	private static final long serialVersionUID = -2004625662403753609L;
	/**
	 * 餐厅 id
	 */
	@Property("restaurant_id")
	private Integer restaurantId;
	/**
	 * sku id
	 */
	@Property("sku_id")
	private Integer skuId;
	/**
	 * 同 sku_id 一样, 下单购物车用 这个 id
	 */
	@Property("food_id")
	private Integer foodId;
	/**
	 * 名字
	 */
	private String name;
	/**
	 * 拼音
	 */
	@Property("pinyin_name")
	private String pinyinName;
	/**
	 * 近期评价数
	 */
	@Property("recent_rating")
	private Integer recentRating;
	/**
	 * 剩余优惠库存
	 */
	@Property("promotion_stock")
	private Integer promotionStock;
	/**
	 * 当前价格, 如果美食活动, 此价格是优惠后的价格
	 */
	private Float price;
	/**
	 * 是否必点, 必点分类下只要点一个 food 就好了, 如果必点商品售完, 不需要必点
	 */
	@Property("is_essential")
	private Boolean isEssential;
	/**
	 * 是否售完
	 */
	@Property("sold_out")
	private Boolean soldOut;
	/**
	 * 打包费
	 */
	@Property("packing_fee")
	private Float packingFee;
	/**
	 * item id
	 */
	@Property("item_id")
	private String itemId;
	/**
	 * 月销量
	 */
	@Property("recent_popularity")
	private Integer recentPopularity;
	/**
	 * 库存
	 */
	private Integer stock;
	/**
	 * 原价, 如果没有绑定活动, 该字段为空
	 */
	@Property("original_price")
	private Float originalPrice;
	/**
	 * 购物车加购模式, 1: 正常, 2: 必选 (如果没有加购必选菜, 无法下单)
	 */
	@Property("checkout_mode")
	private Integer checkoutMode;
	/**
	 * 规格信息 JSON
	 */
	private String specs;
	public Integer getRestaurantId() {
		return restaurantId;
	}
	public void setRestaurantId(Integer restaurantId) {
		this.restaurantId = restaurantId;
	}
	public Integer getSkuId() {
		return skuId;
	}
	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}
	public Integer getFoodId() {
		return foodId;
	}
	public void setFoodId(Integer foodId) {
		this.foodId = foodId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPinyinName() {
		return pinyinName;
	}
	public void setPinyinName(String pinyinName) {
		this.pinyinName = pinyinName;
	}
	public Integer getRecentRating() {
		return recentRating;
	}
	public void setRecentRating(Integer recentRating) {
		this.recentRating = recentRating;
	}
	public Integer getPromotionStock() {
		return promotionStock;
	}
	public void setPromotionStock(Integer promotionStock) {
		this.promotionStock = promotionStock;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public Boolean getIsEssential() {
		return isEssential;
	}
	public void setIsEssential(Boolean isEssential) {
		this.isEssential = isEssential;
	}
	public Boolean getSoldOut() {
		return soldOut;
	}
	public void setSoldOut(Boolean soldOut) {
		this.soldOut = soldOut;
	}
	public Float getPackingFee() {
		return packingFee;
	}
	public void setPackingFee(Float packingFee) {
		this.packingFee = packingFee;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public Integer getRecentPopularity() {
		return recentPopularity;
	}
	public void setRecentPopularity(Integer recentPopularity) {
		this.recentPopularity = recentPopularity;
	}
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	public Float getOriginalPrice() {
		return originalPrice;
	}
	public void setOriginalPrice(Float originalPrice) {
		this.originalPrice = originalPrice;
	}
	public Integer getCheckoutMode() {
		return checkoutMode;
	}
	public void setCheckoutMode(Integer checkoutMode) {
		this.checkoutMode = checkoutMode;
	}
	public String getSpecs() {
		return specs;
	}
	public void setSpecs(String specs) {
		this.specs = specs;
	}

	

}
