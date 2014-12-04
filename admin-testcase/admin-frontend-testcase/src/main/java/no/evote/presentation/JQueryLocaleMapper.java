package no.evote.presentation;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * Locale strings for Norwegian bokmål and nynorsk are currently not supported in the jQuery datepicker, so we need to convert them to "no", which is supported.
 * Other locales may also need to be converted this way.
 */
@Named("JQueryLocaleMapper")
@ApplicationScoped
public class JQueryLocaleMapper {

	private static final Map<String, String> LOCALE_MAP = new HashMap<String, String>();
	static {
		LOCALE_MAP.put("nb-NO", "no");
		LOCALE_MAP.put("nn-NO", "no");
	};

	public String map(final String locale) {
		String mappedValue = null;
		if (LOCALE_MAP.containsKey(locale)) {
			mappedValue = LOCALE_MAP.get(locale);
		} else {
			mappedValue = locale;
		}
		return mappedValue;
	}
}
