package GUI;

public enum Currency {
	// Enum constants with their ISO currency codes
	AUD("Indian Rupee", "AUD"),
	USD("US Dollar", "USD"),
	GBP("British Pound", "GBP"),
	EUR("Euro", "EUR"),
	CAD("Canadian Dollar", "CAD");

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
