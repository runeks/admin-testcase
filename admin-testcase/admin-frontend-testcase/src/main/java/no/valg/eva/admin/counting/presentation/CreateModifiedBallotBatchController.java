package no.valg.eva.admin.counting.presentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

import no.evote.exception.ModifiedBallotBatchCreationFailed;
import no.evote.presentation.ConversationScopedController;
import no.evote.presentation.util.FacesUtil;
import no.evote.presentation.util.MessageUtil;
import no.evote.security.UserData;
import no.valg.eva.admin.common.counting.model.BallotCount;
import no.valg.eva.admin.common.counting.model.BatchId;
import no.valg.eva.admin.common.counting.model.modifiedballots.ModifiedBallotBatch;
import no.valg.eva.admin.common.counting.service.ModifiedBallotBatchService;
import no.valg.eva.admin.frontend.common.dialog.DialogUtils;

@SuppressWarnings("CdiNormalScopeInspection")
@Named
@ConversationScoped
public class CreateModifiedBallotBatchController extends ConversationScopedController {

	public static final String REGISTER_MODIFIED_BALLOTS_PAGE = "/secure/counting/registerModifiedBallot.xhtml";
	public static final String SHOW_MODIFIED_BALLOTS_PAGE = "/secure/counting/showModifiedBallot.xhtml";
	public static final String CREATE_MODIFIED_BALLOTS_BATCH_PAGE = "includes/createModifiedBallotBatchDialog";
	public static final String BALLOT_COUNT_REF = "ballotCountRef";

	@Inject
	protected UserData userData;
	protected ModifiedBallotBatchService modifiedBallotBatchService;
	@Inject
	protected Map<String, String> messageProvider;

	private Integer modifiedBallotBatchSize;
	private BallotCount ballotCount;
	private ModifiedBallotBatch modifiedBallotBatch;

	@Override
	protected void doInit() {
		// No init currently needed
	}

	public void showModifiedBallotBatchDialog(BallotCount ballotCount) {
		String dialogPath = CREATE_MODIFIED_BALLOTS_BATCH_PAGE;
		modifiedBallotBatchSize = null;
		this.ballotCount = ballotCount;
		openDialog(dialogPath, getDialogParams());
	}

	private Map<String, List<String>> getDialogParams() {
		Map <String, List<String>> urlParams = new HashMap<>();
		List<String> cidParam = new ArrayList<>();
		cidParam.add(this.getConversation().getId());
		urlParams.put("cid", cidParam);
		return urlParams;
	}

	protected void openDialog(String dialogPath, Map<String, List<String>> dialogParams) {
		Map<String, Object> dialogOptions = DialogUtils.getDefaultModalDialogOptions();
		dialogOptions.put("onHide", "function(){PF('poller').start();}");
		DialogUtils.openDialog(dialogPath, dialogOptions, dialogParams);
	}

	public void initiateModifiedBallotBatch() {
		try {
			modifiedBallotBatch = modifiedBallotBatchService.createModifiedBallotBatch(userData, ballotCount, getModifiedBallotBatchSizeNonNull());
			String registerModifiedBallotUrl = buildRegisterModifiedBallotUrl(modifiedBallotBatch.getBatchId());
			redirectTo(registerModifiedBallotUrl);
		} catch (ModifiedBallotBatchCreationFailed e) {
			buildErrorMessage(e);
		}
	}

	public int getModifiedBallotBatchSizeNonNull() {
		return modifiedBallotBatchSize == null ? 0 : modifiedBallotBatchSize;
	}

	private String buildRegisterModifiedBallotUrl(BatchId modifiedBallotBatchId) {
		return REGISTER_MODIFIED_BALLOTS_PAGE + "?modifiedBallotBatchId=" + modifiedBallotBatchId.getId() + "&cid=" + getConversation().getId();
	}

	protected void redirectTo(String urlString) {
		FacesUtil.redirect(urlString, false);
	}

	protected void buildErrorMessage(ModifiedBallotBatchCreationFailed e) {
		MessageUtil.buildDetailMessage(FacesMessage.SEVERITY_ERROR,
				messageProvider.get("@count.votes.cast.batch.size.invalid"));
	}

	public Integer getModifiedBallotBatchSize() {
		return modifiedBallotBatchSize;
	}

	public void setModifiedBallotBatchSize(Integer modifiedBallotBatchSize) {
		this.modifiedBallotBatchSize = modifiedBallotBatchSize;
	}

	public String getPartyTextId() {
		return "@party[" + ballotCount.getId() + "].name";
	}

	public BatchId getModifiedBallotBatchId() {
		return modifiedBallotBatch.getBatchId();
	}

	public ModifiedBallotBatch getModifiedBallotBatch() {
		return modifiedBallotBatch;
	}

	public void goToShowModifiedBallot(BallotCount ballotCount) {
		redirectTo(SHOW_MODIFIED_BALLOTS_PAGE + "?" + BALLOT_COUNT_REF + "=" + ballotCount.getBallotCountRef().getPk() + "&cid=" + getConversation().getId());
	}
}
