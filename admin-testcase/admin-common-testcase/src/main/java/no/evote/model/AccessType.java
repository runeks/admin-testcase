package no.evote.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import no.evote.validation.StringNotNullEmptyOrBlanks;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * RBAC: Categories of securable objects
 */
@Entity
@Table(name = "access_type", uniqueConstraints = @UniqueConstraint(columnNames = "access_type_id"))
@AttributeOverride(name = "pk", column = @Column(name = "access_type_pk"))
public class AccessType extends VersionedEntity implements java.io.Serializable {

	private String id;
	private String name;

	public AccessType() {
	}

	public AccessType(final no.valg.eva.admin.common.rbac.AccessType accessType) {
		this.id = accessType.getId();
		this.name = accessType.getName();
	}

	@Column(name = "access_type_id", nullable = false, length = 2)
	@StringNotNullEmptyOrBlanks
	@Size(max = 2)
	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	@Column(name = "access_type_name", nullable = false, length = 50)
	@StringNotNullEmptyOrBlanks
	@Size(max = 50)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return id;
	}

	/**
	 * Converts this to view object
	 * @return access type view object
	 */
	public no.valg.eva.admin.common.rbac.AccessType toViewObject() {
		return no.valg.eva.admin.common.rbac.AccessType.fromId(id);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
        }
		if (!(o instanceof AccessType)) {
			return false;
        }

		AccessType that = (AccessType) o;

		if (!id.equals(that.id)) {
			return false;
        }
		if (!name.equals(that.name)) {
			return false;
        }
		return true;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(id)
				.append(name)
				.toHashCode();
	}

}
