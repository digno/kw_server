package nz.co.rubz.kiwi.model.enums;

/**
 * https://openapi-doc.faas.ele.me/v2/appendix/enums.html#enum-order-invalid-type
 * TODO
 * @author lvqi1
 *
 */

public enum CancelOrderEnum {

/** 
 * FEATURED(0) 招牌 , SPICY(1) 辣  ,NEW (2) 新
 */
	FEATURED(0), SPICY(1) ,NEW (2);

	private static CancelOrderEnum[] allEnums = { FEATURED, SPICY ,NEW};

	private CancelOrderEnum(int value) {
	}

	public static CancelOrderEnum[] getAllEnums() {
		return allEnums;
	}

	public int value() {
		return ordinal();
	}

	public static CancelOrderEnum getEnum(int value) {
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

	public static CancelOrderEnum getEnum(String value) {
		return CancelOrderEnum.valueOf(value);
	}

	/**
	 * Checks whether the enum's value is greater than the input enum's value.
	 */
	public boolean above(CancelOrderEnum input) {
		return compareTo(input) > 0;
	}

	/**
	 * Checks whether the enum's value is less than the input enum's value.
	 */
	public boolean below(CancelOrderEnum input) {
		return compareTo(input) < 0;
	}

}
