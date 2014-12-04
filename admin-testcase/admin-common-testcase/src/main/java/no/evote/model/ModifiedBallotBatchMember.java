package no.evote.model;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 */
@Entity
@Table(name = "cast_vote_batch_member")
@AttributeOverride(name = "pk", column = @Column(name = "cast_vote_batch_member_pk"))
@NamedQueries({
		@NamedQuery(name = "ModifiedBallotBatchMember.findHighestSerialNumberForBallotCount",
			query = "select max(serialNumber) from ModifiedBallotBatchMember where modifiedBallot.ballotCount.pk = :ballotCountPk")
})
public class ModifiedBallotBatchMember extends VersionedEntity {
	private ModifiedBallotBatch modifiedBallotBatch;
	private ModifiedBallot modifiedBallot;
	private int serialNumber;
	private boolean done;

	public ModifiedBallotBatchMember(ModifiedBallot modifiedBallot) {
		setModifiedBallot(modifiedBallot);
	}

	public ModifiedBallotBatchMember() {
		super();
	}

	public ModifiedBallotBatchMember(ModifiedBallot modifiedBallot, boolean done, int serialNumber) {
		this.modifiedBallot = modifiedBallot;
		this.done = done;
		this.serialNumber = serialNumber;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cast_vote_pk")
	public ModifiedBallot getModifiedBallot() {
		return modifiedBallot;
	}

	public void setModifiedBallot(ModifiedBallot modifiedBallot) {
		this.modifiedBallot = modifiedBallot;
	}

	@Column(name = "serial_number")
	public int getSerialNumber() {
		return serialNumber;
	}

	@Column(name = "done")
	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cast_vote_batch_pk", nullable = false)
	public ModifiedBallotBatch getModifiedBallotBatch() {
		return modifiedBallotBatch;
	}

	public void setModifiedBallotBatch(ModifiedBallotBatch modifiedBallotBatch) {
		this.modifiedBallotBatch = modifiedBallotBatch;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return null;
	}

	public String partyName() {
		ModifiedBallot modifiedBallot = getModifiedBallot();
		return modifiedBallot != null ? modifiedBallot.partyName() : null;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ModifiedBallotBatchMember)) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}

		ModifiedBallotBatchMember that = (ModifiedBallotBatchMember) o;

		if (done != that.done) {
			return false;
		}
		if (serialNumber != that.serialNumber) {
			return false;
		}
		if (modifiedBallot != null ? !modifiedBallot.equals(that.modifiedBallot) : that.modifiedBallot != null) {
			return false;
		}
		if (modifiedBallotBatch != null ? !modifiedBallotBatch.equals(that.modifiedBallotBatch) : that.modifiedBallotBatch != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (modifiedBallotBatch != null ? modifiedBallotBatch.hashCode() : 0);
		result = 31 * result + (modifiedBallot != null ? modifiedBallot.hashCode() : 0);
		result = 31 * result + serialNumber;
		result = 31 * result + (done ? 1 : 0);
		return result;
	}
}
