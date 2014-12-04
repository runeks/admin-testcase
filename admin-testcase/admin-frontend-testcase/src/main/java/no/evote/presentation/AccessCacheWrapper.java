package no.evote.presentation;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import no.evote.security.UserData;

/**
 * Simplifies lookups to UserService.hasAccess, in particular for tests.
 */
@SessionScoped
public class AccessCacheWrapper implements Serializable {

	@Inject
	private UserData userData;

	/**
	 * Check access on a comma separated list of securable objects
	 */
	public boolean hasAccess(final String secObj) {
		return true;
	}

	/**
	 * Check access on a string array of securable objects
	 */
	public boolean hasAccess(final String[] secObjs) {
		return true;
	}
}
