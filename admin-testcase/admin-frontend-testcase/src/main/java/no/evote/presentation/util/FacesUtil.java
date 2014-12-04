package no.evote.presentation.util;

import java.io.IOException;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.evote.model.BinaryData;
import no.evote.security.UserData;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

public final class FacesUtil {
	private static final Logger LOG = Logger.getLogger(FacesUtil.class);
	private static final String DEFAULT_CONTENTTYPE = "application/force-download";
	private static FacesContext context;

	private FacesUtil() {
	}

	public static Object resolveExpression(final String expression) {
		FacesContext facesContext = getContext();
		Application app = facesContext.getApplication();
		ExpressionFactory elFactory = app.getExpressionFactory();
		ELContext elContext = facesContext.getELContext();
		ValueExpression valueExp = elFactory.createValueExpression(elContext, expression, Object.class);
		return valueExp.getValue(elContext);
	}

	public static UserData getUserData() {
		FacesContext context = getContext();
		return context.getApplication().evaluateExpressionGet(context, "#{userDataProducer.userData}", UserData.class);
	}

	public static void sendFile(final String filename, final byte[] data) throws IOException {
		sendFile(filename, data, DEFAULT_CONTENTTYPE);
	}

	public static void sendFile(final String filename, final byte[] data, final String contentType) throws IOException {
		FacesContext facesContext = getContext();
		ExternalContext context = facesContext.getExternalContext();
		context.responseReset();
		HttpServletResponse response = (HttpServletResponse) context.getResponse();
		response.setContentType(contentType);
		response.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

		ServletOutputStream out = response.getOutputStream();
		response.setContentLength(data.length);
		out.write(data);
		out.close();

		facesContext.responseComplete();
	}

	public static void sendFile(final BinaryData binaryData) throws IOException {
		sendFile(binaryData.getFileName(), binaryData.getBinaryData(), binaryData.getMimeType());
	}

	public static void setSessionAttribute(final String key, final Object value) {
		getSession().setAttribute(key, value);
	}

	private static HttpSession getSession() {
		return ((HttpSession) getContext().getExternalContext().getSession(false));
	}

	public static Object getSessionAttribute(final String key) {
		return getSession().getAttribute(key);
	}

	public static void redirect(final String urlString, final boolean encode) {
		FacesContext ctx = getContext();
		ExternalContext extContext = ctx.getExternalContext();
		String url;
		if (encode) {
			url = extContext.encodeActionURL(ctx.getApplication().getViewHandler().getActionURL(ctx, urlString));
		} else {
			url = urlString;
		}
		LOG.info("Got " + urlString + ",  Redirecting to " + url);

		try {
			extContext.redirect(url);
		} catch (IOException ioe) {
			throw new FacesException(ioe);
		}
	}

	/**
	 * Given an object, test if it's String or Integer and either return the parsed integer value or cast it to Integer
	 *
	 * @param object An object that is either an String representation of an integer, or an Integer
	 * @return The integer value of the object
	 */
	public static int getIntFromStringOrInteger(final Object object) {
		if (object instanceof String) {
			return Integer.parseInt((String) object);
		} else if (object instanceof Integer) {
			return Integer.class.cast(object).intValue();
		} else {
			throw new IllegalStateException("Object is of unknown type, unable to convert to integer value");
		}
	}

	public static ServletContext getServletContext() {
		FacesContext facesContext = getContext();
		ExternalContext externalContext = facesContext.getExternalContext();
		return (ServletContext) externalContext.getContext();
	}

	private static FacesContext getContext() {
		if (context != null) {
			return context;
		}
		return FacesContext.getCurrentInstance();
	}

	/*
	 * Setters only used for mocking in tests
	 */
	public static void setContext(final FacesContext context) {
		FacesUtil.context = context;
	}

	/**
	 * Helper for updating DOM across contexts when using PrimeFaces dialog framework (DF). Since DF is scoped into a unique "primefaces dialog conversation"
	 * pfdlgcid in short, the page behind the dialog is not reachable from server-side code. This method is intended to solve this problem. - \o/ Go PrimeFaces! \o/
	 *
	 * @param sourceComponent The component that "sends" the event e.g. myFormId:myPanel:myAwesomeButton
	 * @param update          The component client id that you want to update. e.g. myFormId:myPanel:myAwesomeInputElement
	 * @param eventName       The clientBehaviour event name, be sure that the sourceComponent supports this event, or nothing will happen.
	 * @param process         The scope to process the request for. default is sourceComponent.
	 */
	public static void updateDom(String sourceComponent, String update, String eventName, String process) {

		if (process == null) {
			process = sourceComponent;
		}

		RequestContext.getCurrentInstance().execute("PrimeFaces.ab({s:'" + sourceComponent + "',e:'" + eventName + "',p:'" + process + "',u:'" + update + "'})");
	}

	public static void updateDom(String sourceComponent, String update, String eventName) {
		updateDom(sourceComponent, update, eventName, null);
	}
}
