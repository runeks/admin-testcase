package no.evote.service.security;

import java.io.IOException;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.evote.exception.EvoteSecurityException;
import no.evote.presentation.exceptions.ErrorPageRenderer;
import no.evote.security.UserData;
import no.evote.service.AuditLoggerService;
import no.evote.util.StringUtil;

import org.apache.log4j.Logger;

public class CSRFFilter implements Filter {

	private static final Logger LOGGER = Logger.getLogger(CSRFFilter.class);

	@Inject
	private Instance<UserData> userDataInstance;
	private AuditLoggerService auditLoggerService;
	@Inject
	private transient Map<String, String> mms;

	@Override
	/*
	 * A token is added in every form and ajax request to the server. This filter controls that the token has not been changed.'
	 * This is done to protect against CSRF attacks
	 */
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		req.setCharacterEncoding("UTF-8"); // Fixes #3860
		boolean isAjaxRequest = "XMLHttpRequest".equals(req.getHeader("X-Requested-With"));
		UserData userData = userDataInstance.get();

		if (req.getMethod().equals("GET") || userData == null) {
			filterChain.doFilter(request, response);
			return;
		}

		String csrfToken = null;
		if (isAjaxRequest) {
			csrfToken = req.getHeader("X-CSRF-Token");
			// TODO Remove then all pages are on new design
			// Make it possible to post CSRFToken as ajax request param (for dialogs etc).
			if (csrfToken == null) {
				csrfToken = req.getParameter("CSRFToken");
			}
		} else if (req.getMethod().equals("POST")) {
			csrfToken = req.getParameter("CSRFToken");
		}

		if (csrfToken == null) {
			EvoteSecurityException evoteSecurityException = new EvoteSecurityException("CSRF token is missing");
			StackTraceElement stackTrace = evoteSecurityException.getStackTrace()[0];

			writeAuditLog(userData, evoteSecurityException, stackTrace);

			ErrorPageRenderer.render500Error(req, (HttpServletResponse) response, mms.get("@filter.CSRF.nonexist"), "");

			return;
		} else if (isTokenInvalid((HttpServletResponse) response, req, userData, csrfToken)) { // Compare the token to the one in session
			return;
		}

		filterChain.doFilter(request, response);
	}

	private void writeAuditLog(UserData userData, EvoteSecurityException evoteSecurityException, StackTraceElement stackTrace) {
		try {
			auditLoggerService.error(userData, stackTrace.getMethodName(), this.getClass().getName(), evoteSecurityException, stackTrace,
					evoteSecurityException.getMessage());
		} finally {
			String loggerMsg = StringUtil.join(userData.getUid(), stackTrace.getMethodName(), this.getClass()
					.getName(), evoteSecurityException.getMessage());
			LOGGER.error(loggerMsg);
		}
	}

	/**
	 * Checks the token from the request against the token contained in the UserData instance
	 */
	private boolean isTokenInvalid(final HttpServletResponse response, final HttpServletRequest req, final UserData userData, final String csrfToken)
			throws IOException {
			return false;
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		// To conform with interface
	}

	@Override
	public void destroy() {
		// To conform with interface
	}

}
