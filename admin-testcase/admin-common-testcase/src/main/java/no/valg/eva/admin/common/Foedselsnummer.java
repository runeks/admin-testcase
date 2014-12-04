package no.valg.eva.admin.common;

import org.joda.time.LocalDate;

// CSOFF: MagicNumber
public final class Foedselsnummer extends PersonId {

	public Foedselsnummer(String foedselsnummer) {
		super(foedselsnummer);
	}

	public LocalDate dateOfBirth() {
		int individsifre = Integer.parseInt(id.substring(6, 9));
		int day = Integer.parseInt(id.substring(0, 2));
		int birthday;
		if (day > 40) {
			// D-nummer
			birthday = day - 40;
		} else {
			birthday = day;
		}
		int month = Integer.parseInt(id.substring(2, 4));
		int birthmonth;
		if (month > 40) {
			// H-nummer
			birthmonth = month - 40;
		} else {
			birthmonth = month;
		}
		int year = Integer.parseInt(id.substring(4, 6));
		int century;
		if (individsifre >= 0 && individsifre <= 499) {
			// 000-499 is 1900-1999
			century = 19;
		} else if (individsifre >= 500 && individsifre <= 749
				&& year >= 55) {
			// 500-749 is 1855-1899
			century = 18;
		} else if (individsifre >= 500 && individsifre <= 999
				&& year <= 39) {
			// 500-999 is 2000-2039
			century = 20;
		} else if (individsifre >= 900 && individsifre <= 999
				&& year >= 40) {
			// 900-999 is 1940–1999
			century = 19;
		} else {
			throw new IllegalStateException(
					"cannot convert the national identification number (" + id
							+ ") to a birth date");
		}
		year = century * 100 + year;
		return new LocalDate(year, birthmonth, birthday);
	}
}
// CSON: MagicNumber
