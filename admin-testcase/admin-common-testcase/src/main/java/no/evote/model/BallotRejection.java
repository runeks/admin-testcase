package no.evote.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import no.evote.util.EqualsHashCodeUtil;
import no.evote.validation.StringNotNullEmptyOrBlanks;

/**
 * Reasons for rejection of ballots
 */
@Entity
@Cacheable
@Table(name = "ballot_rejection", uniqueConstraints = @UniqueConstraint(columnNames = "ballot_rejection_id"))
@AttributeOverride(name = "pk", column = @Column(name = "ballot_rejection_pk"))
@NamedQueries({
		@NamedQuery(name = "BallotRejection.BallotRejectionByEarlyVoting", query = "select br from BallotRejection br where br.earlyVoting = :ev order by id"),
		@NamedQuery(name = "BallotRejection.findAll", query = "select br from BallotRejection br order by id") })
public class BallotRejection extends VersionedEntity implements java.io.Serializable, Comparable<BallotRejection> {

	private String id;
	private boolean earlyVoting;
	private boolean electronicVoting;
	private String name;

	@Column(name = "ballot_rejection_id", nullable = false, length = 6)
	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	@Column(name = "early_voting", nullable = false)
	public boolean isEarlyVoting() {
		return this.earlyVoting;
	}

	public void setEarlyVoting(final boolean earlyVoting) {
		this.earlyVoting = earlyVoting;
	}

	@Column(name = "electronic_voting", nullable = false)
	public boolean isElectronicVoting() {
		return this.electronicVoting;
	}

	public void setElectronicVoting(final boolean electronicVoting) {
		this.electronicVoting = electronicVoting;
	}

	@Column(name = "ballot_rejection_name", nullable = false, length = 50)
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
		return id;
	}

	@Override
	public int compareTo(final BallotRejection other) {
		return getId().compareTo(other.getId());
	}
}
