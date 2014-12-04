package no.valg.eva.admin.common.rbac;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;

/**
 * Value object with basic attributes for Role
 */
public class RoleItem implements Serializable {
	
	private final String roleId;
	private final String roleName;

	public RoleItem(String roleId, String roleName) {
		this.roleId = requireNonNull(roleId);
		this.roleName = requireNonNull(roleName);
	}

	public String getRoleId() {
		return roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof RoleItem)) {
			return false;
		}

		RoleItem roleItem = (RoleItem) o;

		if (!roleId.equals(roleItem.roleId)) {
			return false;
		}
		if (!roleName.equals(roleItem.roleName)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = roleId.hashCode();
		result = 31 * result + roleName.hashCode();
		return result;
	}
}
