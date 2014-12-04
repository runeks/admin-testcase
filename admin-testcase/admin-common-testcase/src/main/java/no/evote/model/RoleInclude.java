package no.evote.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import no.evote.logging.AuditLogUtil;

/**
 * RBAC: Roles included per role
 */
@Entity
@Table(name = "role_include")
@AttributeOverride(name = "pk", column = @Column(name = "role_include_pk"))
public class RoleInclude extends VersionedEntity implements java.io.Serializable {

	private Role roleByRolePk;
	private Role roleByIncludedRolePk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_pk", nullable = false)
	public Role getRoleByRolePk() {
		return this.roleByRolePk;
	}

	public void setRoleByRolePk(final Role roleByRolePk) {
		this.roleByRolePk = roleByRolePk;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "included_role_pk", nullable = false)
	public Role getRoleByIncludedRolePk() {
		return this.roleByIncludedRolePk;
	}

	public void setRoleByIncludedRolePk(final Role roleByIncludedRolePk) {
		this.roleByIncludedRolePk = roleByIncludedRolePk;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(roleByRolePk, roleByIncludedRolePk);
	}
}
