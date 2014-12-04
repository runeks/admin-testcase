package no.valg.eva.admin.counting.presentation.view.ballotcount;

import no.valg.eva.admin.counting.presentation.CountController;

public class TotalRejectedBallotCountRow extends HeaderRow {

	private CountController ctrl;

	public TotalRejectedBallotCountRow(CountController ctrl) {
		super("@count.ballot.totalRejected");
		this.ctrl = ctrl;
	}

	@Override
	public Long getCount() {
		return (long) ctrl.getCount().getTotalRejectedBallotCount();
	}
}
