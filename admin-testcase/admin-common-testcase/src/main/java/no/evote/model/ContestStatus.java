package no.evote.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import no.evote.validation.StringNotNullEmptyOrBlanks;

/**
 * Status for contest
 */
@Entity
@Table(name = "contest_status", uniqueConstraints = @UniqueConstraint(columnNames = "contest_status_id"))
@AttributeOverride(name = "pk", column = @Column(name = "contest_status_pk"))
public class ContestStatus extends VersionedEntity implements java.io.Serializable {

	private Access access;
	private int id;
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "task_access_pk")
	public Access getAccess() {
		return this.access;
	}

	public void setAccess(final Access access) {
		this.access = access;
	}

	@Column(name = "contest_status_id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	@Column(name = "contest_status_name", nullable = false, length = 50)
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
		return String.valueOf(id);
	}
}