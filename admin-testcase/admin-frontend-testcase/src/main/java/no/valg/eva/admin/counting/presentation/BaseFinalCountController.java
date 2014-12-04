package no.valg.eva.admin.counting.presentation;

import static no.valg.eva.admin.frontend.common.Button.notRendered;
import static no.valg.eva.admin.frontend.common.Button.rendered;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import no.evote.constants.SecObjAccess;
import no.evote.presentation.util.MessageUtil;
import no.valg.eva.admin.common.counting.constants.ReportingUnitTypeId;
import no.valg.eva.admin.common.counting.model.BallotCount;
import no.valg.eva.admin.common.counting.model.BallotCountRef;
import no.valg.eva.admin.common.counting.model.Count;
import no.valg.eva.admin.common.counting.model.CountQualifier;
import no.valg.eva.admin.common.counting.model.FinalCount;
import no.valg.eva.admin.common.counting.model.RejectedBallotCount;
import no.valg.eva.admin.common.counting.service.ModifiedBallotBatchService;
import no.valg.eva.admin.frontend.common.Button;
import no.valg.eva.admin.frontend.common.ButtonType;

public abstract class BaseFinalCountController extends CountController {

	protected static final String COUNTING_PATH = "/secure/counting/";

	private ModifiedBallotBatchService modifiedBallotBatchService;

	private boolean hasModifiedBallotBatchForCurrentCount;

	abstract List<FinalCount> getFinalCounts();

	abstract ReportingUnitTypeId getReportingUnitTypeId();

	abstract int getFinalCountIndex();

	abstract void updateCounts(int index, FinalCount finalCount);

	@Override
	protected void doInit() {
		super.doInit();
		updateHasModifiedBallotBatchForCurrentCount();
		if (isUserOnCountyLevel() && this instanceof FinalCountController && !isApproved()) {
			// TODO
			MessageUtil.buildDetailMessage(FacesMessage.SEVERITY_INFO, "Kommunen har ikke godkjent endelig telling!");
		}
	}

	@Override
	public void saveCount() {
		try {
			FinalCount count = getFinalCount();
			count.validate();
			count = getCountingService().saveCount(userData, getCountContext(), count, getReportingUnitTypeId());
			updateCounts(getFinalCountIndex(), count);
			MessageUtil.buildDetailMessage(FacesMessage.SEVERITY_INFO, getMessageProvider().get("@count.isSaved"));
		} catch (Exception e) {
			logAndBuildDetailErrorMessage(e);
		}
	}

	@Override
	public void modifiedBallotProcessed() {
		saveCount();
		FinalCount finalCount = getFinalCount();
		finalCount.setModifiedBallotsProcessed(true);
		finalCount = countingService.saveCount(userData, getContext(), finalCount, getReportingUnitTypeId());
		updateCounts(getFinalCountIndex(), finalCount);
	}

	public String saveCountAndRegisterCountCorrections() {
		// Lagrer først telling da stikke-prosessen er avhengig av at tellingen faktisk er lagra i databasen
		saveCount();
		return COUNTING_PATH + "modifiedBallotsStatus.xhtml?faces-redirect=true";
	}

	@Override
	public Count getCount() {
		if (getFinalCounts() == null || getFinalCounts().isEmpty()) {
			return null;
		}
		return getFinalCounts().get(getFinalCountIndex());
	}

	public FinalCount getFinalCount() {
		return (FinalCount) getCount();
	}

	public boolean isReferendum() {
		return getCount() != null;
	}

	public void newFinalCount() {
		int newIndex = getFinalCounts().size();
		// TODO runeks spør gelu: id setting kan bli feil her, om to klienter gjør dette samtidig vil det bli krøll på serversiden..
		// TODO runeks bør sette final count i en constructor her som tar inn final count verdier skal tas fra
		String id = "E" + getContext().getCategory().getId() + (newIndex + 1);
		FinalCount newFinalCount = createNewFinalCount();
		newFinalCount.setId(id);
		updateCounts(newIndex, newFinalCount);
	}

