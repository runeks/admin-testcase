package no.valg.eva.admin.common.rbac;

import java.io.Serializable;

import no.evote.util.Treeable;


/**
 * RBAC: Hierarchy of access to securable objects
 */
public class Access implements Serializable, Treeable {

	private AccessType accessType;
	private String path;
	private String name;

	public AccessType getAccessType() {
		return this.accessType;
	}

	public void setAccessType(final AccessType accessType) {
		this.accessType = accessType;
	}

	@Override
	public String getPath() {
		return this.path;
	}

	public void setPath(final String path) {
		this.path = path;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Access) {
			return ((Access) obj).getPath().equals(this.getPath());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getPath().hashCode();
	}

	@Override
	public String toString() {
		return path;
	}

}
