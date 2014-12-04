package no.evote.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import no.evote.model.Municipality;
import no.evote.service.MunicipalityService;
import no.evote.util.ServiceLookupUtil;

@FacesConverter(value = "municipalityConverter")
//@FacesConverter(value = "municipalityConverter", forClass = no.evote.model.Municipality.class)
public class MunicipalityConverter implements Converter {

	private final MunicipalityService municipalityService = ServiceLookupUtil.lookupService(MunicipalityService.class);

	@Override
	public Object getAsObject(final FacesContext fContext, final UIComponent component, final String value) {
		if (value == null || value.length() == 0) {
			return null;
		}

		return municipalityService.findByPk(Long.parseLong(value));
	}

	@Override
	public String getAsString(final FacesContext context, final UIComponent component, final Object value) {

		if (value == null) {
			return null;
		}
		if (value instanceof Municipality) {
			final Municipality municipality = (Municipality) value;
			return municipality.getPk().toString();
		} else {
			throw new IllegalArgumentException("object " + value + " is of type " + value.getClass().getName() + "; expected type: " + Municipality.class);
		}
	}

}
