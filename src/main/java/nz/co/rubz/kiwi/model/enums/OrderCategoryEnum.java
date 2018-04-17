package nz.co.rubz.kiwi.model.enums;

/**
 * 
 * @author lvqi1
 *
 */

public enum OrderCategoryEnum {

	/**
	 * 訂單類型
	 * 
	 * 1 食物 2 配送费 3 优惠券 102 打包费
	 */
	FOOD(1), DELIVERY(2), COUPON(3), PACK(102);

	private static OrderCategoryEnum[] allEnums = { FOOD, DELIVERY, COUPON, PACK };

	private OrderCategoryEnum(int value) {
	}

	public static OrderCategoryEnum[] getAllEnums() {
		return allEnums;
	}

	public int value() {
		return ordinal();
	}

	public static OrderCategoryEnum getEnum(int value) {
		switch (value) {
		case 1:
			return FOOD;
		case 2:
			return DELIVERY;
		case 3:
			return COUPON;
		case 102:
			return PACK;
		default:
			return null;
		}
	}

	public static OrderCategoryEnum getEnum(String value) {
		return OrderCategoryEnum.valueOf(value);
	}

	/**
	 * Checks whether the enum's value is greater than the input enum's value.
	 */
	public boolean above(OrderCategoryEnum input) {
		return compareTo(input) > 0;
	}

	/**
	 * Checks whether the enum's value is less than the input enum's value.
	 */
	public boolean below(OrderCategoryEnum input) {
		return compareTo(input) < 0;
	}

}
