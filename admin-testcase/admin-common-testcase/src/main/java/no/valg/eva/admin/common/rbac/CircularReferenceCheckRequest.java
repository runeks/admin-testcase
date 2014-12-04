package no.valg.eva.admin.common.rbac;

import java.io.Serializable;

/**
 * Request object for performing circular reference checks
 */
public class CircularReferenceCheckRequest implements Serializable {

	private final Role role;
	private final long newIncludedRolePk;

	public CircularReferenceCheckRequest(final Role currentRole, final long newIncludedRolePk) {
		this.role = currentRole;
		this.newIncludedRolePk = newIncludedRolePk;
	}

	public Role getRole() {
		return role;
	}

	public long getNewIncludedRolePk() {
		return newIncludedRolePk;
	}
}
