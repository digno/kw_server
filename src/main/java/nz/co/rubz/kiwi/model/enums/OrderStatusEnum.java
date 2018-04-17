package nz.co.rubz.kiwi.model.enums;

public enum OrderStatusEnum {

	/**
	 * -5 STATUS_CODE_NOT_PAID（等待支付） 
	 * -4 STATUS_CODE_PAYMENT_FAIL（支付失败）
	 * -1 STATUS_CODE_INVALID（订单已取消） 
	 * 0 STATUS_CODE_UNPROCESSED（订单未处理） 
	 * 2 STATUS_CODE_PROCESSED_AND_VALID（订单已处理） 
	 * 11 STATUS_CODE_USER_CONFIRMED（用户确认订单）
	 *
	 */
	STATUS_CODE_PAYMENT_FAIL(-4), STATUS_CODE_NOT_PAID(-5), STATUS_CODE_INVALID(-1), STATUS_CODE_UNPROCESSED(
			0), STATUS_CODE_PROCESSED_AND_VALID(2), STATUS_CODE_USER_CONFIRMED(11);

	private static OrderStatusEnum[] allEnums = { STATUS_CODE_PAYMENT_FAIL, STATUS_CODE_NOT_PAID, STATUS_CODE_INVALID,
			STATUS_CODE_UNPROCESSED, STATUS_CODE_PROCESSED_AND_VALID, STATUS_CODE_USER_CONFIRMED };

	private OrderStatusEnum(int value) {
	}

	public static OrderStatusEnum[] getAllEnums() {
		return allEnums;
	}

	public int value() {
		return ordinal();
	}

	public static OrderStatusEnum getEnum(int value) {
		switch (value) {
		case -4:
			return STATUS_CODE_PAYMENT_FAIL;
		case -5:
			return STATUS_CODE_NOT_PAID;
		case -1:
			return STATUS_CODE_INVALID;
		case 0:
			return STATUS_CODE_UNPROCESSED;
		case 2:
			return STATUS_CODE_PROCESSED_AND_VALID;
		case 11:
			return STATUS_CODE_USER_CONFIRMED;
		default:
			return null;
		}
	}

	public static OrderStatusEnum getEnum(String value) {
		return OrderStatusEnum.valueOf(value);
	}

	/**
	 * Checks whether the enum's value is greater than the input enum's value.
	 */
	public boolean above(OrderStatusEnum input) {
		return compareTo(input) > 0;
	}

	/**
	 * Checks whether the enum's value is less than the input enum's value.
	 */
	public boolean below(OrderStatusEnum input) {
		return compareTo(input) < 0;
	}

}
