package no.evote.converters;

import static java.lang.Long.parseLong;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import no.evote.model.Party;
import no.evote.presentation.util.FacesUtil;
import no.evote.service.PartyService;
import no.evote.util.ServiceLookupUtil;

@FacesConverter(value = "partyConverter")
//@FacesConverter(value = "partyConverter", forClass = no.evote.model.Party.class)
public class PartyConverter implements Converter {

	private final PartyService partyService = ServiceLookupUtil.lookupService(PartyService.class);

	@Override
	public Object getAsObject(final FacesContext fContext, final UIComponent component, final String value) {
		if (value == null || value.length() == 0) {
			return null;
		}

		final long pk = parseLong(value);
		return partyService.findByPk(FacesUtil.getUserData(), pk);
	}

	@Override
	public String getAsString(final FacesContext context, final UIComponent component, final Object value) {

		if (value == null) {
			return null;
		}
		if (value instanceof Party) {
			final Party party = (Party) value;
			return party.getPk().toString();
		} else {
			throw new IllegalArgumentException("object " + value + " is of type " + value.getClass().getName() + "; expected type: " + Party.class);
		}
	}

}
