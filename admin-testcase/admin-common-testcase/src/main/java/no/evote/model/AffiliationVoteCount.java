package no.evote.model;

import javax.persistence.AttributeOverride;
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

import no.evote.logging.AuditLogUtil;

/**
 * Settlement: Count of affiliation votes
 */
@Entity
@Table(name = "affiliation_vote_count", uniqueConstraints = @UniqueConstraint(columnNames = { "settlement_pk", "affiliation_pk" }))
@AttributeOverride(name = "pk", column = @Column(name = "affiliation_vote_count_pk"))
@NamedQueries({ @NamedQuery(name = "AffiliationVoteCount.findBySettlement", query = "SELECT avc FROM AffiliationVoteCount avc WHERE"
		+ " avc.settlement.pk = :settlementPk AND affiliation.ballot.id != 'BLANK' ORDER BY avc.affiliation.displayOrder") })
public class AffiliationVoteCount extends VersionedEntity implements java.io.Serializable {

	private Settlement settlement;
	private Affiliation affiliation;
	private int ballots;
	private int modifiedBallots;
	private int earlyVotingBallots;
	private int earlyVotingModifiedBallots;
	private int electionDayBallots;
	private int electionDayModifiedBallots;
	private int baselineVotes;
	private int addedVotes;
	private int subtractedVotes;
	private int votes;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "settlement_pk", nullable = false)
	public Settlement getSettlement() {
		return this.settlement;
	}

	public void setSettlement(final Settlement settlement) {
		this.settlement = settlement;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "affiliation_pk", nullable = false)
	public Affiliation getAffiliation() {
		return this.affiliation;
	}

	public void setAffiliation(final Affiliation affiliation) {
		this.affiliation = affiliation;
	}

	@Column(name = "ballots", nullable = false)
	public int getBallots() {
		return this.ballots;
	}

	public void setBallots(final int ballots) {
		this.ballots = ballots;
	}

	@Column(name = "modified_ballots", nullable = false)
	public int getModifiedBallots() {
		return this.modifiedBallots;
	}

	public void setModifiedBallots(final int modifiedBallots) {
		this.modifiedBallots = modifiedBallots;
	}

	@Column(name = "early_voting_ballots", nullable = false)
	public int getEarlyVotingBallots() {
		return this.earlyVotingBallots;
	}

	public void setEarlyVotingBallots(final int earlyVotingBallots) {
		this.earlyVotingBallots = earlyVotingBallots;
	}

	@Column(name = "early_voting_modified_ballots", nullable = false)
	public int getEarlyVotingModifiedBallots() {
		return this.earlyVotingModifiedBallots;
	}

	public void setEarlyVotingModifiedBallots(final int earlyVotingModifiedBallots) {
		this.earlyVotingModifiedBallots = earlyVotingModifiedBallots;
	}

	@Column(name = "election_day_ballots", nullable = false)
	public int getElectionDayBallots() {
		return this.electionDayBallots;
	}

	public void setElectionDayBallots(final int electionDayBallots) {
		this.electionDayBallots = electionDayBallots;
	}

	@Column(name = "election_day_modified_ballots", nullable = false)
	public int getElectionDayModifiedBallots() {
		return this.electionDayModifiedBallots;
	}

	public void setElectionDayModifiedBallots(final int electionDayModifiedBallots) {
		this.electionDayModifiedBallots = electionDayModifiedBallots;
	}

	@Column(name = "baseline_votes", nullable = false)
	public int getBaselineVotes() {
		return this.baselineVotes;
	}

	public void setBaselineVotes(final int baselineVotes) {
		this.baselineVotes = baselineVotes;
	}

	@Column(name = "added_votes", nullable = false)
	public int getAddedVotes() {
		return this.addedVotes;
	}

	public void setAddedVotes(final int addedVotes) {
		this.addedVotes = addedVotes;
	}

	@Column(name = "subtracted_votes", nullable = false)
	public int getSubtractedVotes() {
		return this.subtractedVotes;
	}

	public void setSubtractedVotes(final int subtractedVotes) {
		this.subtractedVotes = subtractedVotes;
	}

	@Column(name = "votes", nullable = false)
	public int getVotes() {
		return this.votes;
	}

	public void setVotes(final int votes) {
		this.votes = votes;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(settlement, affiliation);
	}

}
