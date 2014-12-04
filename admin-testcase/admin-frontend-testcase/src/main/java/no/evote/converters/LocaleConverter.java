package no.evote.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import no.evote.model.Locale;
import no.evote.presentation.LocaleController;

@FacesConverter(value = "localeConverter")
//@FacesConverter(value = "localeConverter", forClass = no.evote.model.Locale.class)
public class LocaleConverter implements Converter {

	@Inject
	private LocaleController localeController;

	@Override
	public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {
		if (value == null || value.trim().length() == 0) {
			return null;
		}

		setLocaleController(context);
		return localeController.getLocaleMap().get(value);
	}

	@Override
	public String getAsString(final FacesContext context, final UIComponent component, final Object value) {

		if (value == null) {
			return null;
		}
		if (value instanceof Locale) {
			setLocaleController(context);
			return localeController.buildLocaleKey((Locale) value);
		} else {
			throw new IllegalArgumentException("object " + value + " is of type " + value.getClass().getName() + "; expected type: no.evote.model.Locale");
		}
	}

	private void setLocaleController(final FacesContext context) {
		if (localeController == null) {
			localeController = context.getApplication().evaluateExpressionGet(context, "#{LocaleController}", LocaleController.class);
		}
	}

}
