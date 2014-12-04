package no.valg.eva.admin.counting.presentation;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static no.valg.eva.admin.frontend.common.Button.notRendered;
import static no.valg.eva.admin.frontend.common.Button.rendered;
import static no.valg.eva.admin.frontend.common.dialog.DialogUtils.getDefaultModalDialogOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import no.evote.constants.AreaLevelEnum;
import no.evote.constants.SecObjAccess;
import no.evote.presentation.UserDataController;
import no.valg.eva.admin.common.counting.model.Count;
import no.valg.eva.admin.common.counting.model.CountContext;
import no.valg.eva.admin.common.counting.model.Counts;
import no.valg.eva.admin.common.counting.model.DailyMarkOffCount;
import no.valg.eva.admin.counting.presentation.view.ballotcount.BallotCountsModel;
import no.valg.eva.admin.frontend.common.Button;
import no.valg.eva.admin.frontend.common.ButtonType;

import org.primefaces.event.SelectEvent;

public abstract class CountController extends BaseCountController implements TopInfoProvider {

	private static final String CONTROLLER_DOES_NOT_SUPPORT_THIS_OPERATION = "controller does not support this operation";

	@Inject
	protected StartCountingController startCountingController;
	@Inject
	private UserDataController userDataController;

	private int tabIndex;

	public CountController() {
	}

	@Override
	protected void doInit() {
		countCategory = startCountingController.getCountCategory();
		contestPath = startCountingController.getContestPath();
		areaPath = startCountingController.getAreaPath();
		setCountContext(initCountContext());
	}

	public Button button(ButtonType type) {
		switch (type) {
		case SAVE:
			if (hasWriteAccess()) {
				return rendered(isCountEditable());
			}
		case APPROVE:
			if (hasWriteAccess()) {
				return rendered(isCountEditable());
			}
		case REVOKE:
			if (hasRevokeAccess()) {
				return rendered(isApproved() && !isNextApproved());
			}
		default:
			return notRendered();
		}
	}

	/**
	 * @return true if user can edit, else false
	 */
	protected boolean hasWriteAccess() {
		return getUserDataController().hasAccess(SecObjAccess.COUNT_ADVANCE_VOTES_EDIT)
				|| getUserDataController().hasAccess(SecObjAccess.COUNT_ELECTION_DAY_VOTES_EDIT);
	}

	/**
	 * @return true if user can revoke (oppheve godkjenning) protocol or preliminary count, else false
	 */
	protected boolean hasRevokeAccess() {
		return getUserDataController().hasAccess(SecObjAccess.COUNT_REVOKE_PRELIMINARY);
	}

	protected boolean hasRejectAccess() {
		return getUserDataController().hasAccess(SecObjAccess.COUNT_REVOKE_FINAL);
	}

	@Override
	public Map<String, String> getMessageProvider() {
		return new HashMap<>();
	}

	/**
	 * Convenient for test
	 */
	UserDataController getUserDataController() {
		return userDataController;
	}

	@Override
	public String getDisplayAreaName() {
		if (getCount().getAreaPath().isMunicipalityPollingDistrict()) {
			return getMessageProvider().get("@area.polling_district.municipality");
		} else {
			return (getCount().getAreaPath().getLevel().equals(AreaLevelEnum.POLLING_DISTRICT) ? getCount().getAreaPath().getPollingDistrictId()
					: getCount().getAreaPath().getBoroughId())
					+ " " + getCount().getAreaName();
		}
	}

	public Counts getCounts() {
		return startCountingController.getCounts();
	}

	public void back() {
	}

	public void revokeApprovedCount() {
		throw new UnsupportedOperationException(CONTROLLER_DOES_NOT_SUPPORT_THIS_OPERATION);
	}

	public void modifiedBallotProcessed() {
		throw new UnsupportedOperationException(CONTROLLER_DOES_NOT_SUPPORT_THIS_OPERATION);
	}

	public boolean isRejected() {
		throw new UnsupportedOperationException(CONTROLLER_DOES_NOT_SUPPORT_THIS_OPERATION);
	}

	public boolean isElectronicMarkOffs() {
		return false;
	}

	public List<DailyMarkOffCount> getDailyMarkOffCounts() {
		return Collections.emptyList();
	}

	public boolean isIncludeMarkOffCount() {
		return true;
	}

