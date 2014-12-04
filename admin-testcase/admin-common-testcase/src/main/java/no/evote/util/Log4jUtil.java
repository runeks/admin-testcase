package no.evote.util;

import java.net.URL;

import no.evote.exception.EvoteException;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public final class Log4jUtil {
	private Log4jUtil() {
		// Intentionally left empty
	}

	public static void configure(final ClassLoader classLoader) {
		URL log4JConfig = classLoader.getResource("log4j.xml");
		if (log4JConfig == null) {
			throw new EvoteException("Unable to find log4j.xml on classpath");
		}

		DOMConfigurator.configure(log4JConfig);
        Logger.getLogger(Log4jUtil.class).info("Configured Log4J from " + log4JConfig);
    }
}
