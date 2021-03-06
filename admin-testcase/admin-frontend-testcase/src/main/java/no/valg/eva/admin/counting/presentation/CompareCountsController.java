package no.valg.eva.admin.counting.presentation;

import static no.valg.eva.admin.frontend.common.Button.notRendered;
import static no.valg.eva.admin.frontend.common.Button.rendered;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;

import no.evote.exception.ValidateException;
import no.evote.presentation.util.MessageUtil;
import no.valg.eva.admin.common.counting.model.AbstractCount;
import no.valg.eva.admin.common.counting.model.CountQualifier;
import no.valg.eva.admin.common.counting.model.FinalCount;
import no.valg.eva.admin.common.counting.model.RejectedBallotCount;
import no.valg.eva.admin.counting.presentation.view.CompareBallotCountView;
import no.valg.eva.admin.counting.presentation.view.CompareBallotCounts;
import no.valg.eva.admin.counting.presentation.view.CompareCountsSelect;
import no.valg.eva.admin.frontend.common.Button;

@Named
@ConversationScoped
public class CompareCountsController extends CountController {

	private BaseFinalCountController finalCountController;
	private CompareCountsSelect firstCountSelect;
	private CompareCountsSelect secondCountSelect;
	private CompareCountsSelect currentCountsSelect;
	private boolean county;
	private boolean noProcessed;
	private boolean oneProcessed;

	@Override
	protected void doInit() {
		super.doInit();
		finalCountController = (BaseFinalCountController) getPreviousController();
		firstCountSelect = new CompareCountsSelect(this);
		secondCountSelect = new CompareCountsSelect(this);
		county = isUserOnCountyLevel();
		setupDefaultCompare();
	}

	public void setupDefaultCompare() {
		// Setup compares and messages
		List<FinalCount> processed = getProcessedFinalCounts();
		noProcessed = processed.isEmpty();
		oneProcessed = processed.size() == 1;
		firstCountSelect.setId("");
		secondCountSelect.setId("");
		if (noProcessed) {
			return;
		}
		if (oneProcessed) {
			firstCountSelect.setId(processed.get(0).getId());
		}
		if (processed.size() > 1) {
			if (finalCountController.isApproved()) {
				// Set the approved
				for (FinalCount count : processed) {
					if (count.isApproved()) {
						firstCountSelect.setId(count.getId());
					}
				}
			} else {
				// Set to the last 2
				firstCountSelect.setId(processed.get(processed.size() - 2).getId());
				secondCountSelect.setId(processed.get(processed.size() - 1).getId());
			}
		}

	}

	public void newFinalCount() {
		finalCountController.newFinalCount();
		startCountingController.setCurrentTab(getTabIndex() - 1);
	}

	public List<FinalCount> getProcessedFinalCounts() {
		List<FinalCount> result = new ArrayList<>();
		if (finalCountController != null) {
			for (FinalCount count : finalCountController.getFinalCounts()) {
				if (count.isModifiedBallotsProcessed()) {
					result.add(count);
				}
			}
		}
		return result;
	}

	public CompareCountsSelect getFirstCountSelect() {
		return firstCountSelect;
	}

	public CompareCountsSelect getSecondCountSelect() {
		return secondCountSelect;
	}

	public FinalCount getCount(CompareCountsSelect selected) {
		for (FinalCount count : getProcessedFinalCounts()) {
			if (selected.getId().equals(count.getId())) {
				return count;
			}
		}
		return null;
	}

	public void saveComment(CompareCountsSelect selected) {
		try {
			FinalCount count = selected.getFinalCount();
			count.validate();
			count = getCountingService().saveCount(userData, getCountContext(), count, finalCountController.getReportingUnitTypeId());
			updateCountList(count);
			MessageUtil.buildDetailMessage(FacesMessage.SEVERITY_INFO, getMessageProvider().get("@count.isSaved"));
		} catch (Exception e) {
			logAndBuildDetailErrorMessage(e);
		}
	}

	public void approve(CompareCountsSelect selected) {
		try {
			FinalCount count = selected.getFinalCount();
			count.validateForApproval();
			if (!count.hasComment() && getTotalBallotCountDifferenceFromPreviousCount(count) != 0) {
				throw new ValidateException("@count.error.validation.missing_comment");
			}
			count = getCountingService().approveCount(userData, getCountContext(), count, finalCountController.getReportingUnitTypeId());
			updateCountList(count);
			MessageUtil.buildDetailMessage(FacesMessage.SEVERITY_INFO, getMessageProvider().get("@count.isApproved"));
			setupDefaultCompare();
		} catch (Exception e) {
			logAndBuildDetailErrorMessage(e);
		}
	}

