package no.evote.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import no.evote.model.VoteCountCategory;
import no.evote.presentation.util.FacesUtil;
import no.evote.service.VoteCountCategoryService;
import no.evote.util.ServiceLookupUtil;

@FacesConverter(value = "voteCountCategoryConverter")
//@FacesConverter(value = "voteCountCategoryConverter", forClass = no.evote.model.VoteCountCategory.class)
public class VoteCountCategoryConverter implements Converter {

	private final VoteCountCategoryService voteCountCategoryService = ServiceLookupUtil.lookupService(VoteCountCategoryService.class);

	@Override
	public Object getAsObject(final FacesContext fContext, final UIComponent component, final String value) {
		if (value == null || value.length() == 0) {
			return null;
		}

		long pk = Long.parseLong(value.split("#")[1]);
		return voteCountCategoryService.findByPk(FacesUtil.getUserData(), pk);
	}

	@Override
	public String getAsString(final FacesContext context, final UIComponent component, final Object value) {

		if (value == null) {
			return null;
		}
		if (value instanceof VoteCountCategory) {
			return value.toString();
		} else {
			throw new IllegalArgumentException("object " + value + " is of type " + value.getClass().getName()
					+ "; expected type: no.evote.model.ReportingUnit");
		}
	}

}
