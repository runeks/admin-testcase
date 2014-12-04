package no.evote.model;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import no.evote.constants.AreaLevelEnum;
import no.evote.constants.ElectionLevelEnum;
import no.evote.logging.AuditLogUtil;
import no.evote.security.ContextSecurable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;

/**
 * Count of ballots
 */
@Entity
@Table(name = "ballot_count", uniqueConstraints = { @UniqueConstraint(columnNames = { "vote_count_pk", "ballot_pk", "ballot_rejection_pk" }) })
@AttributeOverride(name = "pk", column = @Column(name = "ballot_count_pk"))
@NamedQueries({ @NamedQuery(name = "BallotCount.findModifiedBallotCount", query = "SELECT COUNT(*) FROM ModifiedBallot cv WHERE cv.ballotCount.pk = :bcPk"),
		@NamedQuery(name = "BallotCount.getByVoteCount", query = "SELECT bc FROM BallotCount bc WHERE bc.voteCount.pk = :vcpk") })
@NamedNativeQueries({
		@NamedNativeQuery(name = "BallotCount.findBallotCountByVCBallot", query = "select bc.* from admin.ballot as b "
				+ "left outer join admin.ballot_count as bc on bc.ballot_pk = b.ballot_pk and bc.vote_count_pk = ?1 "
				+ "where b.contest_pk = ?2 AND b.approved = ?3 order by b.display_order ASC", resultClass = BallotCount.class),
		@NamedNativeQuery(name = "BallotCount.findBallotCountByVCBallotRejection", query = "select bc.* from admin.ballot_rejection br "
				+ "left outer join admin.ballot_count as bc on bc.ballot_rejection_pk = br.ballot_rejection_pk and bc.vote_count_pk = ?1 "
				+ "where br.early_voting = ?2 order by br.ballot_rejection_id ASC;", resultClass = BallotCount.class) })
public class BallotCount extends VersionedEntity implements java.io.Serializable, ContextSecurable, Comparable<BallotCount> {
	private VoteCount voteCount;
	private Ballot ballot;
	private BallotRejection ballotRejection;
	private int unmodifiedBallots;
	private int modifiedBallots;
	private Set<ModifiedBallot> modifiedBallotSet = new LinkedHashSet<>();
	@Deprecated
	private Integer tmpManuelRejectedCount;

	private Long registeredModifiedBallots;

	@Deprecated
	private Integer tmpManuelRegistredBallotCount;
	@Deprecated
	private Integer tmpManuelNotRegistredBallotCount;

	public BallotCount() {
	}

	public BallotCount(final VoteCount voteCount, final Ballot ballot, final int unmodifiedBallots, final int modifiedBallots) {
		this.voteCount = voteCount;
		this.ballot = ballot;
		this.unmodifiedBallots = unmodifiedBallots;
		this.modifiedBallots = modifiedBallots;
	}

	public BallotCount(final VoteCount voteCount, final BallotRejection ballotRejection, final int unmodifiedBallots, final int modifiedBallots) {
		super();
		this.voteCount = voteCount;
		this.ballotRejection = ballotRejection;
		this.unmodifiedBallots = unmodifiedBallots;
		this.modifiedBallots = modifiedBallots;
	}