	public void reject(CompareCountsSelect selected) {
		try {
			FinalCount count = selected.getFinalCount();
			count = getCountingService().rejectCount(userData, getCountContext(), count, finalCountController.getReportingUnitTypeId());
			updateCountList(count);
			MessageUtil.buildDetailMessage(FacesMessage.SEVERITY_INFO, getMessageProvider().get("@count.isRejected"));
		} catch (Exception e) {
			logAndBuildDetailErrorMessage(e);
		}
	}

	public Button getApproveButton(CompareCountsSelect selected) {
		if (!hasWriteAccess() || selected.getFinalCount() == null) {
			return notRendered();
		}
		FinalCount count = selected.getFinalCount();
		if (count.isEditable() && count.isModifiedBallotsProcessed() && !finalCountController.isApproved()
				&& finalCountController.isPreviousApproved()) {
			return rendered(true);
		}
		return notRendered();
	}

	public Button getRejectButton(CompareCountsSelect selected) {
		if (!hasRejectAccess() || selected.getFinalCount() == null) {
			return notRendered();
		}
		FinalCount count = selected.getFinalCount();
		if (count.isApproved() && finalCountController.isPreviousApproved()) {
			return rendered(true);
		}
		return notRendered();
	}

	public Button getNewFinalCountButton() {
		if (!hasWriteAccess()) {
			return notRendered();
		}
		return rendered(!finalCountController.isApproved()
				&& finalCountController.getFinalCount().isModifiedBallotsProcessed() && finalCountController.isPreviousApproved());
	}

	public int getTotalBallotCountDifferenceFromPreviousCount(FinalCount count) {
		if (county) {
			return getCounts().getCountyTotalBallotCountDifference(count.getId());
		} else {
			return getCounts().getTotalBallotCountDifferenceBetween(CountQualifier.PRELIMINARY, CountQualifier.FINAL, count.getId());
		}
	}

	@Override
	public String getDisplayAreaName() {
		return finalCountController.getDisplayAreaName();
	}

	public AbstractCount getBaseCount() {
		if (county) {
			return getCounts().getFinalCounts().get(getCounts().getFinalCountIndex());
		} else {
			return getCounts().getPreliminaryCount();
		}
	}

	public String getBaseCountNameKey() {
		if (county) {
			return "@count.tab.type[KE].approved";
		} else {
			return "@count.tab.type[F].approved";
		}
	}

	@Override
	public boolean isApproved() {
		return false;
	}

	public CompareBallotCounts getBallotCountViewsForBase() {
		return getBallotCountViewsForBase(getBaseCount(), finalCountController.getFinalCount());
	}

	CompareBallotCounts getBallotCountViewsForBase(AbstractCount baseCount, FinalCount finalCount) {
		boolean isFinalCount = baseCount instanceof FinalCount;
		CompareBallotCounts result = new CompareBallotCounts(baseCount.getBallotCounts());
		// Add SUM ballot counts
		result.add(new CompareBallotCountView("@count.label.ballot_total", baseCount.getOrdinaryBallotCount()).markBold());
		// Add blanks
		result.add(new CompareBallotCountView("@count.label.blancs", null, baseCount.getBlankBallotCount()));
		// Add SUM blanks
		result.add(new CompareBallotCountView("@count.ballot.approve.rejected.proposed"));
		// Add rejected ballot counts
		int i = 0;
		for (RejectedBallotCount count : finalCount.getRejectedBallotCounts()) {
			CompareBallotCountView row;
			if (isFinalCount) {
				row = new CompareBallotCountView(count.getName(), count.getId(), baseCount.getRejectedBallotCounts().get(i).getCount());
			} else {
				row = new CompareBallotCountView(count.getName(), count.getId(), null);
			}
			row.setType(CompareBallotCountView.BallotCountViewType.REJECTED_BALLOT_COUNT);
			result.add(row);
			i++;
		}
		// Add SUM rejected
		if (isFinalCount) {
			result.add(new CompareBallotCountView("@count.ballot.totalRejected", baseCount.getTotalRejectedBallotCount()).markBold());
		} else {
			result.add(new CompareBallotCountView("@count.ballot.totalRejected", baseCount.getQuestionableBallotCount()).markBold());
		}
		return result;
	}

