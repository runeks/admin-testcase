package no.evote.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import no.evote.model.Candidate;
import no.evote.service.CandidateService;
import no.evote.util.ServiceLookupUtil;

@FacesConverter(value = "candidateConverter")
//@FacesConverter(value = "candidateConverter", forClass = no.evote.model.Candidate.class)
public class CandidateConverter implements Converter {

	private final CandidateService candidateService = ServiceLookupUtil.lookupService(CandidateService.class);

	@Override
	public Object getAsObject(final FacesContext fContext, final UIComponent component, final String value) {
		if (value == null || value.length() == 0) {
			return null;
		}

		return candidateService.findCandidateByName(value);
	}

	@Override
	public String getAsString(final FacesContext context, final UIComponent component, final Object value) {

		if (value == null) {
			return null;
		}
		if (value instanceof Candidate) {
			return value.toString();
		} else {
			throw new IllegalArgumentException("object " + value + " is of type " + value.getClass().getName() + "; expected type: no.evote.model.Candidate");
		}
	}

}
