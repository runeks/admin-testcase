package no.valg.eva.admin.common.counting.model.modifiedballots;

import java.io.Serializable;

import no.valg.eva.admin.common.counting.model.BallotCount;

public class ModifiedBallotsStatus implements Serializable {
	private int inProgress;
	private int completed;
	private int total;

	private BallotCount ballotCount;

	public ModifiedBallotsStatus(BallotCount ballotCount) {
		this.ballotCount = ballotCount;
	}

	public ModifiedBallotsStatus(int total, int inProgress, int completed) {
		this.total = total;
		this.inProgress = inProgress;
		this.completed = completed;
	}

	public String getBallotId() {
		return ballotCount.getId();
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(final int total) {
		this.total = total;
	}

	public int getRemaining() {
		return total - completed - inProgress;
	}

	public boolean isRegistrationComplete() {
		return getRemaining() == 0 && inProgress == 0;
	}

	public void setInProgress(int inProgress) {
		this.inProgress = inProgress;
	}

	public int getInProgress() {
		return inProgress;
	}

	public void setCompleted(final int completed) {
		this.completed = completed;
	}

	public int getCompleted() {
		return completed;
	}

	public BallotCount getBallotCount() {
		return ballotCount;
	}

	public boolean hasModifiedBallotsAndRegistrationIsNotDone() {
		return (hasModifiedBallots() && getRemaining() > 0);
	}

	public boolean hasModifiedBallotsAndRegistrationIsDone() {
		return (total > 0 && getRemaining() == 0);
	}

	public boolean hasModifiedBallots() {
		return total > 0;
	}
}