	public CompareBallotCounts getBallotCountViewsForFirst() {
		return getBallotCountViewsFor(getFirstCountSelect());
	}

	public CompareBallotCounts getBallotCountViewsForSecond() {
		return getBallotCountViewsFor(getSecondCountSelect());
	}

	public boolean isCountEditable(FinalCount finalCount) {
		return finalCount.isEditable() && finalCountController.isPreviousApproved() && !finalCountController.isNextApproved()
				&& !finalCountController.isApproved();
	}

	public boolean isNoProcessed() {
		return noProcessed;
	}

	public boolean isOneProcessed() {
		return oneProcessed;
	}

	private void updateCountList(FinalCount finalCount) {
		int index = 0;
		for (FinalCount count : finalCountController.getFinalCounts()) {
			if (finalCount.getId().equals(count.getId())) {
				finalCountController.getFinalCounts().set(index, finalCount);
			}
			index++;
		}
	}

	private CompareBallotCounts getBallotCountViewsFor(CompareCountsSelect selected) {
		return getBallotCountViewsFor(getBaseCount(), selected.getFinalCount());
	}

	CompareBallotCounts getBallotCountViewsFor(AbstractCount baseCount, FinalCount finalCount) {
		if (finalCount == null) {
			finalCount = finalCountController.getFinalCount();
			// CSOFF: MagicNumber
			return new CompareBallotCounts(finalCount.getBallotCounts().size() + 3 + finalCount.getRejectedBallotCounts().size()
					+ 1);
			// CSON: MagicNumber
		}

		boolean isFinalCount = baseCount instanceof FinalCount;
		CompareBallotCounts result = new CompareBallotCounts(finalCount.getBallotCounts(), baseCount.getBallotCounts());

		// Add SUM ballot counts
		CompareBallotCountView row = new CompareBallotCountView(finalCount.getOrdinaryBallotCount()).markBold();
		row.setDiff(finalCount.getOrdinaryBallotCount() - baseCount.getOrdinaryBallotCount());
		result.add(row);
		// Add blanks
		row = new CompareBallotCountView(finalCount.getBlankBallotCount());
		row.setDiff(finalCount.getBlankBallotCount() - baseCount.getBlankBallotCount());
		result.add(row);
		// Add SUM blanks
		result.add(new CompareBallotCountView());
		// Add rejected ballot counts
		int i = 0;
		for (RejectedBallotCount count : finalCount.getRejectedBallotCounts()) {
			row = new CompareBallotCountView(count.getName(), count.getId(), finalCount.getRejectedBallotCounts().get(i).getCount());
			row.setType(CompareBallotCountView.BallotCountViewType.REJECTED_BALLOT_COUNT);
			if (isFinalCount) {
				row.setDiff(finalCount.getRejectedBallotCounts().get(i).getCount() - baseCount.getRejectedBallotCounts().get(i).getCount());
			}
			result.add(row);
			i++;
		}
		// Add SUM rejected
		row = new CompareBallotCountView(finalCount.getTotalRejectedBallotCount()).markBold();
		if (isFinalCount) {
			row.setDiff(finalCount.getTotalRejectedBallotCount() - baseCount.getTotalRejectedBallotCount());
		} else {
			row.setDiff(finalCount.getTotalRejectedBallotCount() - baseCount.getQuestionableBallotCount());
		}
		result.add(row);
		return result;
	}

	public CompareCountsSelect getCurrentCountsSelect() {
		return currentCountsSelect;
	}

	public void setCurrentCountsSelect(CompareCountsSelect countsSelect) {
		this.currentCountsSelect = countsSelect;
	}

	public boolean isCommentRequiredForCurrentCountsSelect() {
		return isCommentRequired(currentCountsSelect.getFinalCount());
	}

	private boolean isCommentRequired(FinalCount finalCount) {
		return getTotalBallotCountDifferenceFromPreviousCount(finalCount) != 0;
	}

	public boolean isCountEditableForCurrentCountsSelect() {
		return isCountEditable(currentCountsSelect.getFinalCount());
	}

	public boolean isConfirmApproveCountDisabledForCurrentCountsSelect() {
		FinalCount finalCount = currentCountsSelect.getFinalCount();
		return isCommentRequired(finalCount) && !finalCount.hasComment();
	}
}
