package no.evote.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import no.evote.validation.StringNotNullEmptyOrBlanks;

/**
 * Categories for separation of votes, e.g. normal votes and write-ins
 */
@Entity
@Cacheable
@Table(name = "vote_category", uniqueConstraints = @UniqueConstraint(columnNames = "vote_category_id"))
@AttributeOverride(name = "pk", column = @Column(name = "vote_category_pk"))
public class VoteCategory extends VersionedEntity implements java.io.Serializable {

	private String id;
	private String name;

	public enum VoteCategoryValues {
		personal, renumber, strikeout, writein,
	}

	@Column(name = "vote_category_id", nullable = false, length = 12)
	@StringNotNullEmptyOrBlanks
	@Size(max = 12)
	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	@Column(name = "vote_category_name", nullable = false, length = 50)
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
