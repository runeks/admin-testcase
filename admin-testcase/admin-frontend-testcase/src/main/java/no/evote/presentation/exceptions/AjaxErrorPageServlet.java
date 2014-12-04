package no.evote.presentation.exceptions;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Error page renderer used for failed Ajax requests. Renders error page with message and stack trace from the HttpSession.
 */
@WebServlet(name = "AjaxErrorPage", urlPatterns = "/ajaxerror")
public class AjaxErrorPageServlet extends HttpServlet {
	public static final String ERROR_HEADER = "X-ERROR-PAGE";
	public static final String MESSAGE = "ERROR-MESSAGE";
	public static final String STACKTRACE = "ERROR-STACKTRACE";

	@Override
	public void doGet(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
		HttpSession session = req.getSession();
		ErrorPageRenderer.render500Error(req, res, (String) session.getAttribute(MESSAGE), (String) session.getAttribute(STACKTRACE));
	}
}
