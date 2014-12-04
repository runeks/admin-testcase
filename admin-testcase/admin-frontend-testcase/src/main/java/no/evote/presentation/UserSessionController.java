package no.evote.presentation;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import no.evote.security.UserData;

import org.apache.log4j.Logger;

@Named
@RequestScoped
public class UserSessionController {
	public static final String OPERATOR_SWITCHED_ROLES_SESSION_ATTR_KEY = "operatorSwitchedRoles";
	private static final String REDIRECT_TO_SELECT_ROLE = "/secure/selectRole.xhtml?faces-redirect=true";
	private static final Logger LOGGER = Logger.getLogger(UserSessionController.class);

	@Inject
	private UserData userData;

	public String changeRole() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

		HttpSession oldSession = existingSession(externalContext);
		oldSession.setAttribute(OPERATOR_SWITCHED_ROLES_SESSION_ATTR_KEY, true);
		oldSession.invalidate();

//		userData.invalidateRoleSelection();

		HttpSession newSession = newSession(externalContext);
		newSession.setAttribute(OPERATOR_SWITCHED_ROLES_SESSION_ATTR_KEY, userData);

		LOGGER.debug("User " + userData.getUid() + " is switching roles...");
		return REDIRECT_TO_SELECT_ROLE;
	}

	private HttpSession existingSession(ExternalContext externalContext) {
		return (HttpSession) externalContext.getSession(false);
	}

	private HttpSession newSession(ExternalContext externalContext) {
		return (HttpSession) externalContext.getSession(true);
	}

}