	public boolean isIncludeProtocolCount() {
		return false;
	}

	public int getOrdinaryBallotCountDifferenceFromPreviousCount() {
		return 0;
	}

	public int getBlankBallotCountDifferenceFromPreviousCount() {
		return 0;
	}

	public int getQuestionableBallotCountDifferenceFromPreviousCount() {
		return 0;
	}

	public int getTotalMarkOffCount() {
		return getCounts().getMarkOffCount();
	}

	public CountContext getContext() {
		return getCounts().getContext();
	}

	public String saveCountAndRegisterCountCorrections() {
		throw new UnsupportedOperationException(CONTROLLER_DOES_NOT_SUPPORT_THIS_OPERATION);
	}

	public void openConfirmApproveCountDialog() {
		throw new IllegalStateException(format("not implemented for %s", this.getClass()));
	}

	void openConfirmApproveCountDialog(String dialogPath) {
		Map<String, Object> dialogOptions = getDefaultModalDialogOptions();
		dialogOptions.put("closable", false);
		getRequestContext().openDialog(dialogPath, dialogOptions, getDialogParams());
	}

	public void closeConfirmApproveCountDialog(ApproveCountOperation operation) {
		getRequestContext().closeDialog(operation);
	}

	public void onConfirmApproveCountDialogReturn(SelectEvent event) {
		throw new IllegalStateException(format("not implemented for %s", this.getClass()));
	}

	public boolean isCountEditable() {
		return getCount().isEditable() && !isApproved() && isPreviousApproved() && !isNextApproved();
	}

	public void saveCount() {
		throw new UnsupportedOperationException(CONTROLLER_DOES_NOT_SUPPORT_THIS_OPERATION);
	}

	public boolean isApproved() {
		throw new UnsupportedOperationException(CONTROLLER_DOES_NOT_SUPPORT_THIS_OPERATION);
	}

	public boolean isSplitBallotCounts() {
		throw new UnsupportedOperationException(CONTROLLER_DOES_NOT_SUPPORT_THIS_OPERATION);
	}

	public Count getCount() {
		throw new UnsupportedOperationException(CONTROLLER_DOES_NOT_SUPPORT_THIS_OPERATION);
	}

	public void setCount(Count count) {
		throw new UnsupportedOperationException(CONTROLLER_DOES_NOT_SUPPORT_THIS_OPERATION);
	}

	public int getTotalBallotCountDifferenceFromPreviousCount() {
		throw new UnsupportedOperationException(CONTROLLER_DOES_NOT_SUPPORT_THIS_OPERATION);
	}

	public int getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(int tabIndex) {
		this.tabIndex = tabIndex;
	}

	public CountController getPreviousController() {
		if (startCountingController.getTabs().isEmpty() || getTabIndex() <= 0) {
			return null;
		}
		return startCountingController.getTabs().get(getTabIndex() - 1).getController();
	}

	public boolean isPreviousApproved() {
		CountController ctrl = getPreviousController();
		return ctrl == null || ctrl.isApproved();
	}

	public CountController getNextController() {
		if (startCountingController.getTabs().isEmpty() || getTabIndex() >= startCountingController.getTabs().size() - 1) {
			return null;
		}
		return startCountingController.getTabs().get(getTabIndex() + 1).getController();
	}

	public boolean isNextApproved() {
		CountController ctrl = getNextController();
		return ctrl != null && ctrl.isApproved();
	}

	Map<String, List<String>> getDialogParams() {
		Map<String, List<String>> urlParams = new HashMap<>();
		urlParams.put("cid", asList(getCid()));
		return urlParams;
	}

	public Tab getPreviousTab() {
		CountController previous = getPreviousController();
		if (previous != null) {
			return startCountingController.getTabs().get(previous.getTabIndex());
		}
		return null;
	}

	public Tab getTab() {
		return startCountingController.getTabs().get(getTabIndex());
	}

	public BallotCountsModel getBallotCountsModel() {
		return new BallotCountsModel(this);
	}

	@Override
	public String getMunicipalityName() {
		return getCounts().getMunicipalityName();
	}

	@Override
	public String getElectionName() {
		return getCounts().getElectionName();
	}

	@Override
	public String getCategoryName() {
		return getCountContext().getCategoryName();
	}
}
