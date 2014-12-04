package no.evote.presentation.validation;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import no.evote.validation.FoedselsNummerValidator;

/**
 */

@FacesValidator("no.evote.presentation.validation.FodselsnummerFacesValidator")
public class FodselsnummerFacesValidator implements Serializable, Validator {

	@Override
	public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) {
		if (!FoedselsNummerValidator.isFoedselsNummerValid(o.toString())) {
			FacesMessage msg =
					new FacesMessage("@validation.invalid.id");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
	}

}
