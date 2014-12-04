package no.evote.i18n;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import no.evote.constants.EvoteConstants;
import no.evote.model.Locale;
import no.evote.service.TranslationService;
import no.evote.util.EvoteProperties;

import org.apache.log4j.Logger;

/**
 * Implements the actual loading of the translations. It is called by the ResourceBundle factory methods. Extended by classes in their respective projects to be
 * available for their classloaders.
 */
public abstract class BaseResourceBundleControlDatabase extends ResourceBundle.Control implements Serializable {
	private static final int DEFAULT_TIMEOUT = 1000 * 60 * 60;

	private static final Logger LOG = Logger.getLogger(BaseResourceBundleControlDatabase.class);

	private int timeToLive;
	private final TranslationService translationService;

	@Override
	public List<String> getFormats(final String baseName) {
		if (baseName == null) {
			throw new IllegalArgumentException();
		}
		return FORMAT_CLASS;
	}

	public BaseResourceBundleControlDatabase(final TranslationService translationService) {
		this.translationService = translationService;
		String timeToLiveProperty = EvoteProperties.getProperty("no.evote.i18n.ResourceBundleControlDatabase.timeToLive", true);
		if (timeToLiveProperty != null) {
			timeToLive = Integer.parseInt(timeToLiveProperty);
		} else {
			// Default is 1 hour
			timeToLive = DEFAULT_TIMEOUT;
		}
	}

	@Override
	public ResourceBundle newBundle(final String baseName, final java.util.Locale locale, final String format, final ClassLoader loader, final boolean reload)
			throws IllegalAccessException, InstantiationException, IOException {

		if ((baseName == null) || (locale == null) || (format == null) || (loader == null)) {
			throw new IllegalArgumentException();
		}

		String localeString = locale.getLanguage() + EvoteConstants.LOCALE_SEPARATOR + locale.getCountry();
		Locale evoteLocale = getTranslationService().findLocaleById(localeString);
		if (evoteLocale == null) {
			LOG.info("Unable to retrieve locale for " + localeString);
			throw new InstantiationException(String.format("Unable to create bundle for locale \"%s\" (%s)", localeString, locale));
		}
		LOG.info("Retrieved locale for " + localeString);

		Properties properties = new Properties();
		Map<String, String> translations = getTranslationService().getLocaleTexts(null, evoteLocale.getPk());
		LOG.info("Loading global translations, found " + translations.size() + " texts");
		properties.putAll(translations);

		return new DbResourceBundle(evoteLocale, properties, getTimeToLive());
	}

	@Override
	public long getTimeToLive(final String baseName, final java.util.Locale locale) {
		return getTimeToLive();
	}

	/**
	 * Note that the a PMD warning is suppressed here, since it mistakenly reads it as empty
	 * 
	 * @see java.util.ResourceBundle.Control#needsReload(String, java.util.Locale, String, ClassLoader, java.util.ResourceBundle,
	 *      long)
	 */
	@SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract")
	@Override
	public boolean needsReload(final String baseName, final java.util.Locale locale, final String format, final ClassLoader loader,
			final ResourceBundle bundle, final long loadTime) {
		return true;
	}

	public int getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(final int timeToLive) {
		this.timeToLive = timeToLive;
	}

	public TranslationService getTranslationService() {
		return translationService;
	}
}
