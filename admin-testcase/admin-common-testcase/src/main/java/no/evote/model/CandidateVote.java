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

import no.evote.constants.AreaLevelEnum;
import no.evote.constants.ElectionLevelEnum;
import no.evote.logging.AuditLogUtil;
import no.evote.security.ContextSecurable;

/**
 * Candidate votes on individual ballot
 */
@Entity
@Table(name = "candidate_vote", uniqueConstraints = @UniqueConstraint(columnNames = { "cast_vote_pk", "candidate_pk", "vote_category_pk" }))
@AttributeOverride(name = "pk", column = @Column(name = "candidate_vote_pk"))
@NamedQueries({ @NamedQuery(name = "CandidateVote.getByModifiedBallot", query = "SELECT cv FROM CandidateVote cv WHERE cv.modifiedBallot.pk = :cvpk") })
public class CandidateVote extends VersionedEntity implements java.io.Serializable, ContextSecurable {

	private Candidate candidate;
	private VoteCategory voteCategory;
	private ModifiedBallot modifiedBallot;
	private Integer renumberPosition;

	public CandidateVote() {
		super();
	}

	public CandidateVote(final CandidateVote candidateVote, final Candidate candidate, final ModifiedBallot modifiedBallot) {
		super();
		this.candidate = candidate;
		this.modifiedBallot = modifiedBallot;
		voteCategory = candidateVote.getVoteCategory();
		renumberPosition = candidateVote.getRenumberPosition();
	}

	public CandidateVote(Candidate candidate, VoteCategory voteCategory) {
		this.candidate = candidate;
		this.voteCategory = voteCategory;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "candidate_pk", nullable = false)
	public Candidate getCandidate() {
		return this.candidate;
	}

	public void setCandidate(final Candidate candidate) {
		this.candidate = candidate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vote_category_pk", nullable = false)
	public VoteCategory getVoteCategory() {
		return this.voteCategory;
	}

	public void setVoteCategory(final VoteCategory voteCategory) {
		this.voteCategory = voteCategory;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cast_vote_pk", nullable = false)
	public ModifiedBallot getModifiedBallot() {
		return this.modifiedBallot;
	}

	public void setModifiedBallot(final ModifiedBallot modifiedBallot) {
		this.modifiedBallot = modifiedBallot;
	}

	@Column(name = "renumber_position")
	public Integer getRenumberPosition() {
		return this.renumberPosition;
	}

	public void setRenumberPosition(final Integer renumberPosition) {
		this.renumberPosition = renumberPosition;
	}

	@Override
	public Long getAreaPk(final AreaLevelEnum level) {
		return null;
	}

	@Override
	public Long getElectionPk(final ElectionLevelEnum level) {
		if (level.equals(ElectionLevelEnum.CONTEST)) {
			return candidate.getBallot().getContest().getPk();
		}
		return null;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(modifiedBallot, candidate, voteCategory);
	}

	@Override
	public String toString() {
		return new StringBuilder().append("<Candidate> vote_category: ").append(this.getVoteCategory().getId()).append(", renumbering: ")
				.append(this.getRenumberPosition()).toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof CandidateVote)) {
			return false;
		}

		CandidateVote that = (CandidateVote) o;

		if (this.getPk() != null && that.getPk() != null && this.getPk().equals(that.getPk())) {
			return true;
		}

		if (candidate != null ? !candidate.equals(that.candidate) : that.candidate != null) {
			return false;
		}
		if (modifiedBallot != null ? !modifiedBallot.equals(that.modifiedBallot) : that.modifiedBallot != null) {
			return false;
		}
		if (renumberPosition != null ? !renumberPosition.equals(that.renumberPosition) : that.renumberPosition != null) {
			return false;
		}
		if (voteCategory != null ? !voteCategory.equals(that.voteCategory) : that.voteCategory != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + (candidate != null ? candidate.hashCode() : 0);
		result = 31 * result + (voteCategory != null ? voteCategory.hashCode() : 0);
		result = 31 * result + (modifiedBallot != null ? modifiedBallot.hashCode() : 0);
		result = 31 * result + (renumberPosition != null ? renumberPosition.hashCode() : 0);
		return result;
	}

	@Transient
	public boolean isPersonalVote() {
		return VoteCategory.VoteCategoryValues.personal.name().equals(getVoteCategory().getId());
	}

	@Transient
	public boolean isWriteIn() {
		return VoteCategory.VoteCategoryValues.writein.name().equals(getVoteCategory().getId());
	}

}
