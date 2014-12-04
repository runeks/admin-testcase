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

import no.evote.validation.StringNotNullEmptyOrBlanks;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Categories for separation of cast votes, e.g. ordinary early votes and early votes from other municipalities
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "voting_category", uniqueConstraints = @UniqueConstraint(columnNames = "voting_category_id"))
@AttributeOverride(name = "pk", column = @Column(name = "voting_category_pk"))
@NamedQueries({ @NamedQuery(name = "VotingCategory.findPaperVotingCategories", query = "SELECT vc FROM VotingCategory vc WHERE vc.electronicVoting = false") })
public class VotingCategory extends VersionedEntity implements java.io.Serializable {

	private String id;
	private boolean earlyVoting;
	private boolean electronicVoting;
	private String name;

	@Column(name = "voting_category_id", nullable = false, length = 4)
	@StringNotNullEmptyOrBlanks
	@Size(max = 4)
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

	@Column(name = "voting_category_name", nullable = false, length = 50)
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
