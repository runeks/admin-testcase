package no.valg.eva.admin.login.tmp;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.evote.constants.EvoteConstants;
import no.evote.service.security.SelectRoleFilter;
import no.valg.eva.admin.frontend.security.TmpLoginDetector;

@WebServlet("/tmpLogin")
public class TmpLoginServlet extends HttpServlet {
	private TmpLoginDetector tmpLoginDetector = new TmpLoginDetector();

	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
		if (tmpLoginDetector.isTmpLoginEnabled()) {
			invalidateSessionIfExists(req);

			Writer w = resp.getWriter();
			resp.setContentType("text/html");

			w.append("<html>" + "<head><link type=\"text/css\" href=\"/javax.faces.resource/all.css.xhtml?ln=css&rv=4.0%20(90e94bd)\" "
					+ "rel=\"stylesheet\"></head>" + "<body id=\"page-tmplogin\"> <form method=\"POST\">"
					+ "Bruker ID: <input type=\"text\" name=\"username\" /> " + "<br/>"
					+ "Security level: <input type=\"text\" name=\"secLevel\" value=\"3\"/> " + "<br/>" + "<input type=\"submit\" value=\"Login\"/>"
					+ "</form>" + "</body>" + "</html>");
		} else {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	@Override
	protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {

		req.getSession().setAttribute(EvoteConstants.MINID_UID_ATTRIBUTE_NAME, req.getParameter("username"));
		req.getSession().setAttribute(EvoteConstants.MINID_SECURITY_LEVEL_ATTRIBUTE_NAME, req.getParameter("secLevel"));

		resp.sendRedirect("/secure/selectRole.xhtml");

	}

	private void invalidateSessionIfExists(HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		if (session != null && notScanningLoginViaTmpLoginInProgress(session)) {
			session.invalidate();
		}
	}

	private boolean notScanningLoginViaTmpLoginInProgress(HttpSession session) {
		return session.getAttribute(SelectRoleFilter.SCANNING_LOGIN_VIA_TMP_LOGIN_SESSION_KEY) == null;
	}
}
