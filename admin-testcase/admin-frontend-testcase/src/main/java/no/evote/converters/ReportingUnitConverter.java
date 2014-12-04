package no.evote.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import no.evote.model.ReportingUnit;
import no.evote.service.ReportingUnitService;
import no.evote.util.ServiceLookupUtil;

@FacesConverter(value = "reportingUnitConverters")
//@FacesConverter(value = "reportingUnitConverters", forClass = no.evote.model.ReportingUnit.class)
public class ReportingUnitConverter implements Converter {

	private final ReportingUnitService reportingUnitService = ServiceLookupUtil.lookupService(ReportingUnitService.class);

	@Override
	public Object getAsObject(final FacesContext fContext, final UIComponent component, final String value) {
		if (value == null || value.length() == 0) {
			return null;
		}

		Long pk = Long.valueOf(value.split("#")[1]);
		return reportingUnitService.findByPk(pk);
	}

	@Override
	public String getAsString(final FacesContext context, final UIComponent component, final Object value) {

		if (value == null) {
			return null;
		}
		if (value instanceof ReportingUnit) {
			return value.toString();
		} else {
			throw new IllegalArgumentException("object " + value + " is of type " + value.getClass().getName()
					+ "; expected type: no.evote.model.ReportingUnit");
		}
	}

}
