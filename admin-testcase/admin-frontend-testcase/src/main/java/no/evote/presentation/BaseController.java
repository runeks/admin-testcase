package no.evote.presentation;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import no.evote.presentation.util.MessageUtil;

import org.primefaces.context.RequestContext;

/**
 * Common controller type. Useful for adding common annotations or functionality to all controller instances.
 */
public abstract class BaseController implements Serializable {

	@Inject
	private RequestContextBroker requestContextBroker;
	@Inject
	private FacesContextBroker facesContextBroker;

	@PostConstruct
	public void initBaseController() {
		MessageUtil.clearMessages();
	}

	public RequestContext getRequestContext() {
		return requestContextBroker.getContext();
	}

	protected FacesContext getFacesContext() {
		return facesContextBroker.getContext();
	}
}