	public FinalCount createNewFinalCount() {
		FinalCount originalFinalCount = getFinalCounts().get(0);
		FinalCount newFinalCount =
				new FinalCount(
						null,
						originalFinalCount.getAreaPath(),
						originalFinalCount.getCategory(),
						originalFinalCount.getAreaName(),
						originalFinalCount.getReportingUnitAreaName(),
						originalFinalCount.isManualCount());
		List<BallotCount> newBallotCounts = new ArrayList<>();
		for (BallotCount ballotCount : originalFinalCount.getBallotCounts()) {
			BallotCount newBallotCount = new BallotCount(ballotCount.getId(), ballotCount.getName(), 0, 0);
			newBallotCounts.add(newBallotCount);
		}
		newFinalCount.setBallotCounts(newBallotCounts);
		List<RejectedBallotCount> newRejectedBallotCounts = new ArrayList<>();
		for (RejectedBallotCount rejectedBallotCount : originalFinalCount.getRejectedBallotCounts()) {
			RejectedBallotCount newRejectedBallotCount = new RejectedBallotCount(rejectedBallotCount.getId(), rejectedBallotCount.getName(), 0);
			newRejectedBallotCounts.add(newRejectedBallotCount);
		}
		newFinalCount.setRejectedBallotCounts(newRejectedBallotCounts);
		return newFinalCount;
	}

	@Override
	public Button button(ButtonType type) {
		FinalCount count = getFinalCount();
		switch (type) {
		case REVOKE:
			return notRendered();
		case APPROVE:
			return notRendered();
		case REGISTER_CORRECTIONS:
			if (!hasRegisterCorrectionsAccess() || !hasCorrections()) {
				return notRendered();
			}
			if (count.isEditable() && !count.isModifiedBallotsProcessed() && !isApproved() && isPreviousApproved()) {
				return rendered(true);
			}
			return notRendered();
		case MODIFIED_BALLOT_PROCESSED:
			if (!hasWriteAccess() || hasCorrections()) {
				return notRendered();
			}
			if (count.isEditable() && !count.isModifiedBallotsProcessed() && !isApproved() && isPreviousApproved()) {
				return rendered(true);
			}
			return notRendered();
		default:
			return super.button(type);
		}
	}

	boolean hasRegisterCorrectionsAccess() {
		return getUserDataController().hasAccess(SecObjAccess.COUNT_REGISTER_CORRECTED);
	}

	@Override
	public boolean isIncludeMarkOffCount() {
		return false;
	}

	@Override
	public boolean isSplitBallotCounts() {
		return true;
	}

	@Override
	public int getOrdinaryBallotCountDifferenceFromPreviousCount() {
		return getCounts().getOrdinaryBallotCountDifferenceBetween(CountQualifier.PRELIMINARY, CountQualifier.FINAL, getFinalCount().getId());
	}

	@Override
	public int getBlankBallotCountDifferenceFromPreviousCount() {
		return getCounts().getBlankBallotCountDifferenceBetween(CountQualifier.PRELIMINARY, CountQualifier.FINAL, getFinalCount().getId());
	}

	@Override
	public int getQuestionableBallotCountDifferenceFromPreviousCount() {
		return getCounts().getQuestionableBallotCountDifferenceBetween(CountQualifier.PRELIMINARY, CountQualifier.FINAL, getFinalCount().getId());
	}

	@Override
	public int getTotalBallotCountDifferenceFromPreviousCount() {
		return getCounts().getTotalBallotCountDifferenceBetween(CountQualifier.PRELIMINARY, CountQualifier.FINAL, getFinalCount().getId());
	}

	boolean hasCorrections() {
		for (BallotCount count : getFinalCount().getBallotCounts()) {
			if (count.getModifiedCount() > 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isCountEditable() {
		return super.isCountEditable() && !hasModifiedBallotBatchForCurrentCount && !getFinalCount().isModifiedBallotsProcessed();
	}

	void updateHasModifiedBallotBatchForCurrentCount() {
		if (getFinalCount() == null || getFinalCount().isNew()) {
			hasModifiedBallotBatchForCurrentCount = false;
			return;
		}
		List<BallotCountRef> pks = new ArrayList<>();
		for (BallotCount bc : getFinalCount().getBallotCounts()) {
			pks.add(bc.getBallotCountRef());
		}
		hasModifiedBallotBatchForCurrentCount = modifiedBallotBatchService.hasModifiedBallotBatchForBallotCountPks(pks);
	}
}
