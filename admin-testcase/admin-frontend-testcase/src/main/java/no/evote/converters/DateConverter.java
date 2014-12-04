package no.evote.converters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;


/**
 * Abstract converter for converting to/from Date objects using SimpleDateFormatter. Pattern and error messages should be defined in subclasses.
 */
public abstract class DateConverter implements Converter {
	private String patternVar = null;
	private String fallbackPattern = null;
	private Map<String, String> messageProvider = null;

	public DateConverter(final String patternVar) {
		this.patternVar = patternVar;
	}

	public DateConverter(final String patternVar, final String fallbackPattern) {
		this.patternVar = patternVar;
		this.fallbackPattern = fallbackPattern;
	}

	public void setMessageProvider(final Map<String, String> messageProvider) {
		this.messageProvider = messageProvider;
	}

	protected Map<String, String> getMessageProvider() {
		return new HashMap<>();
	}

	@Override
	public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {
		Date date = null;
		if (value != null && value.trim().length() > 0 && !value.equals("__.__.____")) {
			boolean required = component.getAttributes().get("required").equals("true");

			// Find the correct date pattern
			Application app = context.getApplication();

			String pattern = (String) component.getAttributes().get("pattern"); // Pattern set as attribute on component
			if (pattern == null || pattern.length() == 0) {
				pattern = app.evaluateExpressionGet(context, "#{msgs['" + patternVar + "']}", String.class); // Get pattern from faces parameter
			}

			Locale locale = context.getViewRoot().getLocale();
			DateFormat dateFormat = new SimpleDateFormat(pattern, locale);
			dateFormat.setLenient(false);

			// Try to parse the date, and issue a converter error if it is invalid
			date = new Date();
			try {
				if (fallbackPattern == null) {
					date = dateFormat.parse(value);
				} else {
					date = parseWithFallback(locale, dateFormat, value);
				}
			} catch (ParseException e) {
				if (required || (!required && !"".equals(value))) {
					throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, generateSummaryErrorMessage(component, value, dateFormat),
							generateDetailErrorMessage(component, value, dateFormat)), e);
				}
			}
		}

		return date;
	}

	private Date parseWithFallback(final Locale locale, final DateFormat dateFormat, final String value) throws ParseException {
		Date date;
		try {
			date = dateFormat.parse(value);
		} catch (ParseException e) {
			DateFormat fallbackDateFormat = new SimpleDateFormat(fallbackPattern, locale);
			date = fallbackDateFormat.parse(value);
		}

		return date;
	}

	@Override
	public String getAsString(final FacesContext context, final UIComponent component, final Object value) {
		if (value == null) {
			return "";
		} else if (value instanceof String) {
			return (String) value;
		} else {
			// When converting the date to a string, there should be no need to set the formatter to non-lenient
			Application app = context.getApplication();

			String pattern = (String) component.getAttributes().get("pattern"); // Pattern set as attribute on component
			if (pattern == null || pattern.length() == 0) {
				pattern = app.evaluateExpressionGet(context, "#{msgs['" + patternVar + "']}", String.class); // Get pattern from faces parameter
			}

			Locale locale = context.getViewRoot().getLocale();
			DateFormat dateFormat = new SimpleDateFormat(pattern, locale);
			return dateFormat.format((Date) value);
		}
	}

	public void setPatternVar(final String patternVar) {
		this.patternVar = patternVar;
	}

	public String getPatternVar() {
		return patternVar;
	}

	protected abstract String generateSummaryErrorMessage(final UIComponent component, final String value, DateFormat dateFormat);

	protected abstract String generateDetailErrorMessage(final UIComponent component, final String value, DateFormat dateFormat);
}
