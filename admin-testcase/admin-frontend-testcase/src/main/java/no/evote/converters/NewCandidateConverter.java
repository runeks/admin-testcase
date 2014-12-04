package no.evote.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import no.evote.util.Wrapper;
import no.valg.eva.admin.common.counting.model.modifiedballots.Candidate;
import no.valg.eva.admin.common.counting.model.modifiedballots.CandidateRef;

@FacesConverter(value = "newCandidateConverter")
public class NewCandidateConverter implements Converter {
	@Override
	public Object getAsObject(final FacesContext fContext, final UIComponent component, final String value) {
		if (value == null || value.length() == 0) {
			return null;
		}
		return new Candidate(new CandidateRef(Long.parseLong(value)));
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getAsString(final FacesContext context, final UIComponent component, final Object value) {

		// TODO Denne metoden kan mest sannsynlig forenkles til kun å håndtere Wrapper-objekter
		if (value == null || (value instanceof Wrapper) && ((Wrapper<Candidate>) value).getValue() == null) {
			return null;
		}
		if (value instanceof Wrapper) {
			return "" + ((Wrapper<Candidate>) value).getValue().getCandidateRef().getPk();
		} else {
			return "" + ((Candidate) value).getCandidateRef().getPk();
		}
	}

}
