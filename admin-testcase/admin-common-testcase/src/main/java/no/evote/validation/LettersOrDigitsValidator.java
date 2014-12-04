package no.evote.validation;

import java.io.Serializable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LettersOrDigitsValidator implements ConstraintValidator<LettersOrDigits, String>, Serializable {

	private String extraChars;

	@Override
	public void initialize(final LettersOrDigits constraintAnnotation) {
		extraChars = constraintAnnotation.extraChars();
	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		if (value == null || value.length() == 0) {
			return true;
		}

		for (int i = 0; i < value.length(); i++) {
			Character currentChar = value.charAt(i);

			if (Character.isLetterOrDigit(currentChar)) {
				continue;
			} else {
				if (extraChars.indexOf(currentChar) > -1) {
					continue;
				}
			}
			return false;
		}

		return true;
	}

}
