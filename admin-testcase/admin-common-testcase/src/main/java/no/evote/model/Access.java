package no.evote.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import no.evote.util.Treeable;
import no.evote.validation.StringNotNullEmptyOrBlanks;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * RBAC: Hierarchy of access to securable objects
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "access", uniqueConstraints = @UniqueConstraint(columnNames = "access_path"))
@AttributeOverride(name = "pk", column = @Column(name = "access_pk"))
@NamedQueries({
		@NamedQuery(name = "Access.findAccessById", query = "SELECT a FROM Access a WHERE a.path = :accessId"),
		@NamedQuery(name = "Access.findAccessesAtLevelByPath", query = "SELECT a FROM Access a WHERE a.path LIKE :accessPath1 AND a.path NOT LIKE :accessPath2") })
public class Access extends VersionedEntity implements java.io.Serializable, Treeable {

	private AccessType accessType;
	private String path;
	private String name;

	public Access() {
	}

	public Access(final no.valg.eva.admin.common.rbac.Access avo) {
		accessType = new AccessType(avo.getAccessType());
		path = avo.getPath();
		name = avo.getName();
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "access_type_pk", nullable = false)
	public AccessType getAccessType() {
		return this.accessType;
	}

	public void setAccessType(final AccessType accessType) {
		this.accessType = accessType;
	}

	@Override
	@Column(name = "access_path", nullable = false, length = 100)
	@StringNotNullEmptyOrBlanks
	@Size(max = 100)
	public String getPath() {
		return this.path;
	}

	public void setPath(final String path) {
		this.path = path;
	}

	@Column(name = "access_name", nullable = false, length = 50)
	@StringNotNullEmptyOrBlanks
	@Size(max = 50)
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

	@Override
	@Transient
	public String getAuditLogId() {
		return path;
	}

	/**
	 * @return this as view object
	 */
	public no.valg.eva.admin.common.rbac.Access toViewObject() {
		no.valg.eva.admin.common.rbac.Access avo = new no.valg.eva.admin.common.rbac.Access();
		avo.setAccessType(accessType.toViewObject());
		avo.setPath(path);
		avo.setName(name);
		return avo;
	}
}
