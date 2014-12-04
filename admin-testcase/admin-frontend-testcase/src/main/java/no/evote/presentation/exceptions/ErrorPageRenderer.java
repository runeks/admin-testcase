package no.evote.presentation.exceptions;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.evote.util.IOUtil;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

public final class ErrorPageRenderer {

	private static final Logger LOGGER = Logger.getLogger(ErrorPageRenderer.class);

	public static final String ERROR_404_TITLE = "Siden finnes ikke / Page not found";
	public static final String ERROR_404_MESSAGE = "Siden du prøvde å gå til finnes ikke / The requested page was not found.";
	public static final String ERROR_500_TITLE = "En feil har oppstått / An error occurred";

	private ErrorPageRenderer() {
	}

	private static String render(final String title, final String message, final String stackTrace) throws IOException {
		InputStream is = ErrorPageRenderer.class.getClassLoader().getResourceAsStream("errorpage.html");

		// Add exception message and stack trace to the error page
		String errorPage = new String(IOUtil.getBytes(is), "UTF-8");
		if (title != null) {
			errorPage = errorPage.replace("%title", StringEscapeUtils.escapeHtml(title));
		}

		if (message != null) {
			errorPage = errorPage.replace("%message", StringEscapeUtils.escapeHtml(message));
		}

		if (stackTrace != null) {
			errorPage = errorPage.replace("%stacktrace", StringEscapeUtils.escapeHtml(stackTrace));
		}
		return errorPage;
	}

	public static void render500Error(final HttpServletRequest request, final HttpServletResponse response, final String message, final Throwable stacktrace)
			throws IOException {
		render500Error(request, response, message, getStackTraceAsString(stacktrace));
	}

	private static String getStackTraceAsString(final Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		return sw.toString();
	}

	public static void render404Error(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		// CSOFF: MagicNumber
		render(request, response, ERROR_404_TITLE, ERROR_404_MESSAGE, "", 404);
		// CSON: MagicNumber
	}

	public static void render500Error(final HttpServletRequest request, final HttpServletResponse response, final String message, final String stackTrace)
			throws IOException {
		LOGGER.error("Internal server error 500 - rendering error page with message and stacktrace:");
		LOGGER.error(message);
		LOGGER.error(stackTrace);
		// CSOFF: MagicNumber
		render(request, response, ERROR_500_TITLE, message, stackTrace, 500);
		// CSON: MagicNumber
	}

	private static void render(final HttpServletRequest request, final HttpServletResponse response, final String title, final String message,
			final String stackTrace, final int statusCode) throws IOException {
		response.resetBuffer();

		response.setContentType("text/html; charset=UTF-8");
		response.setStatus(statusCode);
		response.addHeader(AjaxErrorPageServlet.ERROR_HEADER, request.getContextPath().replace("/secure", "") + "/ajaxerror");
		HttpSession session = request.getSession(false);
		session.setAttribute(AjaxErrorPageServlet.MESSAGE, message);
		session.setAttribute(AjaxErrorPageServlet.STACKTRACE, stackTrace);

		Writer w = response.getWriter();
		w.write(ErrorPageRenderer.render(title, message, stackTrace));
		w.flush();
	}

}
