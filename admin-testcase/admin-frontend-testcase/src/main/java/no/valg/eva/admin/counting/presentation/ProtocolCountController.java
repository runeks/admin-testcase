package no.valg.eva.admin.counting.presentation;

import static no.valg.eva.admin.counting.presentation.ApproveCountOperation.CONFIRM;
import static no.valg.eva.admin.frontend.common.Button.notRendered;
import static no.valg.eva.admin.frontend.common.Button.renderedAndEnabled;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

import no.evote.presentation.util.MessageUtil;
import no.valg.eva.admin.common.counting.model.Count;
import no.valg.eva.admin.common.counting.model.Counts;
import no.valg.eva.admin.common.counting.model.DailyMarkOffCount;
import no.valg.eva.admin.common.counting.model.ProtocolCount;
import no.valg.eva.admin.frontend.common.Button;
import no.valg.eva.admin.frontend.common.ButtonType;

import org.primefaces.event.SelectEvent;

@Named
@ConversationScoped
public class ProtocolCountController extends CountController {

	@Inject
	private PreliminaryCountController preliminaryCountController;

	@Override
	protected void doInit() {
		super.doInit();
	}

	@Override
	public void saveCount() {
		try {
			ProtocolCount protocolCount = getProtocolCount();
			protocolCount.validate();
			setCount(countingService.saveCount(userData, getCountContext(), protocolCount));
			MessageUtil.buildDetailMessage(FacesMessage.SEVERITY_INFO, getMessageProvider().get("@count.isSaved"));
		} catch (Exception e) {
			logAndBuildDetailErrorMessage(e);
		}
	}

	public void approveCount() {
		try {
			ProtocolCount protocolCount = getProtocolCount();
			protocolCount.validateForApproval();
			setCount(countingService.approveCount(userData, getCountContext(), protocolCount));
			MessageUtil.buildDetailMessage(FacesMessage.SEVERITY_INFO, getMessageProvider().get("@count.isApproved"));
		} catch (Exception e) {
			logAndBuildDetailErrorMessage(e);
		}
	}

	@Override
	public void revokeApprovedCount() {
		try {
			setCount(countingService.revokeCount(userData, getCountContext(), getProtocolCount()));
			MessageUtil.buildDetailMessage(FacesMessage.SEVERITY_INFO, getMessageProvider().get("@count.isNotApprovedAnymore"));
		} catch (Exception e) {
			logAndBuildDetailErrorMessage(e);
		}
	}

	@Override
	public boolean isApproved() {
		ProtocolCount protocolCount = getProtocolCount();
		return protocolCount != null && protocolCount.isApproved();
	}

	public ProtocolCount getProtocolCount() {
		return (ProtocolCount) getCount();
	}

	@Deprecated
	public void setProtocolCount(ProtocolCount protocolCount) {
		setCount(protocolCount);
	}

	@Override
	public Button button(ButtonType type) {
		if (type == ButtonType.BACK) {
			return preliminaryCountController.isSelectedProtocolCount() ? renderedAndEnabled() : notRendered();
		}
		return super.button(type);
	}

	@Override
	public boolean isSplitBallotCounts() {
		return false;
	}

	@Override
	public Count getCount() {
		if (preliminaryCountController.isSelectedProtocolCount()) {
			return getCounts().getProtocolCounts().get(preliminaryCountController.getSelectedProtocolCountIndex());
		}
		return getCounts().getFirstProtocolCount();
	}

	@Override
	public void setCount(Count count) {
		Counts counts = getCounts();
		if (preliminaryCountController.isSelectedProtocolCount()) {
			counts.getProtocolCounts().set(preliminaryCountController.getSelectedProtocolCountIndex(), (ProtocolCount) count);
		} else {
			counts.setFirstProtocolCount((ProtocolCount) count);
		}
	}

	@Override
	public int getTotalBallotCountDifferenceFromPreviousCount() {
		return getProtocolCount().getDifferenceBetweenTotalBallotCountsAndMarkOffCount();
	}

	@Override
	public boolean isElectronicMarkOffs() {
		return getProtocolCount().isElectronicMarkOffs();
	}

	public List<DailyMarkOffCount> getDailyMarkOffCounts() {
		return getProtocolCount().getDailyMarkOffCounts();
	}

	@Override
	public void back() {
		preliminaryCountController.selectProtocolCount(null);
	}

	@Override
	public String getDisplayAreaName() {
		if (getCounts().getProtocolCounts().size() > 1 && !preliminaryCountController.isSelectedProtocolCount()) {
			return getMessageProvider().get("@area.polling_district.municipality");
		}
		return getCount().getAreaPath().getPollingDistrictId() + " " + getCount().getAreaName();
	}

	@Override
	public void openConfirmApproveCountDialog() {
		openConfirmApproveCountDialog("/secure/counting/dialogs/confirmApproveProtocolCountDialog");
	}

	public boolean isCommentRequired() {
		return getProtocolCount().isCommentRequired();
	}

	public boolean isConfirmApproveCountDisabled() {
		return isCommentRequired() && !getProtocolCount().hasComment();
	}

	@Override
	public void onConfirmApproveCountDialogReturn(SelectEvent event) {
		ApproveCountOperation operation = (ApproveCountOperation) event.getObject();
		if (operation == CONFIRM) {
			approveCount();
		}
	}

	public String getProtocolBallotCountsHeader() {
		if (getProtocolCount().getBallotCountForOtherContests() == null) {
			return getMessageProvider().get("@count.votes.contentsPolls");
		} else {
			return getMessageProvider().get("@count.votes.contentsPolls") + " (" + getMessageProvider().get("@area_level[4].name") + " "
					+ getCounts().getContestName() + ")";
		}
	}

	public List<ProtocolBallotCount> getProtocolBallotCounts() {
		List<ProtocolBallotCount> result = new ArrayList<>();
		result.add(new OrdinaryCount());
		if (getProtocolCount().getBlankBallotCount() != null) {
			result.add(new BlankCount());
		}
		result.add(new QuestionableCount());
		return result;
	}

	public List<ProtocolCount> getBallotCountForOtherContests() {
		if (getProtocolCount().getBallotCountForOtherContests() != null) {
			return Arrays.asList(getProtocolCount());
		}
		return Collections.emptyList();
	}

	public interface ProtocolBallotCount {
		String getTitle();

		int getValue();

		void setValue(int value);
	}

	public class OrdinaryCount implements ProtocolBallotCount {
		@Override
		public String getTitle() {
			return "@count.votes.ordinary";
		}

		@Override
		public int getValue() {
			return getProtocolCount().getOrdinaryBallotCount();
		}

		@Override
		public void setValue(int value) {
			getProtocolCount().setOrdinaryBallotCount(value);
		}
	}

	public class BlankCount implements ProtocolBallotCount {
		@Override
		public String getTitle() {
			return "@count.votes.blanc";
		}

		@Override
		public int getValue() {
			return getProtocolCount().getBlankBallotCount();
		}

		@Override
		public void setValue(int value) {
			getProtocolCount().setBlankBallotCount(value);
		}
	}

	public class QuestionableCount implements ProtocolBallotCount {
		@Override
		public String getTitle() {
			return "@count.votes.questionable";
		}

		@Override
		public int getValue() {
			return getProtocolCount().getQuestionableBallotCount();
		}

		@Override
		public void setValue(int value) {
			getProtocolCount().setQuestionableBallotCount(value);
		}
	}
}
