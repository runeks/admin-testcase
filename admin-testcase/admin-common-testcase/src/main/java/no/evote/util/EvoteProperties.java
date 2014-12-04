package no.evote.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import no.evote.exception.EvoteException;
import no.evote.exception.EvoteInitiationException;

public final class EvoteProperties {
	private static final String EVOTE_PROPERTIES = "EVOTE_PROPERTIES";

	private static String propertyFilePath;

	private static Properties properties = new Properties();

	private EvoteProperties() {

	}

	static {
		propertyFilePath = System.getenv(EVOTE_PROPERTIES);
		if (propertyFilePath == null) {
			propertyFilePath = System.getProperty(EVOTE_PROPERTIES);
		}

		try {
			if (propertyFilePath == null) {
				throw new IllegalArgumentException("EVOTE_PROPERTIES is not defined");
			} else {
				FileInputStream fi = new FileInputStream(propertyFilePath);
				properties.load(fi);
				fi.close();
			}
		} catch (FileNotFoundException e) {
			throw new EvoteInitiationException("Failed to load evote properties", e);
		} catch (IOException e) {
			throw new EvoteInitiationException("Failed to load evote properties", e);
		}
	}

	public static String getProperty(final String key) {
		return getProperty(key, false);
	}

	public static String getProperty(final String key, final String defaultValue) {
		String value = getProperty(key, true);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	public static String getProperty(final String key, final boolean allowNull) {
		String value = (String) properties.get(key);
		if (value == null && !allowNull) {
			throw new EvoteException("Property missing in evote.properties: " + key);
		}
		return value;
	}

}
