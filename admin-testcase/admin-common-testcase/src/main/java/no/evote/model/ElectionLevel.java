package no.evote.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import no.evote.util.EqualsHashCodeUtil;
import no.evote.validation.StringNotNullEmptyOrBlanks;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Description of levels in mv_election
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "election_level", uniqueConstraints = @UniqueConstraint(columnNames = "election_level_id"))
@AttributeOverride(name = "pk", column = @Column(name = "election_level_pk"))
@NamedQuery(name = "ElectionLevel.findAllUnderLevel", query = "SELECT el FROM ElectionLevel el WHERE el.id >= :level")
public class ElectionLevel extends VersionedEntity implements java.io.Serializable {

	private int id;
	private String name;

	@Column(name = "election_level_id", unique = true, nullable = false)
	@NotNull
	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	@Column(name = "election_level_name", nullable = false, length = 50)
	@StringNotNullEmptyOrBlanks
	@Size(max = 50)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public boolean equals(final Object other) {
		return EqualsHashCodeUtil.genericEquals(this, other);
	}

	@Override
	public int hashCode() {
		return EqualsHashCodeUtil.genericHashCode(this);
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return String.valueOf(id);
	}
}
