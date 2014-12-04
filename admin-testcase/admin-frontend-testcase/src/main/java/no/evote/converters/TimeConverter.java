package no.evote.converters;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "timeConverter")
public class TimeConverter extends DateConverter {

	public TimeConverter() {
		super("@common.date.time_pattern");
	}

	@Override
	protected String generateSummaryErrorMessage(final UIComponent component, final String value, final DateFormat dateFormat) {
		String summary = getMessageProvider().get("javax.faces.converter.DateTimeConverter.TIME");
		return MessageFormat.format(summary, value, "", component.getAttributes().get("label"));
	}

	@Override
	protected String generateDetailErrorMessage(final UIComponent component, final String value, final DateFormat dateFormat) {
		String detail = getMessageProvider().get("javax.faces.converter.DateTimeConverter.TIME_detail");
		return MessageFormat.format(detail, value, dateFormat.format(new Date()), component.getAttributes().get("label"));
	}

}
