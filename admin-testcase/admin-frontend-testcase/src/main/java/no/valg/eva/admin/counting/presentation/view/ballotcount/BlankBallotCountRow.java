package no.valg.eva.admin.counting.presentation.view.ballotcount;

import no.valg.eva.admin.counting.presentation.CountController;

public class BlankBallotCountRow extends HeaderRow {

	private CountController ctrl;

	public BlankBallotCountRow(CountController ctrl) {
		super("@count.label.blancs");
		this.ctrl = ctrl;
	}

	@Override
	public Integer getProtocolCount() {
		return ctrl.getCounts().getBlankBallotCountForProtocolCounts();
	}

	@Override
	public boolean isCountInput() {
		return true;
	}

	@Override
	public Long getCount() {
		return (long) ctrl.getCount().getBlankBallotCount();
	}

	@Override
	public void setCount(Long count) {
		if (count != null) {
			ctrl.getCount().setBlankBallotCount(count.intValue());
		}
	}

	@Override
	public Integer getDiff() {
		return ctrl.getBlankBallotCountDifferenceFromPreviousCount();
	}

	@Override
	public String getStyleClass() {
		return "";
	}
}
