package nz.co.rubz.kiwi.bean;

import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;

@Entity(noClassnameStored = true)
public class Food extends BaseEntity {

	private static final long serialVersionUID = -6397396425488986556L;

	/**
	 * 食物描述
	 */
	private String description;
	/**
	 * 食物id
	 */
	@Property("food_id")
	private Integer foodId;
	/**
	 * 食物名
	 */
	@Property("food_name")
	private String foodName;
	/**
	 * 食物图片
	 */
	@Property("image_url")
	private String imageUrl;
	/**
	 * 是否招牌菜
	 */
	@Property("is_featured")
	private Integer isFeatured;
	/**
	 * 是否配菜
	 */
	@Property("is_gum")
	private Integer isGum;
	/**
	 * 是否新菜
	 */
	@Property("is_new")
	private Integer isNew;
	/**
	 * 是否辣
	 */
	@Property("is_spicy")
	private Integer isSpciy;
	/**
	 * 是否有效
	 */
	@Property("is_valid")
	private Integer isValid;
	/**
	 * 食物评价，依次为1-5星评价的数目
	 */
	@Property("num_ratings")
	private Integer numRating;
	/**
	 * 食物价格
	 */
	private Float price;
	/**
	 * 最近一个月的售出份数
	 */
	@Property("recent_popularity")
	private Integer recentPopularity;
	/**
	 * 最近一个月的评价
	 */
	@Property("recent_rating")
	private Float recentRating;
	/**
	* 
	*/
	@Property("restaurant_id")
	private Integer restaurantId;
	/**
	 * 餐厅id
	 */
	@Property("restaurant_name")
	private String restaurantName;
	/**
	 * 餐厅名称
	 */
	private Integer stock;
	/**
	 * 属性, 参考 食物属性
	 */
	private List<FoodAttrEnum> attributes;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getFoodId() {
		return foodId;
	}

	public void setFoodId(Integer foodId) {
		this.foodId = foodId;
	}

	public String getFoodName() {
		return foodName;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Integer getIsFeatured() {
		return isFeatured;
	}

	public void setIsFeatured(Integer isFeatured) {
		this.isFeatured = isFeatured;
	}

	public Integer getIsGum() {
		return isGum;
	}

	public void setIsGum(Integer isGum) {
		this.isGum = isGum;
	}

	public Integer getIsNew() {
		return isNew;
	}

	public void setIsNew(Integer isNew) {
		this.isNew = isNew;
	}

	public Integer getIsSpciy() {
		return isSpciy;
	}

	public void setIsSpciy(Integer isSpciy) {
		this.isSpciy = isSpciy;
	}

	public Integer getIsValid() {
		return isValid;
	}

	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	public Integer getNumRating() {
		return numRating;
	}

	public void setNumRating(Integer numRating) {
		this.numRating = numRating;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Integer getRecentPopularity() {
		return recentPopularity;
	}

	public void setRecentPopularity(Integer recentPopularity) {
		this.recentPopularity = recentPopularity;
	}

	public Float getRecentRating() {
		return recentRating;
	}

	public void setRecentRating(Float recentRating) {
		this.recentRating = recentRating;
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

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public List<FoodAttrEnum> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<FoodAttrEnum> attributes) {
		this.attributes = attributes;
	}

}
