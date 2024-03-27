package GUI;

public enum Currency {
	USD("US Dollar", "USD"),
	EUR("Euro", "EUR"),
	INR("Indian Rupee", "INR"),
	GBP("British Pound", "GBP"),
	CAD("Canadian Dollar", "CAD"),
	AUD("Australian Dollar", "AUD"),
	BGN("Bulgarian Lev", "BGN"),
	BRL("Brazilian Real", "BRL"),
	CHF("Swiss Franc", "CHF"),
	CNY("Chinese Yuan", "CNY"),
	DKK("Danish Krone", "DKK"),
	HKD("Hong Kong Dollar", "HKD"),
	HRK("Croatian Kuna", "HRK"),
	ILS("Israeli New Shekel", "ILS"),
	MYR("Malaysian Ringgit", "MYR"),
	NZD("New Zealand Dollar", "NZD"),
	PLN("Polish Zloty", "PLN"),
	RON("Romanian Leu", "RON"),
	SGD("Singapore Dollar", "SGD");

	private final String description;
	private final String code;

	Currency(String description, String code) {
		this.description = description;
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public String getCode() {
		return code;
	}
}
