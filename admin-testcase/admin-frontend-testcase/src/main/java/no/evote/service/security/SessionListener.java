package no.evote.service.security;

import java.util.Date;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import no.evote.presentation.UserSessionController;
import no.evote.security.UserData;
import no.evote.service.AuditLoggerService;

import org.apache.log4j.Logger;

public class SessionListener implements HttpSessionListener {

	private static final Logger LOGGER = Logger.getLogger(SessionListener.class);

	@Inject
	private Instance<UserData> userDataInstance;


	@Override
	public void sessionCreated(final HttpSessionEvent httpSessionEvent) {
		HttpSession session = httpSessionEvent.getSession();
		session.setAttribute("time", new Date().getTime());
		LOGGER.debug("session created med attributter: " + session.getAttributeNames());
	}

	@Override
	public void sessionDestroyed(final HttpSessionEvent httpSessionEvent) {
		UserData userData = getUserDataIfPossible();
		if (userData != null) {
			LOGGER.debug("session Destroyed - for bruker id " + userData.getUid());
		} else {
			LOGGER.debug("session Destroyed - userData is null");
		}
		if (!sessionWasDestroyedBecauseOperatorIsSwitchingRoles(httpSessionEvent)
				&& userData != null) {
		}
	}

	private UserData getUserDataIfPossible() {
		try {
			return userDataInstance.get();
		} catch (RuntimeException e) {
			LOGGER.debug("Unable to get UserData object while destroying context");
		}
		return null;
	}

	private boolean sessionWasDestroyedBecauseOperatorIsSwitchingRoles(HttpSessionEvent sessionEvent) {
		return sessionEvent.getSession().getAttribute(UserSessionController.OPERATOR_SWITCHED_ROLES_SESSION_ATTR_KEY) != null;
	}

}
