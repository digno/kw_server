/**
 * 
 */
package nz.co.rubz.kiwi;

/**
 * @author lvqi
 * 
 */

public enum KiwiServiceKeyEnum {
	// 用户服务
	USER(0),
	// 餐厅服务
	RESTAURANT(1),
	// 食物服务
	FOOD(2),
	// 订单服务
	ORDER(3),
	// 菜单服务
	MENU(4),
	// 购物车服务
	CART(5),
	// 配送服务
	DELIVERY(6),
	// 支付服务
	PAYMENT(7),
	// 通知服务
	NOTIFY(8),
	// 客户端服务
	CLIENT(9),
	// 评论服务
	COMMENT(10),
	// 系统服务
	SYSTEM(11),
	// 客户端响应信息
	PONG(12);

	private static KiwiServiceKeyEnum[] allEnums = { USER, RESTAURANT, FOOD,ORDER,MENU,CART,DELIVERY,PAYMENT,
 NOTIFY,
			CLIENT, COMMENT, SYSTEM,PONG };

	private KiwiServiceKeyEnum(int value) {
	}

	public static KiwiServiceKeyEnum[] getAllEnums() {
		return allEnums;
	}

	public int value() {
		return ordinal();
	}

	public static KiwiServiceKeyEnum getEnum(int value) {
		switch (value) {
		case 0:
			return USER;
		case 1:
			return RESTAURANT;
		case 2:
			return FOOD;
		case 3:
			return ORDER;
		case 4:
			return MENU;
		case 5:
			return CART;
		case 6:
			return DELIVERY;
		case 7:
			return PAYMENT;
		case 8:
			return NOTIFY;
		case 9:
			return CLIENT;
		case 10:
			return COMMENT;
		case 11:
			return SYSTEM;
		case 12:
			return PONG;
		default:
			return null;
		}
	}

	public static KiwiServiceKeyEnum getEnum(String value) {
		return KiwiServiceKeyEnum.valueOf(value);
	}

	/**
	 * Checks whether the enum's value is greater than the input enum's value.
	 */
	public boolean above(KiwiServiceKeyEnum input) {
		return compareTo(input) > 0;
	}

	/**
	 * Checks whether the enum's value is less than the input enum's value.
	 */
	public boolean below(KiwiServiceKeyEnum input) {
		return compareTo(input) < 0;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(KiwiServiceKeyEnum.USER.ordinal());
		System.out.println(KiwiServiceKeyEnum.getEnum("USER"));
	}

}
