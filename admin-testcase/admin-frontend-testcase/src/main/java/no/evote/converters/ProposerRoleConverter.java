package no.evote.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import no.evote.model.ProposerRole;
import no.evote.presentation.util.FacesUtil;
import no.evote.service.ProposerService;
import no.evote.util.ServiceLookupUtil;

@FacesConverter(value = "proposerRoleConverter")
//@FacesConverter(value = "proposerRoleConverter", forClass = no.evote.model.ProposerRole.class)
public class ProposerRoleConverter implements Converter {

	private final ProposerService proposerService = ServiceLookupUtil.lookupService(ProposerService.class);

	@Override
	public Object getAsObject(final FacesContext fContext, final UIComponent component, final String value) {
		if (value == null || value.length() == 0) {
			return null;
		}

		return proposerService.findProposerRoleByPk(FacesUtil.getUserData(), Long.parseLong(value));
	}

	@Override
	public String getAsString(final FacesContext context, final UIComponent component, final Object value) {

		if (value == null) {
			return "";
		}
		if (value instanceof ProposerRole) {
			final ProposerRole proposerRole = (ProposerRole) value;
			return proposerRole.getPk().toString();
		} else {
			// throw new IllegalArgumentException("object " + value + " is of type " + value.getClass().getName() + "; expected type: " + ProposerRole.class);
			return "";
		}
	}

}
