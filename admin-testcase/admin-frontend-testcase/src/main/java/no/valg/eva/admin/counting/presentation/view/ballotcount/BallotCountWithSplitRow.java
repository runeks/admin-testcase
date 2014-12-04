package no.valg.eva.admin.counting.presentation.view.ballotcount;

import no.valg.eva.admin.common.counting.model.BallotCount;

public class BallotCountWithSplitRow extends BallotCountRow implements BallotCountsWithSplitModelRow {

	public BallotCountWithSplitRow(BallotCount ballotCount) {
		super(ballotCount);
	}

	@Override
	public boolean isCountInput() {
		return false;
	}

	@Override
	public void setCount(Long count) {
		;
	}

	@Override
	public boolean isModifiedCountInput() {
		return true;
	}

	@Override
	public void setModifiedCount(Integer modifiedCount) {
		if (modifiedCount != null) {
			getBallotCount().setModifiedCount(modifiedCount);
		}
	}

	@Override
	public Integer getModifiedCount() {
		return getBallotCount().getModifiedCount();
	}

	@Override
	public boolean isUnmodifiedCountInput() {
		return true;
	}

	@Override
	public void setUnmodifiedCount(Integer unmodifiedCount) {
		if (unmodifiedCount != null) {
			getBallotCount().setUnmodifiedCount(unmodifiedCount);
		}
	}

	@Override
	public Integer getUnmodifiedCount() {
		return getBallotCount().getUnmodifiedCount();
	}
}
