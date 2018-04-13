package nz.co.rubz.kiwi.bean;

/**
 * https://openapi-doc.faas.ele.me/v2/appendix/enums.html#action
 * TODO
 * @author lvqi1
 *
 */

public enum ActionEnum {

/** 
 * FEATURED(0) 招牌 , SPICY(1) 辣  ,NEW (2) 新
 */
	FEATURED(0), SPICY(1) ,NEW (2);

	private static ActionEnum[] allEnums = { FEATURED, SPICY ,NEW};

	private ActionEnum(int value) {
	}

	public static ActionEnum[] getAllEnums() {
		return allEnums;
	}

	public int value() {
		return ordinal();
	}

	public static ActionEnum getEnum(int value) {
		switch (value) {
		case 0:
			return FEATURED;
		case 1:
			return SPICY;
		case 2:
			return NEW;
		default:
			return null;
		}
	}

	public static ActionEnum getEnum(String value) {
		return ActionEnum.valueOf(value);
	}

	/**
	 * Checks whether the enum's value is greater than the input enum's value.
	 */
	public boolean above(ActionEnum input) {
		return compareTo(input) > 0;
	}

	/**
	 * Checks whether the enum's value is less than the input enum's value.
	 */
	public boolean below(ActionEnum input) {
		return compareTo(input) < 0;
	}

}
