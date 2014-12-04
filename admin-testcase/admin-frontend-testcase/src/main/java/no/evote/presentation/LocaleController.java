package no.evote.presentation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import no.evote.model.Locale;
import no.evote.service.TranslationService;

/**
 * For changing the language used in the user interface.
 */
@Named("LocaleController")
@SessionScoped
public class LocaleController extends BaseController {
	private static final long serialVersionUID = -9176194513242024321L;

	private Map<String, Locale> localeMap;

	@PostConstruct
	public void init() {
		localeMap = new HashMap<String, Locale>();
	}

	public Map<String, Locale> getLocaleMap() {
		return localeMap;
	}

	public String buildLocaleKey(final Locale locale) {
		return "";
	}
}
