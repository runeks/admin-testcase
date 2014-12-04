package no.evote.converters;

import java.text.DateFormat;
import java.text.MessageFormat;

import javax.faces.component.UIComponent;
import javax.faces.convert.FacesConverter;

/**
 * Convert dates to/from Date/String, using SimpleDateFormat and #{datePattern}, with the formatter configured as non-lenient. I.e. 2007-01-123456 will not be
 * considered a valid date. In default mode, the formatter would be lenient, and it would be a valid date.
 */
@FacesConverter(value = "nonLenientDateConverter")
public class NonLenientDateConverter extends DateConverter {

	public NonLenientDateConverter() {
		super("@common.date.date_pattern", "yyyy-MM-dd");
	}

	@Override
	protected String generateSummaryErrorMessage(final UIComponent component, final String value, final DateFormat dateFormat) {
		return getMessageProvider().get("@common.date.invalid_date.summary");
	}

	@Override
	protected String generateDetailErrorMessage(final UIComponent component, final String value, final DateFormat dateFormat) {
		String detail = getMessageProvider().get("@common.date.invalid_date.detail");
		return MessageFormat.format(detail, value);
	}

}
