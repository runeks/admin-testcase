package no.valg.eva.admin.counting.presentation;

import static no.valg.eva.admin.counting.presentation.ApproveCountOperation.CONFIRM;

import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;

import no.evote.presentation.util.MessageUtil;
import no.valg.eva.admin.common.counting.model.Count;
import no.valg.eva.admin.common.counting.model.DailyMarkOffCount;
import no.valg.eva.admin.common.counting.model.ProtocolAndPreliminaryCount;
import no.valg.eva.admin.counting.presentation.view.DailyMarkOffCountsModel;
import no.valg.eva.admin.counting.presentation.view.MarkOffCountsModel;

import org.primefaces.event.SelectEvent;

@Named
@ConversationScoped
public class ProtocolAndPreliminaryCountController extends CountController {

	@Override
	protected void doInit() {
		super.doInit();
	}

	@Override
	public void saveCount() {
		try {
			getProtocolAndPreliminaryCount().validate();
			setCount(countingService.saveCount(userData, getCountContext(), getProtocolAndPreliminaryCount()));
			MessageUtil.buildDetailMessage(FacesMessage.SEVERITY_INFO, getMessageProvider().get("@count.isSaved"));
		} catch (Exception e) {
			logAndBuildDetailErrorMessage(e);
		}
	}

	public void approveCount() {
		try {
			getProtocolAndPreliminaryCount().validateForApproval();
			setCount(countingService.approveCount(userData, getCountContext(), getProtocolAndPreliminaryCount()));
			MessageUtil.buildDetailMessage(FacesMessage.SEVERITY_INFO, getMessageProvider().get("@count.isApproved"));
		} catch (Exception e) {
			logAndBuildDetailErrorMessage(e);
		}
	}

	@Override
	public void revokeApprovedCount() {
		try {
			setCount(countingService.revokeCount(userData, getCountContext(), getProtocolAndPreliminaryCount()));
			MessageUtil.buildDetailMessage(FacesMessage.SEVERITY_INFO, getMessageProvider().get("@count.isNotApprovedAnymore"));
		} catch (Exception e) {
			logAndBuildDetailErrorMessage(e);
		}
	}

	@Override
	public boolean isApproved() {
		return getCount() != null && getProtocolAndPreliminaryCount().isApproved();
	}

	public ProtocolAndPreliminaryCount getProtocolAndPreliminaryCount() {
		return (ProtocolAndPreliminaryCount) getCount();
	}

	public boolean isSplitBallotCounts() {
		return getProtocolAndPreliminaryCount().isSplitPreliminaryCount();
	}

	@Override
	public Count getCount() {
		return getCounts().getProtocolAndPreliminaryCount();
	}

	@Override
	public void setCount(Count count) {
		getCounts().setProtocolAndPreliminaryCount((ProtocolAndPreliminaryCount) count);
	}

	@Override
	public int getTotalBallotCountDifferenceFromPreviousCount() {
		return getProtocolAndPreliminaryCount().getDifferenceBetweenTotalBallotCountsAndMarkOffCount();
	}

	@Override
	public boolean isElectronicMarkOffs() {
		return getProtocolAndPreliminaryCount().isElectronicMarkOffs();
	}

	@Override
	public List<DailyMarkOffCount> getDailyMarkOffCounts() {
		return getCount() != null ? getProtocolAndPreliminaryCount().getDailyMarkOffCounts() : Collections.<DailyMarkOffCount> emptyList();
	}

	@Override
	public void openConfirmApproveCountDialog() {
		openConfirmApproveCountDialog("/secure/counting/dialogs/confirmApproveProtocolAndPreliminaryCountDialog");
	}

	public boolean isCommentRequired() {
		return getProtocolAndPreliminaryCount().isCommentRequired();
	}

	public boolean isConfirmApproveCountDisabled() {
		return isCommentRequired() && !getProtocolAndPreliminaryCount().hasComment();
	}

	@Override
	public void onConfirmApproveCountDialogReturn(SelectEvent event) {
		ApproveCountOperation operation = (ApproveCountOperation) event.getObject();
		if (operation == CONFIRM) {
			approveCount();
		}
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
