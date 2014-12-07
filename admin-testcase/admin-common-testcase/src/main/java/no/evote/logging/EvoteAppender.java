package no.evote.logging;

import org.apache.log4j.DailyRollingFileAppender;

/**
 * Reads and uses logging filename from evote.properties if set, or falls back to a nice default.
 */
public class EvoteAppender extends DailyRollingFileAppender {

	private static final String DEFAULT_LOG_FILENAME = "../logs/notsecure.log";

	public EvoteAppender() {
		super();

		this.setFile(DEFAULT_LOG_FILENAME);
	}
}
