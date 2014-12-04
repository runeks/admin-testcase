package no.evote.util;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public final class ServiceLookupUtil {

	private static final Logger LOG = Logger.getLogger(ServiceLookupUtil.class);

	private ServiceLookupUtil() {
	}

	@SuppressWarnings("unchecked")
	public static <T> T lookupService(final Class<T> clazz) {
		T service = null;

		try {
			Properties props = new Properties();
			props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			props.put("jboss.naming.client.ejb.context", true); // TODO frry: verifiser om denne trengs

			InitialContext context = new InitialContext(props);

			String jndiName = remoteJndiNameForService(clazz);

			service = (T) context.lookup(jndiName);
		} catch (NamingException ne) {
			throw new IllegalStateException(ne);
		}

		return service;
	}

	@SuppressWarnings("unchecked")
	public static <T> T lookupServiceForTest(final Class<T> clazz, final Context context) {
		T service = null;

		try {
			String jndiName = remoteJndiNameForService(clazz);
			service = (T) context.lookup(jndiName);
		} catch (NamingException ne) {
			throw new IllegalStateException(ne);
		}

		return service;
	}

	private static <T> String remoteJndiNameForService(Class<T> clazz) {
		String jndiName = "ejb:/admin-backend//" + clazz.getSimpleName() + "!" + clazz.getCanonicalName();
		LOG.debug("Looking up backend service " + clazz.getSimpleName() + " with JNDI name " + jndiName);
		return jndiName;
	}

}
