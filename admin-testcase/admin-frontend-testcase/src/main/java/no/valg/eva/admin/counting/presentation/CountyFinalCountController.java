package no.valg.eva.admin.counting.presentation;

import java.util.List;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import no.valg.eva.admin.common.counting.constants.ReportingUnitTypeId;
import no.valg.eva.admin.common.counting.model.FinalCount;

@Named
@ConversationScoped
public class CountyFinalCountController extends BaseFinalCountController {

	@Override
	int getFinalCountIndex() {
		return getCounts().getCountyFinalCountIndex();
	}

	@Override
	void updateCounts(int index, FinalCount finalCount) {
		if (index == getFinalCountIndex()) {
			getCounts().getCountyFinalCounts().set(index, finalCount);
		} else {
			getCounts().getCountyFinalCounts().add(index, finalCount);
			getCounts().setCountyFinalCountIndex(index);
		}
		updateHasModifiedBallotBatchForCurrentCount();
	}

	@Override
	public List<FinalCount> getFinalCounts() {
		return getCounts().getCountyFinalCounts();
	}

	@Override
	ReportingUnitTypeId getReportingUnitTypeId() {
		return ReportingUnitTypeId.FYLKESVALGSTYRET;
	}

	@Override
	public int getBlankBallotCountDifferenceFromPreviousCount() {
		return getCounts().getCountyBlankBallotCountDifference(getFinalCount().getId());
	}

	@Override
	public int getOrdinaryBallotCountDifferenceFromPreviousCount() {
		return getCounts().getCountyOrdinaryBallotCountDifference(getFinalCount().getId());
	}

	@Override
	public int getQuestionableBallotCountDifferenceFromPreviousCount() {
		return getCounts().getCountyQuestionableBallotCountDifference(getFinalCount().getId());
	}

	@Override
	public int getTotalBallotCountDifferenceFromPreviousCount() {
		return getCounts().getCountyTotalBallotCountDifference(getFinalCount().getId());
	}

	@Override
	public boolean isApproved() {
		return getCounts().hasApprovedCountyFinalCount();
	}
}
