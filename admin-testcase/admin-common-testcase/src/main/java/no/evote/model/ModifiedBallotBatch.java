package no.evote.model;

import java.util.LinkedHashSet;
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

import org.apache.commons.lang3.Range;

/**
 */
@Entity
@Table(name = "cast_vote_batch")
@AttributeOverride(name = "pk", column = @Column(name = "cast_vote_batch_pk"))
@NamedQueries({
		@NamedQuery(name = "ModifiedBallotBatch.findActiveForOperator", query = "SELECT b FROM ModifiedBallotBatch b WHERE b.operator.pk = :operatorPk"),
		@NamedQuery(
				name = "ModifiedBallotBatch.countModifiedBallotsInProgress",
				query = "select count(*) from  ModifiedBallotBatchMember where done != true and modifiedBallot.ballotCount.pk = :ballotCountPk"),
		@NamedQuery(
				name = "ModifiedBallotBatch.lowestBatchMemberSerialNumberForBallotCount",
				query = "select coalesce(max(serialNumber), 0) from ModifiedBallotBatchMember where modifiedBallot.ballotCount.pk = :ballotCountPk"),
		@NamedQuery(
				name = "ModifiedBallotBatch.countModifiedBallotBatchForBallotCountPks",
				query = "select count(b) \n"
						+ "from  ModifiedBallotBatch b \n"
						+ "where b.ballotCount.pk in (:ballotCountPks)")
})
public class ModifiedBallotBatch extends VersionedEntity {
	private BallotCount ballotCount;
	private Set<ModifiedBallotBatchMember> batchMembers = new LinkedHashSet<>();
	private Operator operator;
	private String id;

	public ModifiedBallotBatch(Operator operator, BallotCount ballotCount) {
		this.operator = operator;
		this.ballotCount = ballotCount;
	}

	public ModifiedBallotBatch() {
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "modifiedBallotBatch")
	public Set<ModifiedBallotBatchMember> getBatchMembers() {
		return batchMembers;
	}

	public void setBatchMembers(Set<ModifiedBallotBatchMember> batchMembers) {
		this.batchMembers = batchMembers;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "operator_pk", nullable = false)
	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	@Column(name = "batch_id")
	public String getId() {
		return id;
	}

	public void setId(String batchId) {
		this.id = batchId;
	}

	@OneToOne
	@JoinColumn(name = "ballot_count_pk")
	public BallotCount getBallotCount() {
		return ballotCount;
	}

	public void setBallotCount(BallotCount ballotCount) {
		this.ballotCount = ballotCount;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return null;
	}

	public void addModifiedBallotBatchMember(ModifiedBallotBatchMember modifiedBallotBatchMember) {
		getBatchMembers().add(modifiedBallotBatchMember);
		modifiedBallotBatchMember.setModifiedBallotBatch(this);
	}

	@Transient
	public Range<Integer> getSerialNumberRange() {
		int lowest = Integer.MAX_VALUE, highest = 0;
		for (ModifiedBallotBatchMember batchMember : batchMembers) {
			lowest = Math.min(lowest, batchMember.getSerialNumber());
			highest = Math.max(highest, batchMember.getSerialNumber());
		}
		return Range.between(lowest, highest);
	}

	public ModifiedBallotBatchMember memberWithSerialnumber(int serialnumber) {
		for (ModifiedBallotBatchMember member : getBatchMembers()) {
			if (member.getSerialNumber() == serialnumber) {
				return member;
			}
		}
		return null;
	}
}
