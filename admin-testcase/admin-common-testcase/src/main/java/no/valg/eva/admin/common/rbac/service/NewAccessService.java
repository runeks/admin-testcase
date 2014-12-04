package no.valg.eva.admin.common.rbac.service;

import javax.ejb.Remote;

import no.evote.security.AccessCache;
import no.evote.security.UserData;

/**
 * Defines services for rbac.
 */
@Remote
public interface NewAccessService {

	/**
	 * Finds accesses for roles
	 * 
	 * @param userData
	 *            contains a set of roles
	 * @return cache with set of accesses
	 */
	AccessCache findAccessCacheFor(UserData userData);

}
