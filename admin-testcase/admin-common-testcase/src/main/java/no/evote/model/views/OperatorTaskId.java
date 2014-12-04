package no.evote.model.views;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OperatorTaskId implements java.io.Serializable {

	private long operatorRolePk;
	private int taskNumber;

	@Column(name = "operator_role_pk", nullable = false)
	public long getOperatorRolePk() {
		return this.operatorRolePk;
	}

	public void setOperatorRolePk(final long operatorRolePk) {
		this.operatorRolePk = operatorRolePk;
	}

	@Column(name = "task_number", nullable = false)
	public int getTaskNumber() {
		return this.taskNumber;
	}

	public void setTaskNumber(final int taskNumber) {
		this.taskNumber = taskNumber;
	}

	public boolean equals(final Object other) {
		if ((this == other)) {
			return true;
		}
		if ((other == null)) {
			return false;
		}
		if (!(other instanceof OperatorTaskId)) {
			return false;
		}
		OperatorTaskId castOther = (OperatorTaskId) other;

		return (this.getOperatorRolePk() == castOther.getOperatorRolePk()) && (this.getTaskNumber() == castOther.getTaskNumber());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getOperatorRolePk();
		result = 37 * result + this.getTaskNumber();
		return result;
	}

}
