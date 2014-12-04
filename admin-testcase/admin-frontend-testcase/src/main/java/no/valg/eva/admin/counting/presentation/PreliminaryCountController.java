package no.valg.eva.admin.counting.presentation;

import static no.valg.eva.admin.common.counting.model.CountCategory.FO;
import static no.valg.eva.admin.common.counting.model.CountCategory.VO;
import static no.valg.eva.admin.counting.presentation.ApproveCountOperation.CONFIRM;

import java.util.List;

import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;

import no.evote.presentation.util.MessageUtil;
import no.valg.eva.admin.common.counting.model.Count;
import no.valg.eva.admin.common.counting.model.CountQualifier;
import no.valg.eva.admin.common.counting.model.PreliminaryCount;
import no.valg.eva.admin.common.counting.model.ProtocolCount;
import no.valg.eva.admin.common.counting.validator.ApprovePreliminaryCountValidator;
import no.valg.eva.admin.counting.presentation.view.DailyMarkOffCountsModel;
import no.valg.eva.admin.counting.presentation.view.LateValidationCoversModel;
import no.valg.eva.admin.counting.presentation.view.MarkOffCountsModel;

import org.primefaces.event.SelectEvent;

@Named
@ConversationScoped
public class PreliminaryCountController extends CountController {

	private Integer selectedProtocolCountIndex;

	@Override
	protected void doInit() {
		super.doInit();
		selectProtocolCount(null);
	}

	@Override
	public void saveCount() {
		try {
			PreliminaryCount preliminaryCount = getPreliminaryCount();
			preliminaryCount.validate();
			setCount(countingService.saveCount(userData, getCountContext(), preliminaryCount));
			MessageUtil.buildDetailMessage(FacesMessage.SEVERITY_INFO, getMessageProvider().get("@count.isSaved"));
		} catch (Exception e) {
			logAndBuildDetailErrorMessage(e);
		}
	}

	public void approveCount() {
		try {
			PreliminaryCount preliminaryCount = getPreliminaryCount();
			if (preliminaryCount.getCategory() == VO) {
				int totalBallotCountForProtocolCounts = getCounts().getTotalBallotCountForProtocolCounts();
				ApprovePreliminaryCountValidator.forVo(totalBallotCountForProtocolCounts).validate(preliminaryCount);
			} else {
				ApprovePreliminaryCountValidator.forOtherCategoriesThanVo().validate(preliminaryCount);
			}
			setCount(countingService.approveCount(userData, getCountContext(), preliminaryCount));
			MessageUtil.buildDetailMessage(FacesMessage.SEVERITY_INFO, getMessageProvider().get("@count.isApproved"));
		} catch (Exception e) {
			logAndBuildDetailErrorMessage(e);
		}
	}

	@Override
	public void revokeApprovedCount() {
		try {
			setCount(countingService.revokeCount(userData, getCountContext(), getPreliminaryCount()));
			MessageUtil.buildDetailMessage(FacesMessage.SEVERITY_INFO, getMessageProvider().get("@count.isNotApprovedAnymore"));
		} catch (Exception e) {
			logAndBuildDetailErrorMessage(e);
		}
	}

	@Override
	public boolean isApproved() {
		PreliminaryCount preliminaryCount = getPreliminaryCount();
		return preliminaryCount != null && preliminaryCount.isApproved();
	}

	public PreliminaryCount getPreliminaryCount() {
		return (PreliminaryCount) getCount();
	}

	public boolean hasProtocolCounts() {
		return getCounts().hasProtocolCounts();
	}

	public List<ProtocolCount> getProtocolCounts() {
		return getCounts().getProtocolCounts();
	}

	public ProtocolCount getProtocolCount() {
		if (isSelectedProtocolCount()) {
			return getProtocolCounts().get(getSelectedProtocolCountIndex());
		}
		return getProtocolCounts().get(0);
	}

	@Override
	public boolean isIncludeProtocolCount() {
		return getCounts().hasProtocolCounts();
	}

