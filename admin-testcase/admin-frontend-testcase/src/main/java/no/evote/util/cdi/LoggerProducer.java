package no.evote.util.cdi;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Singleton;

import org.apache.log4j.Logger;

/**
 * Producer for injecting Log4J loggers. Usage:
 *
 * <pre>
 * class Example {
 * 	&#064;Inject
 * 	private Logger log;
 *
 * 	public void testIt() {
 * 		log.info(&quot;it works!&quot;);
 * 	}
 * }
 * </pre>
 *
 * Not that the field has to be <code>transient</code> if the class implements serializable.
 */
@Singleton
public class LoggerProducer {

	/**
	 * Produces a logger instance with the name set to the class where it is to be injected
	 * @param injectionPoint
	 *            Where the Logger is to be injected
	 * @return A Logger instance with the name set to the class where it is to be injected
	 */
	@Produces
	public Logger createLogger(final InjectionPoint injectionPoint) {
		return Logger.getLogger(injectionPoint.getMember().getDeclaringClass());
	}
}
