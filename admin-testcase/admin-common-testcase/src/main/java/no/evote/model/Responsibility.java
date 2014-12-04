package no.evote.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import no.evote.validation.StringNotNullEmptyOrBlanks;

/**
 * Possible responsibilities for officers in reporting units
 */
@Entity
@Table(name = "responsibility", uniqueConstraints = @UniqueConstraint(columnNames = "responsibility_id"))
@AttributeOverride(name = "pk", column = @Column(name = "responsibility_pk"))
public class Responsibility extends VersionedEntity implements java.io.Serializable {

	private String id;
	private String name;

	@Column(name = "responsibility_id", nullable = false, length = 4)
	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	@Column(name = "responsibility_name", nullable = false, length = 50)
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
}
