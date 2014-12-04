package no.evote.presentation.validation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.ValidatorException;

/**
 * Validates that at least one checkbox is checked, within a given grouping. Requires two parameters: grouping and count - see
 * <code>createPollingPlace.xhtml</code> for an example.
 */
@FacesValidator("atLeastOneValidator")
public class AtLeastOneCheckboxValidator implements javax.faces.validator.Validator, Serializable {
	private final Map<String, Boolean> checked = new HashMap<String, Boolean>();
	private final Map<String, Integer> counter = new HashMap<String, Integer>();

	@Override
	public void validate(final FacesContext context, final UIComponent component, final Object value) {
		String grouping = (String) component.getAttributes().get("grouping");
		Integer counter = this.counter.get(grouping);
		if (counter == null) {
			counter = Integer.valueOf(0);
		}
		this.counter.put(grouping, counter + 1);

		Boolean checked = this.checked.get(grouping);
		if (checked == null) {
			checked = Boolean.FALSE;
			this.checked.put(grouping, checked);
		}

		if ((Boolean) value) {
			checked = true;
			this.checked.put(grouping, checked);
		}

		if (counter.equals(component.getAttributes().get("count")) && !checked) {

			FacesMessage message = new FacesMessage();
			message.setSeverity(FacesMessage.SEVERITY_FATAL);
			message.setSummary("@area.polling_place.opening_hours.required");
			throw new ValidatorException(message);
		}
	}
}
