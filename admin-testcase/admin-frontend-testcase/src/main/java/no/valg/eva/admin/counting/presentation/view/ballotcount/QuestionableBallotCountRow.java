package no.valg.eva.admin.counting.presentation.view.ballotcount;

import no.valg.eva.admin.counting.presentation.CountController;

public class QuestionableBallotCountRow extends HeaderRow {

	private CountController ctrl;

	public QuestionableBallotCountRow(CountController ctrl) {
		super("@count.label.questionable");
		this.ctrl = ctrl;
	}

	@Override
	public Integer getProtocolCount() {
		return ctrl.getCounts().getQuestionableBallotCountForProtocolCounts();
	}

	@Override
	public boolean isCountInput() {
		return true;
	}

	@Override
	public Long getCount() {
		return (long) ctrl.getCount().getQuestionableBallotCount();
	}

	@Override
	public void setCount(Long count) {
		if (count != null) {
			ctrl.getCount().setQuestionableBallotCount(count.intValue());
		}
	}

	@Override
	public Integer getDiff() {
		return ctrl.getQuestionableBallotCountDifferenceFromPreviousCount();
	}

	@Override
	public String getStyleClass() {
		return "";
	}
}
