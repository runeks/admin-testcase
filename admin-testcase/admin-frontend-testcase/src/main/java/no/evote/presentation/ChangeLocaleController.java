package no.evote.presentation;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import no.evote.model.Locale;
import no.evote.security.UserData;
import no.evote.service.TranslationService;

/**
 * Controller for changing locale, used by changeLocale.xhtml.
 */
@Named
@RequestScoped
public class ChangeLocaleController {


	@Inject
	private UserData userData;

	private List<Locale> locales;

	@PostConstruct
	public void init() {
		locales = new ArrayList<Locale>();
	}

	public Locale getCurrentLocale() {
		return null;
	}

	public void setCurrentLocale(final Locale locale) {

	}

	public List<Locale> getLocales() {
		return locales;
	}
}
