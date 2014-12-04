package no.evote.presentation;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.valg.eva.admin.frontend.security.TmpLoginDetector;

@WebServlet(value = "/logout")
public class LogoutServlet extends HttpServlet {
	private TmpLoginDetector tmpLoginDetector = new TmpLoginDetector();

	@Override
	public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}

		if (tmpLoginDetector.isTmpLoginEnabled()) {
			response.sendRedirect("/welcome.xhtml");
		} else {
			response.sendRedirect("/saml/Logout");
		}
	}
}
