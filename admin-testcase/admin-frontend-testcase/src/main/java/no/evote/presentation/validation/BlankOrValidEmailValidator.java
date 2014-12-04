package no.evote.presentation.validation;

import static no.evote.constants.EvoteConstants.VALID_EMAIL_REGEXP;

import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang3.StringUtils;

/**
 * Validates email format, but accepts empty value
 */
@FacesValidator("no.evote.presentation.validation.BlankOrValidEmailValidator")
public class BlankOrValidEmailValidator implements Validator {
	private static final Pattern VALID_EMAIL_PATTERN = Pattern.compile(VALID_EMAIL_REGEXP);


	@Override
	public void validate(FacesContext context, UIComponent component, Object value) {
		String valueToValidate = value.toString();
		if (!StringUtils.isBlank(valueToValidate) && !VALID_EMAIL_PATTERN.matcher(valueToValidate).matches()) {
			FacesMessage msg =
					new FacesMessage("@validation.email");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
	}
}
