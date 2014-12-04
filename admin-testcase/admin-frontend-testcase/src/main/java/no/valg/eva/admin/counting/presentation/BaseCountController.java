package no.valg.eva.admin.counting.presentation;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import no.evote.constants.AreaLevelEnum;
import no.evote.exception.EvoteException;
import no.evote.presentation.ConversationScopedController;
import no.evote.presentation.util.MessageUtil;
import no.evote.security.UserData;
import no.valg.eva.admin.common.AreaPath;
import no.valg.eva.admin.common.ElectionPath;
import no.valg.eva.admin.common.counting.model.CountCategory;
import no.valg.eva.admin.common.counting.model.CountContext;
import no.valg.eva.admin.common.counting.service.NewCountingService;

import org.apache.log4j.Logger;

public abstract class BaseCountController extends ConversationScopedController {

	protected static final Logger LOGGER = Logger.getLogger(BaseCountController.class);
	@Inject
	protected UserData userData;
	
	protected transient NewCountingService countingService;
	protected boolean error;
	protected CountCategory countCategory;
	protected ElectionPath contestPath;
	protected AreaPath areaPath;
	private CountContext countContext;

	protected abstract Map getMessageProvider();

	protected void logAndBuildDetailErrorMessage(final Exception e) {
		error = true;
		LOGGER.error(e.getMessage(), e);
		String msg = e.getMessage();

		MessageUtil.buildDetailMessage(FacesMessage.SEVERITY_ERROR, msg);
	}

	public boolean isUserOnCountyLevel() {
		return false;
	}

	public boolean isError() {
		return error;
	}

	protected CountContext initCountContext() {
		return new CountContext(contestPath, countCategory);
	}

	protected NewCountingService getCountingService() {
		return countingService;
	}

	public CountContext getCountContext() {
		return countContext;
	}

	public void setCountContext(CountContext countContext) {
		this.countContext = countContext;
	}
}
