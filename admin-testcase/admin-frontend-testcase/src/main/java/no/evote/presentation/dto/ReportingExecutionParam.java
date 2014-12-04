package no.evote.presentation.dto;

public class ReportingExecutionParam {
	public static final int BOOLEAN = 0;
	public static final int DATE = 1;
	public static final int DOUBLE = 2;
	public static final int INTEGER = 3;
	public static final int STRING = 4;
	public static final int LIKE_STRING = 5;

	public static final String REGEX_BOOLEAN = ".+_BL";
	public static final String REGEX_DATE = ".+_DT";
	public static final String REGEX_DOUBLE = ".+_DB";
	public static final String REGEX_INTEGER = ".+_INT";
	public static final String REGEX_STRING = ".+_STR";
	public static final String REGEX_LIKE_STRING = ".+_LSTR";

	private int type;
	private String id;
	private String label;
	private Object value;

	public int getType() {
		return type;
	}

	public void setType(final int type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(final Object value) {
		this.value = value;
	}
}
