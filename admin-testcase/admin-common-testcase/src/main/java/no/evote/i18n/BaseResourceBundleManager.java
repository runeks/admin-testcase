package no.evote.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle.Control;

import no.evote.exception.EvoteException;
import no.evote.service.TranslationService;

import org.apache.log4j.Logger;

/**
 * Tracks resource bundles at the application level. All instances of MessageProvider asks this for bundles, and when changes to text is done, this also reloads
 * the bundles.
 */
public abstract class BaseResourceBundleManager {
	public static final String EVOTE_BUNDLE_NAME = "no.evote.i18n.DbResourceBundle";
	private static Logger log = Logger.getLogger(BaseResourceBundleManager.class);

	private final Map<String, DbResourceBundle> loadedBundles = new HashMap<String, DbResourceBundle>();

	/**
	 * Retrieves a bundle either from the cache or the database (via the JVM)
	 */
	public DbResourceBundle getBundle(final Locale locale) {
		DbResourceBundle bundle = loadedBundles.get(getCacheKey(locale));

		if (bundle == null || !bundle.getLocale().equals(locale)) {
			log.debug("creating new bundle for " + locale);
			Control control = createResourceBundleControlDatabase();
			try {
				bundle = (DbResourceBundle) DbResourceBundle.getBundle(EVOTE_BUNDLE_NAME, locale, getCurrentLoader(this), control);
			} catch (MissingResourceException e) {
				String msg = "No resource found with name:" + EVOTE_BUNDLE_NAME + " with locale:" + getCacheKey(locale) + ". Clearing cache and retrying..";
				log.info(msg, e);

				DbResourceBundle.clearCache(getCurrentLoader(this));
				try {
					// Fyller opp cachen med bokmål og nynorsk
					log.info("Reloading cache with bokmal and nynorsk");
					DbResourceBundle bokmal = (DbResourceBundle) DbResourceBundle.getBundle(EVOTE_BUNDLE_NAME, new Locale("nb", "NO"),
							getCurrentLoader(this), control);
					DbResourceBundle nynorsk = (DbResourceBundle) DbResourceBundle.getBundle(EVOTE_BUNDLE_NAME, new Locale("nn", "NO"),
							getCurrentLoader(this), control);

					bundle = (DbResourceBundle) DbResourceBundle.getBundle(EVOTE_BUNDLE_NAME, locale, getCurrentLoader(this), control);
				} catch (MissingResourceException e2) {
					String msg2 = "Even after clearing cache, no resource found with name:" + EVOTE_BUNDLE_NAME + " with locale:" + getCacheKey(locale);
					log.error(msg2, e2);
					throw new EvoteException(msg, e2);
				}
			}

			loadedBundles.put(getCacheKey(locale), bundle);
		}
		return bundle;
	}

	private ClassLoader getCurrentLoader(final Object fallbackClass) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (loader == null) {
			loader = fallbackClass.getClass().getClassLoader();
		}
		return loader;
	}

	private String getCacheKey(final Locale locale) {
		return locale.toString();
	}

	abstract TranslationService getTranslationService();

	abstract BaseResourceBundleControlDatabase createResourceBundleControlDatabase();

	/**
	 * Loop through all known resource bundles, load the translations for each bundle and replace them
	 */
	public void reloadBundles() {
		for (DbResourceBundle bundle : loadedBundles.values()) {
			Map<String, String> translations = getTranslationService().getLocaleTexts(null, bundle.getEvoteLocale().getPk());
			bundle.updateTranslations(translations);
		}
	}

	/**
	 * Find a bundle in the bundle cache and replace the contents with fresh translations from the database.
	 * @param userLocale
	 *            The locale for which the bundle should be reloaded
	 */
	public DbResourceBundle reloadBundle(final Locale userLocale) {
		DbResourceBundle bundle = getBundle(userLocale);
		Map<String, String> translations = getTranslationService().getLocaleTexts(null, bundle.getEvoteLocale().getPk());
		bundle.updateTranslations(translations);

		return bundle;
	}
}
