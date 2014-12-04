package no.valg.eva.admin.counting.presentation.view.ballotcount;

import no.valg.eva.admin.common.counting.model.RejectedBallotCount;
import no.valg.eva.admin.counting.presentation.CountController;

public class RejectedBallotCountRow extends HeaderRow {

	private RejectedBallotCount rejectedBallotCount;

	public RejectedBallotCountRow(CountController ctrl, RejectedBallotCount rejectedBallotCount) {
		super(ctrl.getMessageProvider().get(rejectedBallotCount.getName()) + " (" + rejectedBallotCount.getId() + ")");
		this.rejectedBallotCount = rejectedBallotCount;
	}

	@Override
	public boolean isCountInput() {
		return true;
	}

	@Override
	public Long getCount() {
		return (long) rejectedBallotCount.getCount();
	}

	@Override
	public void setCount(Long count) {
		if (count != null) {
			rejectedBallotCount.setCount(count.intValue());
		}
	}

	@Override
	public String getStyleClass() {
		return "";
	}
}
