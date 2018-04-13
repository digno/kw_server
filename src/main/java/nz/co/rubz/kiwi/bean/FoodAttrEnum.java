package nz.co.rubz.kiwi.bean;


public enum FoodAttrEnum {

/** 
 * FEATURED(0) 招牌 , SPICY(1) 辣  ,NEW (2) 新
 */
	FEATURED(0), SPICY(1) ,NEW (2);

	private static FoodAttrEnum[] allEnums = { FEATURED, SPICY ,NEW};

	private FoodAttrEnum(int value) {
	}

	public static FoodAttrEnum[] getAllEnums() {
		return allEnums;
	}

	public int value() {
		return ordinal();
	}

	public static FoodAttrEnum getEnum(int value) {
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

	public static FoodAttrEnum getEnum(String value) {
		return FoodAttrEnum.valueOf(value);
	}

	/**
	 * Checks whether the enum's value is greater than the input enum's value.
	 */
	public boolean above(FoodAttrEnum input) {
		return compareTo(input) > 0;
	}

	/**
	 * Checks whether the enum's value is less than the input enum's value.
	 */
	public boolean below(FoodAttrEnum input) {
		return compareTo(input) < 0;
	}

}
