package no.evote.presentation.exceptions;

import no.evote.exception.EvoteException;
import org.apache.log4j.Logger;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.persistence.EntityNotFoundException;
import javax.persistence.OptimisticLockException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

/**
 * Catches all exceptions and redirects to error page.
 */
@SuppressWarnings("deprecation")
public class CustomExceptionHandler extends ExceptionHandlerWrapper {
	private final ExceptionHandler wrapped;
	private final Logger logger = Logger.getLogger(CustomExceptionHandler.class);

	public CustomExceptionHandler(final ExceptionHandler wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public ExceptionHandler getWrapped() {
		return this.wrapped;
	}

	@Override
	public void handle() {
		Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator();
		if (i.hasNext()) {
			// Default error handling renders the errorpage.html file found in /resources.
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
			HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

			// Get the exception
			ExceptionQueuedEvent event = i.next();
			ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
			Throwable t = context.getException();
			String stackTrace = getStackTraceAsString(t);
			String message = t.getMessage();

			// Unwrapping of known exceptions to give better error messages
			Throwable cause = null;
			if (t instanceof FacesException) {
				cause = t.getCause();
			}

			if (isJPAException(cause)) {
				t = cause.getCause();
				stackTrace = "";
				message = cause.getMessage();
			} else if (isWrappedFacesException(t, cause)) {
				t = t.getCause().getCause();
				stackTrace = getStackTraceAsString(t);
				message = t.getMessage();
				cause = t;
			} else if (isNotFoundException(cause)) {
				render404(request, response);
				facesContext.responseComplete();
				return;
			}

			if (!externalContext.isResponseCommitted()) {
				try {
					if (cause != null) {
						logger.warn(cause.getMessage(), cause);
					}
					ErrorPageRenderer.render500Error(request, response, message, stackTrace);
				} catch (IOException e) {
					logger.fatal(e.getMessage(), e);
				}
				facesContext.responseComplete();
				return;
			} else {
				// If the response has been committed (i.e. HTTP headers have been written, etc.), there's not much we can do
				logger.fatal(t.getMessage(), t);
			}
		}

		getWrapped().handle();
	}

	private boolean isNotFoundException(final Throwable cause) {
		return cause instanceof FileNotFoundException;
	}

	private boolean isWrappedFacesException(final Throwable t, final Throwable cause) {
		return cause instanceof FacesException && (t.getCause() instanceof EvaluationException || t.getCause() instanceof ELException)
				&& t.getCause().getCause() instanceof EvoteException;
	}

	private boolean isJPAException(final Throwable cause) {
		return cause instanceof EvoteException
				&& (cause.getCause() instanceof OptimisticLockException || cause.getCause() instanceof EntityNotFoundException);
	}

	private void render404(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			ErrorPageRenderer.render404Error(request, response);
		} catch (IOException e) {
			logger.fatal(e.getMessage(), e);
		}
	}

	private String getStackTraceAsString(final Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		return sw.toString();
	}
}
