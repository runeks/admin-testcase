package no.evote.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import no.evote.constants.AreaLevelEnum;
import no.evote.constants.ElectionLevelEnum;
import no.evote.logging.AuditLogUtil;
import no.evote.security.ContextSecurable;

/**
 * Individual modified ballots
 */
@Entity
@Table(name = "cast_vote", uniqueConstraints = @UniqueConstraint(columnNames = { "ballot_count_pk", "cast_vote_id" }))
@AttributeOverride(name = "pk", column = @Column(name = "cast_vote_pk"))
@NamedQueries({ @NamedQuery(name = "ModifiedBallot.getByBallotCount", query = "SELECT cv FROM ModifiedBallot cv WHERE cv.ballotCount.pk = :bcpk"),
		@NamedQuery(name = "ModifiedBallot.findNextUnassignedModifiedBallot",
				query = "SELECT cv from ModifiedBallot cv "
						+ "where cv.ballotCount.pk = :ballotCountPk and not exists "
						+ "(from ModifiedBallotBatchMember m where m.modifiedBallot = cv)")
})
public class ModifiedBallot extends VersionedEntity implements java.io.Serializable, ContextSecurable {

	private BallotCount ballotCount;
	private BinaryData binaryData;
	private String id;

	private int index;
	private boolean corrected;

	private ModifiedBallotBatchMember modifiedBallotBatchMember;
	private Set<CandidateVote> candidateVotes = new HashSet<>();
	
	public ModifiedBallot() {
		super();
	}

	public ModifiedBallot(final ModifiedBallot modifiedBallot, final BallotCount ballotCount) {
		super();
		this.ballotCount = ballotCount;
		binaryData = modifiedBallot.getBinaryData();
		id = modifiedBallot.getId();
	}

	public ModifiedBallot(BallotCount ballotCount) {
		this.ballotCount = ballotCount;
		ballotCount.addModifiedBallot(this);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ballot_count_pk", nullable = false)
	public BallotCount getBallotCount() {
		return ballotCount;
	}

	public void setBallotCount(final BallotCount ballotCount) {
		this.ballotCount = ballotCount;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scan_binary_data_pk")
	public BinaryData getBinaryData() {
		return binaryData;
	}

	public void setBinaryData(final BinaryData binaryData) {
		this.binaryData = binaryData;
	}

	@Column(name = "cast_vote_id", nullable = false, length = 10)
	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	@OneToOne(mappedBy = "modifiedBallot")
	public ModifiedBallotBatchMember getModifiedBallotBatchMember() {
		return modifiedBallotBatchMember;
	}

	public void setModifiedBallotBatchMember(ModifiedBallotBatchMember modifiedBallotBatchMember) {
		this.modifiedBallotBatchMember = modifiedBallotBatchMember;
	}

	@OneToMany(mappedBy = "modifiedBallot", cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<CandidateVote> getCandidateVotes() {
		return candidateVotes;
	}

	public void setCandidateVotes(Set<CandidateVote> candidateVotes) {
		this.candidateVotes = candidateVotes;
	}

	private void addCandidateVote(CandidateVote candidateVote) {
		getCandidateVotes().add(candidateVote);
		candidateVote.setModifiedBallot(this);
	}

	@Transient
	public int getIndex() {
		return index;
	}

	@Transient
	public void setIndex(final int index) {
		this.index = index;
	}

	/**
	 * NOTE: This field must be used with care, as it is not clear what it means. Possible interpretations:
	 * - The ballot has been modified (which is kind of given, since this class holds modified ballots)
	 * - The processing of the ballot has been completed
	 * - ?
	 */
	@Transient
	public boolean isCorrected() {
		return corrected;
	}

	@Transient
	public void setCorrected(final boolean corrected) {
		this.corrected = corrected;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ModifiedBallot other = (ModifiedBallot) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(ballotCount, id);
	}

	@Override
	public Long getAreaPk(final AreaLevelEnum level) {
		return ballotCount.getAreaPk(level);
	}

	@Override
	public Long getElectionPk(final ElectionLevelEnum level) {
		return ballotCount.getElectionPk(level);
	}

	public String partyName() {
		return getBallotCount().partyName();
	}

	/**
	 * Updates existing candidate votes, removes any write in and personal vote candidates
	 * not passed in.
	 */
	public void updateCandidateVotesForWriteInAndPersonalVotes(Set<CandidateVote> candidateVotes) {
		for (CandidateVote candidateVote : candidateVotes) {
			candidateVote.setModifiedBallot(this);
		}
		Set<CandidateVote> candidateVotesForRemoval = writeInAndPersonalVoteCandidatesForRemoval(candidateVotes);

		getCandidateVotes().addAll(candidateVotes);
		getCandidateVotes().removeAll(candidateVotesForRemoval);
		
	}

	private Set<CandidateVote> writeInAndPersonalVoteCandidatesForRemoval(Set<CandidateVote> candidateVotes) {
		Set<CandidateVote> candidatesForRemoval = new HashSet<>(getCandidateVotes());
		for (CandidateVote candidateVote : candidateVotes) {
			candidatesForRemoval.remove(candidateVote);    // NB! removeAll funker faktisk ikke
		}
		Set<CandidateVote> result = new HashSet<>(candidatesForRemoval);
		for (CandidateVote candidateVote : candidatesForRemoval) {
			if (!(candidateVote.isWriteIn() || candidateVote.isPersonalVote())) {
				result.remove(candidateVote);
			}
		}
		return result;
	}
}
