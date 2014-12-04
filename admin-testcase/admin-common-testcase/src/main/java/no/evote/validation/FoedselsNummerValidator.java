package no.evote.validation;

import java.io.Serializable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

// CSOFF: MagicNumber
public class FoedselsNummerValidator implements ConstraintValidator<FoedselsNummer, String>, Serializable {

	public void initialize(final FoedselsNummer constraint) {
		// Do nothing. Exists to satisfy ConstraintValidator interface.
	}

	public boolean isValid(final String value, final ConstraintValidatorContext constraintValidatorContext) {
		return isFoedselsNummerValid(value);
	}

	public static boolean isFoedselsNummerValid(final String value) {
		int fnrTwoLastDigits;
		int checkSumFromMethod;

		if (value == null || value.length() < 11) {
			return false;
		}

		try {
			fnrTwoLastDigits = Integer.parseInt(value.substring(9, 11));
			checkSumFromMethod = controllDigit(value);
		} catch (NumberFormatException nfe) {
			return false;
		}
		if (fnrTwoLastDigits == checkSumFromMethod) {
			return checkIndividNumber(value);
		} else {
			return false;
		}
	}

	private static int controllDigit(final String sFnr) {
		int digit1 = 0;
		int digit2 = 0;
		int sum = 0;
		int[] weighVector = { 3, 7, 6, 1, 8, 9, 4, 5, 2 };
		int[] fnrFirstNineDigitsAsInt = new int[9];
		String[] fnrFirstNineDigitsAsString = sFnr.split("");

		for (int i = 0; i < 9; i++) {
			fnrFirstNineDigitsAsInt[i] = Integer.parseInt(fnrFirstNineDigitsAsString[i + 1]);
		}

		for (int i = 0; i < 9; i++) {
			sum += weighVector[i] * fnrFirstNineDigitsAsInt[i];
		}

		int sumCheck = sum % 11;

		if (sumCheck != 0) {
			digit1 = 11 - sum % 11;
		} else {
			digit1 = 0;
		}

		digit2 = modus11(sFnr.substring(0, 9) + digit1);

		StringBuffer checkSumFromMethod = new StringBuffer(String.valueOf(digit1));
		checkSumFromMethod.append(String.valueOf(digit2));

		return Integer.parseInt(checkSumFromMethod.toString());
	}

	private static int modus11(final String sFnrTenFirstDigits) {
		int weight = 2;
		int sum = 0;
		int siff = 0;

		for (int i = 9; i >= 0; i--) {
			sum += weight * Integer.parseInt(sFnrTenFirstDigits.substring(i, i + 1));
			weight = (weight - 1) % 6 + 2;
		}
		int sumCheck = sum % 11;

		if (sumCheck != 0) {
			siff = 11 - sum % 11;
		} else {
			siff = 0;
		}
		return siff;
	}

	private static boolean checkIndividNumber(final String fnr) {
		if (fnr.length() == 11) {
			String year = fnr.substring(4, 6);
			Integer yy = Integer.parseInt(year);
			String individnr = fnr.substring(6, 9);
			Integer iNr = Integer.parseInt(individnr);

			if (iNr < 500) {
				// 1900 - 1999
				return true;
			} else if (iNr >= 500 && iNr < 750 && yy >= 55) {
				// 1855 - 1899
				return true;
			} else if (iNr >= 500 && yy < 40) {
				// 2000 - 2039
				return true;
			} else if (iNr >= 900 && yy >= 40) {
				// 1940 - 1999
				return true;
			}
		}
		return false;
	}
}
// CSON: MagicNumber
