package no.evote.lifecycle;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import no.evote.exception.EvoteInitiationException;
import no.evote.util.Log4jUtil;

import org.apache.log4j.Logger;
import org.opensaml.DefaultBootstrap;
import org.opensaml.xml.ConfigurationException;

public class LifecycleListener implements ServletContextListener {
	private Logger log;

	@Override
	public void contextDestroyed(final ServletContextEvent contextEvent) {
		// To conform with interface
	}

	@Override
	public void contextInitialized(final ServletContextEvent contextEvent) {
		initializeLog4j();
		initializeOpenSaml();
	}

	private void initializeLog4j() {
		Log4jUtil.configure(this.getClass().getClassLoader());
		log = Logger.getLogger(LifecycleListener.class);
	}

	private void initializeOpenSaml() {
		try {
			DefaultBootstrap.bootstrap();
		} catch (ConfigurationException e) {
			log.error(e.getMessage(), e);
			throw new EvoteInitiationException("Unable to initialize OpenSAML", e);
		}
	}

}