	public BallotCount(final BallotCount ballotCount, final VoteCount voteCount, final Ballot ballot) {
		super();
		this.voteCount = voteCount;
		this.ballot = ballot;
		ballotRejection = ballotCount.getBallotRejection();
		unmodifiedBallots = ballotCount.getUnmodifiedBallots();
		modifiedBallots = ballotCount.getModifiedBallots();
		registeredModifiedBallots = ballotCount.getRegisteredModifiedBallots();
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vote_count_pk", nullable = false)
	public VoteCount getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(final VoteCount voteCount) {
		this.voteCount = voteCount;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ballot_pk", nullable = true)
	public Ballot getBallot() {
		return ballot;
	}

	public void setBallot(final Ballot ballot) {
		this.ballot = ballot;
	}

	@Transient
	public String getBallotId() {
		return ballot != null ? ballot.getId() : null;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ballot_rejection_pk")
	public BallotRejection getBallotRejection() {
		return ballotRejection;
	}

	public void setBallotRejection(final BallotRejection ballotRejection) {
		this.ballotRejection = ballotRejection;
	}

	@Transient
	public String getBallotRejectionId() {
		return ballotRejection != null ? ballotRejection.getId() : null;
	}

	@NotNull
	@Min(0)
	@Column(name = "unmodified_ballots", nullable = false)
	public int getUnmodifiedBallots() {
		return unmodifiedBallots;
	}

	public void setUnmodifiedBallots(final int unmodifiedBallots) {
		this.unmodifiedBallots = unmodifiedBallots;
	}

	@NotNull
	@Min(0)
	@Column(name = "modified_ballots", nullable = false)
	public int getModifiedBallots() {
		return modifiedBallots;
	}

	public void setModifiedBallots(final int modifiedBallots) {
		this.modifiedBallots = modifiedBallots;
	}

	@Transient
	public Long getRegisteredModifiedBallots() {
		return registeredModifiedBallots;
	}

	@Transient
	public void setRegisteredModifiedBallots(final Long registeredModifiedBallots) {
		this.registeredModifiedBallots = registeredModifiedBallots;
	}

	@Transient
	@Deprecated
	public Integer getTmpManuelRejectedCount() {
		return tmpManuelRejectedCount;
	}

	@Deprecated
	public void setTmpManuelRejectedCount(final Integer tmpManuelRejectedCount) {
		this.tmpManuelRejectedCount = tmpManuelRejectedCount == null ? 0 : tmpManuelRejectedCount;
	}

	@Transient
	@Deprecated
	public Integer getTmpManuelRegistredBallotCount() {
		return tmpManuelRegistredBallotCount;
	}

	@Deprecated
	public void setTmpManuelRegistredBallotCount(final Integer tmpManuelRegistredBallotCount) {
		this.tmpManuelRegistredBallotCount = tmpManuelRegistredBallotCount == null ? 0 : tmpManuelRegistredBallotCount;
	}

	@Transient
	@Deprecated
	public Integer getTmpManuelNotRegistredBallotCount() {
		return tmpManuelNotRegistredBallotCount;
	}

	@Deprecated
	public void setTmpManuelNotRegistredBallotCount(final Integer tmpManuelNotRegistredBallotCount) {
		this.tmpManuelNotRegistredBallotCount = tmpManuelNotRegistredBallotCount == null ? 0 : tmpManuelNotRegistredBallotCount;
	}

	@Override
	public Long getAreaPk(final AreaLevelEnum level) {
		if (level.equals(AreaLevelEnum.POLLING_DISTRICT)) {
			return voteCount.getPollingDistrict().getPk();
		} else {
			return null;
		}
	}

	@Override
	public Long getElectionPk(final ElectionLevelEnum level) {
		if (level.equals(ElectionLevelEnum.CONTEST)) {
			return voteCount.getContestReport().getContest().getPk();
		} else {
			return null;
		}
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(voteCount, ballot, ballotRejection);
	}

	@OneToMany(mappedBy = "ballotCount")
	public Set<ModifiedBallot> getModifiedBallotSet() {
		return modifiedBallotSet;
	}

	public void setModifiedBallotSet(final Set<ModifiedBallot> modifiedBallotSet) {
		this.modifiedBallotSet = modifiedBallotSet;
	}

	@Override
	public int compareTo(final BallotCount other) {
		if (this.getBallot() != null) {
			return getBallot().compareTo(other.getBallot());
		}
		return getBallotRejection().compareTo(other.getBallotRejection());
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		BallotCount rhs = (BallotCount) obj;
		return new EqualsBuilder()
				.appendSuper(super.equals(obj))
				.append(this.voteCount, rhs.voteCount)
				.append(this.ballot, rhs.ballot)
				.append(this.ballotRejection, rhs.ballotRejection)
				.append(this.unmodifiedBallots, rhs.unmodifiedBallots)
				.append(this.modifiedBallots, rhs.modifiedBallots)
				.append(this.registeredModifiedBallots, rhs.registeredModifiedBallots)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.appendSuper(super.hashCode())
				.append(voteCount)
				.append(ballot)
				.append(ballotRejection)
				.append(unmodifiedBallots)
				.append(modifiedBallots)
				.append(registeredModifiedBallots)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.appendSuper(super.toString())
				.append("voteCount", voteCount)
				.append("ballot", ballot)
				.append("ballotRejection", ballotRejection)
				.append("unmodifiedBallots", unmodifiedBallots)
				.append("modifiedBallots", modifiedBallots)
				.append("registeredModifiedBallots", registeredModifiedBallots)
				.toString();
	}

	/**
	 * @return The number of modified ballots where registration of corrections has been started (=in progress) or been completed. I.e. - modified ballots that
	 *         are not yet in progress are not counted here.
	 */
	public int numberOfCompletedOrInProgressModifiedBallots() {
		return getModifiedBallotSet().size();
	}

	public String partyName() {
		Ballot ballot = getBallot();
		return ballot != null ? ballot.partyName() : null;
	}

	public int highestModifiedBallotId() {
		int max = 0;
		for (ModifiedBallot modifiedBallot : modifiedBallotSet) {
			max = Math.max(max, Integer.parseInt(modifiedBallot.getId()));
		}
		return max;
	}

	public void addModifiedBallot(ModifiedBallot modifiedBallot) {
		getModifiedBallotSet().add(modifiedBallot);
		modifiedBallot.setId(String.format("%010d", getModifiedBallotSet().size()));
	}
}
