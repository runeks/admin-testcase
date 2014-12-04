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
 * Status for (scanned) list of proposers
 */
@Entity
@Table(name = "proposer_list_status", uniqueConstraints = @UniqueConstraint(columnNames = "proposer_list_status_id"))
@AttributeOverride(name = "pk", column = @Column(name = "proposer_list_status_pk"))
public class ProposerListStatus extends VersionedEntity implements java.io.Serializable {

	private int id;
	private String name;

	@Column(name = "proposer_list_status_id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	@Column(name = "proposer_list_status_name", nullable = false, length = 50)
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
