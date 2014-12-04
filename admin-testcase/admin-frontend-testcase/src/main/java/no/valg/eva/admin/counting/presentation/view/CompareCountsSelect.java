package no.valg.eva.admin.counting.presentation.view;

import static java.util.Arrays.asList;
import static no.valg.eva.admin.counting.presentation.ApproveCountOperation.CONFIRM;
import static no.valg.eva.admin.frontend.common.dialog.DialogUtils.getDefaultModalDialogOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.valg.eva.admin.common.counting.model.FinalCount;
import no.valg.eva.admin.counting.presentation.ApproveCountOperation;
import no.valg.eva.admin.counting.presentation.CompareCountsController;
import no.valg.eva.admin.frontend.common.Button;

import org.primefaces.event.SelectEvent;

public class CompareCountsSelect {

	private CompareCountsController ctrl;
	private String id = "";

	public CompareCountsSelect(CompareCountsController ctrl) {
		this.ctrl = ctrl;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void approve() {
		ctrl.approve(this);
	}

	public void openConfirmApproveCountDialog() {
		String dialogPath = "/secure/counting/dialogs/confirmApproveFinalCountDialog";
		Map<String, Object> dialogOptions = getDefaultModalDialogOptions();
		dialogOptions.put("closable", false);
		ctrl.setCurrentCountsSelect(this);
		ctrl.getRequestContext().openDialog(dialogPath, dialogOptions, buildDialogParams());
	}

	private Map<String, List<String>> buildDialogParams() {
		Map<String, List<String>> urlParams = new HashMap<>();
		urlParams.put("cid", asList(ctrl.getCid()));
		return urlParams;
	}

	public void onConfirmApproveCountDialogReturn(SelectEvent event) {
		ctrl.setCurrentCountsSelect(null);
		ApproveCountOperation operation = (ApproveCountOperation) event.getObject();
		if (operation == CONFIRM) {
			approve();
		}
	}

	public void saveComment() {
		ctrl.saveComment(this);
	}

	public void reject() {
		ctrl.reject(this);
	}

	public FinalCount getFinalCount() {
		return ctrl.getCount(this);
	}

	public boolean hasFinalCount() {
		return getFinalCount() != null;
	}

	public boolean isFinalCountApproved() {
		return hasFinalCount() && getFinalCount().isApproved();
	}

	public Button getApproveButton() {
		return ctrl.getApproveButton(this);
	}

	public Button getRejectButton() {
		return ctrl.getRejectButton(this);
	}
}
