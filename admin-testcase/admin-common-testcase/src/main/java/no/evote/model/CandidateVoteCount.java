package no.evote.model;

import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import no.evote.logging.AuditLogUtil;

/**
 * Settlement: Count of candidate votes
 */
@Entity
@Table(name = "candidate_vote_count", uniqueConstraints = { @UniqueConstraint(columnNames = { "settlement_pk", "candidate_pk", "vote_category_pk",
		"rank_number" }) })
@AttributeOverride(name = "pk", column = @Column(name = "candidate_vote_count_pk"))
public class CandidateVoteCount extends VersionedEntity implements java.io.Serializable {

	private Settlement settlement;
	private Candidate candidate;
	private Affiliation affiliation;
	private VoteCategory voteCategory;
	private Integer rankNumber;
	private BigDecimal votes;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "settlement_pk", nullable = false)
	public Settlement getSettlement() {
		return this.settlement;
	}

	public void setSettlement(final Settlement settlement) {
		this.settlement = settlement;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "candidate_pk", nullable = false)
	public Candidate getCandidate() {
		return this.candidate;
	}

	public void setCandidate(final Candidate candidate) {
		this.candidate = candidate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "affiliation_pk", nullable = false)
	public Affiliation getAffiliation() {
		return this.affiliation;
	}

	public void setAffiliation(final Affiliation affiliation) {
		this.affiliation = affiliation;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "vote_category_pk", nullable = false)
	public VoteCategory getVoteCategory() {
		return this.voteCategory;
	}

	public void setVoteCategory(final VoteCategory voteCategory) {
		this.voteCategory = voteCategory;
	}

	@Column(name = "rank_number")
	public Integer getRankNumber() {
		return this.rankNumber;
	}

	public void setRankNumber(final Integer rankNumber) {
		this.rankNumber = rankNumber;
	}

	@Column(name = "votes", nullable = false, precision = 10)
	public BigDecimal getVotes() {
		return this.votes;
	}

	public void setVotes(final BigDecimal votes) {
		this.votes = votes;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(settlement, candidate, voteCategory);
	}
}
