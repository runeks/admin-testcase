package no.evote.presentation.util;

import java.io.Serializable;
import java.util.Comparator;

import no.evote.model.Locale;

public class LocaleComparator implements Comparator<Locale>, Serializable {


	@Override
	public int compare(final Locale locale1, final Locale locale2) {
		return 1;
	}

}
