package no.evote.validation;

import java.io.Serializable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PostalCodeValidator implements ConstraintValidator<PostalCode, String>, Serializable {

	@Override
	public void initialize(final PostalCode constraint) {
		// Intentionally empty
	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext constraintValidatorContext) {
		if (value == null) {
			return false;
		}

		// CSOFF: MagicNumber
		if (value.length() != 4) {
			// CSON: MagicNumber
			return false;
		}
		return true;
	}
}
