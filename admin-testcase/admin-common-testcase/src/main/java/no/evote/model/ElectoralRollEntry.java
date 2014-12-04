package no.evote.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import no.evote.logging.AuditLogUtil;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "electoral_roll_entry", uniqueConstraints = @UniqueConstraint(columnNames = { "voter_pk", "election_group_pk" }))
@AttributeOverride(name = "pk", column = @Column(name = "electoral_roll_entry_pk"))
public class ElectoralRollEntry extends VersionedEntity implements java.io.Serializable {

	private Voter voter;
	private ElectionGroup electionGroup;

	public ElectoralRollEntry() {
		super();
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "election_group_pk", nullable = false)
	public ElectionGroup getElectionGroup() {
		return electionGroup;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "voter_pk", nullable = false)
	public Voter getVoter() {
		return voter;
	}

	public void setVoter(final Voter voter) {
		this.voter = voter;
	}

	public void setElectionGroup(final ElectionGroup electionGroup) {
		this.electionGroup = electionGroup;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(voter, electionGroup);
	}

}
