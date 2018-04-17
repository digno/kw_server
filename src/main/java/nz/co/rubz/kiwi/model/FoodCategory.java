package nz.co.rubz.kiwi.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;

@Entity(noClassnameStored = true)
public class FoodCategory extends BaseEntity {

	private static final long serialVersionUID = 2599889918810131941L;

	/**
	 * 食物分类id
	 */
	@Property("category_id")
	private Integer categoryId;

	/**
	 * 食物分类名
	 */
	private String name;

	/**
	 * 食物是否有效
	 */
	@Property("is_valid")
	private Integer isValid;

	/**
	 * 食物分类权重，数值越大，在菜单中的排序就越靠前
	 */
	private Integer weight;

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getIsValid() {
		return isValid;
	}

	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

}
