package nz.co.rubz.kiwi.bean;

import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;

@Entity(noClassnameStored = true)
public class Menu extends BaseEntity {

	private static final long serialVersionUID = 5425530606813376517L;
	/**
	 * 分类名
	 */
	private String name;
	/**
	 * 描述
	 */
	private String desciription;
	/**
	 * 图标地址
	 */
	@Property("icon_url")
	private String iconUrl;
	/**
	 * 活动信息
	 */
	@Property("is_activity")
	private Boolean isActivity;
	/**
	 * 是否是活动分类
	 */

	private Activity activity;
	/**
	 * 分类类型. 1: 普通分类 2: 热销榜 3: 优惠分类 4: 必点分类 5: 非必点分类
	 */
	private Integer type;
	/**
	 * food 列表, 参考 多属性菜单商品
	 */

	private List<MenuItem> foods;
	/**
	 * 默认展示
	 */
	@Property("is_selected")
	private Boolean isSelected;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesciription() {
		return desciription;
	}

	public void setDesciription(String desciription) {
		this.desciription = desciription;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public Boolean getIsActivity() {
		return isActivity;
	}

	public void setIsActivity(Boolean isActivity) {
		this.isActivity = isActivity;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public List<MenuItem> getFoods() {
		return foods;
	}

	public void setFoods(List<MenuItem> foods) {
		this.foods = foods;
	}

	public Boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}

}
