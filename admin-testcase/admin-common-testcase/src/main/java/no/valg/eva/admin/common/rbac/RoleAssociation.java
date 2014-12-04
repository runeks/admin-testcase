package no.valg.eva.admin.common.rbac;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;

import no.valg.eva.admin.common.Area;

public class RoleAssociation implements Serializable {
	private RoleItem role;
	private Area area;

	public RoleAssociation(RoleItem role, Area area) {
		requireNonNull(role);
		requireNonNull(area);

		this.role = role;
		this.area = area;
	}

	public RoleItem getRole() {
		return role;
	}

	public Area getArea() {
		return area;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof RoleAssociation)) {
			return false;
		}

		RoleAssociation that = (RoleAssociation) o;

		if (!area.equals(that.area)) {
			return false;
		}
		if (!role.equals(that.role)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = role.hashCode();
		result = 31 * result + area.hashCode();
		return result;
	}
}
