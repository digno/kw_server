package nz.co.rubz.kiwi.bean;

/**
 * https://openapi-doc.faas.ele.me/v2/appendix/enums.html#action
 * 
 * @author lvqi1
 *
 */

public enum BusyLevelEnum {

/** 
 * 0	BUSY_LEVEL_FREE（餐厅正常营业）
2	BUSY_LEVEL_CLOSED（餐厅休息中）
3	BUSY_LEVEL_NETWORK_UNSTABLE（餐厅网络不稳定/电话订餐）
4	BUSY_LEVEL_HOLIDAY（餐厅放假中）
 */
	BUSY_LEVEL_FREE(0),BUSY_LEVEL_CLOSED (2) ,BUSY_LEVEL_NETWORK_UNSTABLE (3),BUSY_LEVEL_HOLIDAY (4);

	private static BusyLevelEnum[] allEnums = { BUSY_LEVEL_FREE ,BUSY_LEVEL_CLOSED,BUSY_LEVEL_NETWORK_UNSTABLE ,BUSY_LEVEL_HOLIDAY};

	private BusyLevelEnum(int value) {
	}

	public static BusyLevelEnum[] getAllEnums() {
		return allEnums;
	}

	public int value() {
		return ordinal();
	}

	public static BusyLevelEnum getEnum(int value) {
		switch (value) {
		case 0:
			return BUSY_LEVEL_FREE;
		 
		case 2:
			return BUSY_LEVEL_CLOSED;
		case 3:
			return BUSY_LEVEL_NETWORK_UNSTABLE;
		case 4:
			return BUSY_LEVEL_HOLIDAY;
		default:
			return null;
		}
	}

	public static BusyLevelEnum getEnum(String value) {
		return BusyLevelEnum.valueOf(value);
	}

	/**
	 * Checks whether the enum's value is greater than the input enum's value.
	 */
	public boolean above(BusyLevelEnum input) {
		return compareTo(input) > 0;
	}

	/**
	 * Checks whether the enum's value is less than the input enum's value.
	 */
	public boolean below(BusyLevelEnum input) {
		return compareTo(input) < 0;
	}

}