	public boolean isSplitBallotCounts() {
		return getPreliminaryCount().isSplitPreliminaryCount();
	}

	@Override
	public Count getCount() {
		return getCounts().getPreliminaryCount();
	}

	@Override
	public void setCount(Count count) {
		getCounts().setPreliminaryCount((PreliminaryCount) count);
	}

	@Override
	public int getOrdinaryBallotCountDifferenceFromPreviousCount() {
		return getCounts().getOrdinaryBallotCountDifferenceBetween(CountQualifier.PROTOCOL, CountQualifier.PRELIMINARY);
	}

	@Override
	public int getBlankBallotCountDifferenceFromPreviousCount() {
		return getCounts().getBlankBallotCountDifferenceBetween(CountQualifier.PROTOCOL, CountQualifier.PRELIMINARY);
	}

	@Override
	public int getQuestionableBallotCountDifferenceFromPreviousCount() {
		return getCounts().getQuestionableBallotCountDifferenceBetween(CountQualifier.PROTOCOL, CountQualifier.PRELIMINARY);
	}

	@Override
	public int getTotalBallotCountDifferenceFromPreviousCount() {
		if (isIncludeProtocolCount()) {
			return getCounts().getTotalBallotCountDifferenceBetween(CountQualifier.PROTOCOL, CountQualifier.PRELIMINARY);
		}
		return getPreliminaryCount().getTotalBallotCount() - getTotalMarkOffCount();
	}

	@Override
	public int getTotalMarkOffCount() {
		switch (getCountContext().getCategory()) {
		case FO:
			return getCounts().getMarkOffCount() - getPreliminaryCount().getLateValidationCovers();
		case FS:
			return getCounts().getMarkOffCount() + getPreliminaryCount().getLateValidationCovers();
		default:
			return getCounts().getMarkOffCount();
		}
	}

	public boolean renderMultipleProtocolCountsView() {
		return !isSelectedProtocolCount() && hasProtocolCounts() && getProtocolCounts().size() > 1;
	}

	public boolean renderSingleProtocolCountView() {
		return isSelectedProtocolCount() || getProtocolCounts().size() == 1;
	}

	public void selectProtocolCount(Integer selectedProtocolCountIndex) {
		this.selectedProtocolCountIndex = selectedProtocolCountIndex;
	}

	public Integer getSelectedProtocolCountIndex() {
		return selectedProtocolCountIndex;
	}

	public boolean isSelectedProtocolCount() {
		return selectedProtocolCountIndex != null;
	}

	@Override
	public void openConfirmApproveCountDialog() {
		openConfirmApproveCountDialog("/secure/counting/dialogs/confirmApprovePreliminaryCountDialog");
	}

	@Override
	public void onConfirmApproveCountDialogReturn(SelectEvent event) {
		ApproveCountOperation operation = (ApproveCountOperation) event.getObject();
		if (operation == CONFIRM) {
			approveCount();
		}
	}

	public boolean isCommentRequired() {
		if (countCategory == VO) {
			int totalBallotCountForProtocolCounts = getCounts().getTotalBallotCountForProtocolCounts();
			return ApprovePreliminaryCountValidator.forVo(totalBallotCountForProtocolCounts).isCommentRequired(getPreliminaryCount());
		}
		return getPreliminaryCount().isCommentRequired();
	}

	public boolean isConfirmApproveCountDisabled() {
		return isCommentRequired() && !getPreliminaryCount().hasComment();
	}

	public LateValidationCoversModel getLateValidationCoversModel() {
		if (isIncludeMarkOffCount() && getCounts().getContext().getCategory().equals(FO)) {
			return new LateValidationCoversModel(this);
		}
		return null;
	}

	public DailyMarkOffCountsModel getDailyMarkOffCountsModel() {
		if (!getDailyMarkOffCounts().isEmpty()) {
			return new DailyMarkOffCountsModel(this);
		}
		return null;
	}

	public MarkOffCountsModel getMarkOffCountsModel() {
		return new MarkOffCountsModel(this);
	}
}
