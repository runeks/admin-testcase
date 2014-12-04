package no.evote.i18n;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import no.evote.exception.EvoteException;

import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;

public class ValidationResourceBundleLocator implements ResourceBundleLocator, Serializable {
	private static final long serialVersionUID = -2854349420095568493L;
	private transient ResourceBundleManager resourceBundleManager;

	@Override
	@SuppressWarnings("rawtypes")
	public ResourceBundle getResourceBundle(final Locale locale) {

		if (resourceBundleManager == null) {
			try {
				BeanManager beanManager = (BeanManager) new InitialContext().lookup("java:comp/BeanManager");
				Bean<?> bean = beanManager.getBeans(ResourceBundleManager.class).iterator().next();
				CreationalContext ctx = beanManager.createCreationalContext(bean);
				resourceBundleManager = (ResourceBundleManager) beanManager.getReference(bean, ResourceBundleManager.class, ctx);
			} catch (NamingException ne) {
				throw new EvoteException("Error looking up resource bundle manager: ", ne);
			}
		}

		return resourceBundleManager.getBundle(locale);
	}
}
