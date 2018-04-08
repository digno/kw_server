/**
 * 
 */
package nz.co.rubz.kiwi;

/**
 * @author lvqi
 * 
 */

public enum ServiceKeyEnum {
	// 用户服务
	USER(0),
	// 好友服务
	FRIEND(1),
	// 活动服务
	CLASS(2),
	// 回复服务
	NOTIFY(3),
	// 客户端服务
	CLIENT(4),
	COMMENT(5),
	SCHEDULE(6),
	SYSTEM(9),
	// 客户端响应信息
	PONG(10);

	private static ServiceKeyEnum[] allEnums = { USER, FRIEND, CLASS,
 NOTIFY,
			CLIENT, COMMENT, SCHEDULE, SYSTEM,PONG };

	private ServiceKeyEnum(int value) {
	}

	public static ServiceKeyEnum[] getAllEnums() {
		return allEnums;
	}

	public int value() {
		return ordinal();
	}

	public static ServiceKeyEnum getEnum(int value) {
		switch (value) {
		case 0:
			return USER;
		case 1:
			return FRIEND;
		case 2:
			return CLASS;
		case 3:
			return NOTIFY;
		case 4:
			return CLIENT;
		case 5:
			return COMMENT;
		case 6:
			return SCHEDULE;
		case 9:
			return SYSTEM;
		case 10:
			return PONG;
		default:
			return null;
		}
	}

	public static ServiceKeyEnum getEnum(String value) {
		return ServiceKeyEnum.valueOf(value);
	}

	/**
	 * Checks whether the enum's value is greater than the input enum's value.
	 */
	public boolean above(ServiceKeyEnum input) {
		return compareTo(input) > 0;
	}

	/**
	 * Checks whether the enum's value is less than the input enum's value.
	 */
	public boolean below(ServiceKeyEnum input) {
		return compareTo(input) < 0;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(ServiceKeyEnum.USER.ordinal());
		System.out.println(ServiceKeyEnum.getEnum("USER"));
	}

}
