package no.valg.eva.admin.counting.presentation.view.ballotcount;

import no.valg.eva.admin.counting.presentation.CountController;

public class TotalBallotCountRow extends HeaderRow {

	private CountController ctrl;

	public TotalBallotCountRow(CountController ctrl) {
		super("@count.label.totalBallotCounts");
		this.ctrl = ctrl;
	}

	@Override
	public Integer getProtocolCount() {
		return ctrl.getCounts().getOrdinaryBallotCountForProtocolCounts();
	}

	@Override
	public Long getCount() {
		return (long) ctrl.getCount().getOrdinaryBallotCount();
	}

	@Override
	public Integer getDiff() {
		return ctrl.getOrdinaryBallotCountDifferenceFromPreviousCount();
	}

	@Override
	public Integer getModifiedCount() {
		return ctrl.getCount().getModifiedBallotCount();
	}

	@Override
	public Integer getUnmodifiedCount() {
		return ctrl.getCount().getUnmodifiedBallotCount();
	}
}
