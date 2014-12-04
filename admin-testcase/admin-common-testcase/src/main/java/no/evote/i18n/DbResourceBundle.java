package no.evote.i18n;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import no.evote.model.Locale;

/**
 * ResourceBundle implementation that also keeps track of the no.evote.model.Locale that it is associated with, as well as the time it was created and if it has
 * timed out and needs to be reloaded
 */
public class DbResourceBundle extends ResourceBundle implements Serializable {

	private volatile Properties properties;
	private volatile long timestamp;
	private final int timeToLive;
	private final Locale evoteLocale;

	public DbResourceBundle(final Locale evoteLocale, final Properties inProperties, final int timeToLive) {
		super();
		this.properties = inProperties;
		this.timeToLive = timeToLive;
		this.evoteLocale = evoteLocale;
		resetTimestamp();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Enumeration<String> getKeys() {
		return properties != null ? ((Enumeration<String>) properties.propertyNames()) : null;
	}

	@Override
	protected Object handleGetObject(final String key) {
		return properties.getProperty(key);
	}

	public boolean isTimedOut() {
		return timeToLive + timestamp < System.currentTimeMillis();
	}

	public void updateTranslations(final Map<String, String> translations) {
		Properties newProperties = new Properties();
		newProperties.putAll(translations);
		properties = newProperties;
		resetTimestamp();
	}

	public Locale getEvoteLocale() {
		return evoteLocale;
	}

	private void resetTimestamp() {
		timestamp = System.currentTimeMillis();
	}
}
