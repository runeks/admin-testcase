package no.evote.service.security;

import static java.lang.Boolean.TRUE;

import java.io.IOException;
import java.util.List;

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
import javax.servlet.http.HttpSession;

import no.evote.model.Operator;
import no.evote.security.UserData;
import no.evote.service.OperatorService;
import no.valg.eva.admin.common.Person;
import no.valg.eva.admin.common.PersonId;
import no.valg.eva.admin.frontend.context.UserContext;

import org.apache.log4j.Logger;

public class SelectRoleFilter implements Filter {

	private static final Logger LOGGER = Logger.getLogger(SelectRoleFilter.class);
	public static final String SCANNING_LOGIN_VIA_TMP_LOGIN_SESSION_KEY = "skanningTmpLogin";

	@Inject
	private Instance<UserData> userDataInstance;
	private OperatorService operatorService;
	@Inject
	private UserContext userContext;

	@Inject
	private UserData userDataAttr;

	@Override
	/**
	 * If a role is not selected, the user should be redirected to selectRole.xhtml.
	 * One exception is for Scanning login, which has its own role selection process.
	 */
	public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		HttpSession session = request.getSession();
		String servletPath = request.getServletPath();
		UserData userData = userDataInstance.get();

		if (TRUE.equals(isScanningLoginViaTmpLogin(session))) {
			LOGGER.debug("Skanning login via tmp login satt på session - redirect til scanningLoginSelectElectionEvent");
			// Operator has just logged in, but since tmpLogin always forwards to 'Min side',
			// we're now redirecting to Scanning login's start page..
			// This is not used with SAML login, where SAML Relay State is used instead.
			session.removeAttribute(SCANNING_LOGIN_VIA_TMP_LOGIN_SESSION_KEY);
			response.sendRedirect(request.getServletContext().getContextPath() + "/secure/" + ScanningLoginUtil.startPage());
			return;
		}

		if (userHasAlreadySelectedAnOperatorRole(userData)) {
			LOGGER.debug("bruker har valgt en rolle, fant bruker id " + userData.getUid());
			chain.doFilter(req, res);
		} else {
			LOGGER.debug("bruker har ikke valgt rolle, fant bruker id " + userData.getUid());
			setUserContextIfNull(userData);

			if ("/secure/selectRole.xhtml".equals(servletPath)) {
				LOGGER.debug("går til selectRole.xhtml");
				chain.doFilter(req, res);
			} else if (ScanningLoginUtil.isScanningLogin(request)) {
				// Scanning login has its own 'role selection process'.
				LOGGER.debug("går videre med skanning innlogging");
				chain.doFilter(req, res);
			} else {
				LOGGER.debug("legger " + request.getServletPath() + " på session og går til selectRole");
				request.getSession().setAttribute("goto", request.getServletPath());
				response.sendRedirect("/secure/selectRole.xhtml");
			}
		}
	}

	private Boolean isScanningLoginViaTmpLogin(HttpSession session) {
		return (Boolean) session.getAttribute(SCANNING_LOGIN_VIA_TMP_LOGIN_SESSION_KEY);
	}

	private boolean userHasAlreadySelectedAnOperatorRole(UserData userData) {
		return userData != null;	}

	private void setUserContextIfNull(UserData userData) {
		if (userContext.getNameLine() == null) {
			// initialize frontend context with first operator found
			List<Operator> operators = operatorService.findOperatorsById(userData.getUid());
			Operator operator = operators.get(0);
			userContext.setNameLine(new Person(new PersonId(operator.getId()), null, operator.getFirstName(), operator.getMiddleName(), operator
					.getLastName(), null).nameLine());
		}
	}

	@Override
	public void init(final FilterConfig arg0) throws ServletException {
		// To conform with interface
	}

	@Override
	public void destroy() {
		// To conform with interface
	}

}
