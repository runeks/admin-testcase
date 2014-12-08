package no.valg.eva.admin.frontend.security;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

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

import no.evote.constants.EvoteConstants;
import no.evote.exception.EvoteSecurityException;
import no.evote.presentation.UserSessionController;
import no.evote.security.UserData;
import no.evote.security.UserDataProducer;
import no.evote.service.AuditLoggerService;
import no.evote.service.OperatorService;
import no.evote.service.TranslationService;
import no.evote.service.security.ScanningLoginUtil;
import no.evote.service.security.SelectRoleFilter;
import no.valg.eva.admin.common.rbac.service.NewUserDataService;

import org.apache.log4j.Logger;

/**
 * SAML environment variables are expected to have been set by AJP.
 */
public class SamlAssertionFilter implements Filter {

	static final String SAML_ASSERTION_HEADER_KEY = "Shib-Assertion-01";
	static final String AJP_PREFIX = "AJP_";
	private static final String SAML_ASSERTION_HEADER_KEY_WITH_AJP_PREFIX = AJP_PREFIX + SAML_ASSERTION_HEADER_KEY;

	private static final Logger LOGGER = Logger.getLogger(SamlAssertionFilter.class);

	private NewUserDataService userDataService;
	@Inject
	private Instance<UserDataProducer> userDataProducerInstance;
	private TranslationService translationService;
	private OperatorService operatorService;
	private AuditLoggerService auditLoggerService;

	@Inject
	private UserDataProducer userDataProducer;
	@Inject
	private UserData userDataAttr;

	private Object httpGetter;
	private TmpLoginDetector tmpLoginDetector = new TmpLoginDetector();


	@SuppressWarnings("unused")
	public SamlAssertionFilter() {
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		httpGetter = new Object();
		LOGGER.debug("Created HttpConnectionManager and HttpClient for SAML communication");
	}

	@Override
	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
		HttpSession session = ((HttpServletRequest) servletRequest).getSession();
		UserDataProducer userDataProducer = userDataProducerInstance.get();

		UserData userData = userDataProducer.getUserData();
		if (userData != null) {
			LOGGER.debug("Fant user data med bruker id " + userData.getUid());
			filterChain.doFilter(httpServletRequest, httpServletResponse);
		} else if (userSwitchedRoles(session)) {
			UserData userDataFromPreviousSession = popUserDataFromRoleSwitch(session);
			userDataProducer.setUserData(userDataFromPreviousSession);
			LOGGER.debug("Bruker endret rolle, hentet og oppdatert user data med bruker id " + userDataFromPreviousSession.getUid());
			filterChain.doFilter(httpServletRequest, httpServletResponse);
		} else {
			if (tmpLoginDetector.isTmpLoginEnabled()) {
				handleRequestFromTmpLogin(httpServletRequest, httpServletResponse, filterChain, userDataProducer);
			} else {
				handleRequestFromSamlServiceProvider(httpServletRequest, httpServletResponse, filterChain, userDataProducer);
			}
		}
	}

	private boolean userSwitchedRoles(HttpSession session) {
		return session.getAttribute(UserSessionController.OPERATOR_SWITCHED_ROLES_SESSION_ATTR_KEY) != null;
	}

	private UserData popUserDataFromRoleSwitch(HttpSession session) {
		UserData userData = (UserData) session.getAttribute(UserSessionController.OPERATOR_SWITCHED_ROLES_SESSION_ATTR_KEY);
		session.removeAttribute(UserSessionController.OPERATOR_SWITCHED_ROLES_SESSION_ATTR_KEY);

		return userData;
	}

	private void handleRequestFromTmpLogin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain,
			UserDataProducer userDataProducer)
			throws IOException, ServletException {
		HttpSession session = httpServletRequest.getSession();

			UserData userData = popUserDataFromTmpLogin(session, getClientAddressFromRequest(httpServletRequest));

			userDataProducer.setUserData(userData);

			LOGGER.debug("Bruker logget inn med tmpLogin, lagd og oppdatert user data med bruker id " + userData.getUid());

			filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

	private void handleRequestFromSamlServiceProvider(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain,
			UserDataProducer userDataProducer)
			throws IOException, ServletException {
		HttpSession session = httpServletRequest.getSession();
		LOGGER.debug("Request fra SamlServiceProvider - skjer ikke i test");
		try {
			String samlAssertion = getSamlAssertionFromRequest(httpServletRequest);

			UserData userData = userDataService.getUserDataForSamlAssertion(samlAssertion,
					getClientAddressFromRequest(httpServletRequest));
			userDataProducer.setUserData(userData);

			filterChain.doFilter(httpServletRequest, httpServletResponse);
		} catch (EvoteSecurityException e) {
			LOGGER.error(e.getMessage(), e);
			session.invalidate();
			httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/welcome.xhtml?type=error");
		}
	}

	private String getSamlAssertionFromRequest(HttpServletRequest httpServletRequest) throws IOException {
		String assertionUrl = getAssertionUrl(httpServletRequest);
		byte[] samlAssertionBytes = null;

		String samlAssertion = new String(samlAssertionBytes, "UTF-8");
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Response body: " + samlAssertion);
		}
		return samlAssertion;
	}

	private String getAssertionUrl(HttpServletRequest httpServletRequest) {
		String assertionUrl = httpServletRequest.getHeader(SAML_ASSERTION_HEADER_KEY_WITH_AJP_PREFIX);
		LOGGER.debug("Header " + SAML_ASSERTION_HEADER_KEY_WITH_AJP_PREFIX + ": " + assertionUrl);

		if (assertionUrl == null) {
			throw new EvoteSecurityException("Unable to retrieve SAML attributes from request");
		}

		return assertionUrl;
	}

	private boolean userHasJustLoggedInWithTmpLogin(HttpSession session) {
		return (session.getAttribute(EvoteConstants.MINID_UID_ATTRIBUTE_NAME) != null)
				&& (session.getAttribute(EvoteConstants.MINID_SECURITY_LEVEL_ATTRIBUTE_NAME) != null);
	}

	private UserData popUserDataFromTmpLogin(HttpSession session, InetAddress clientAddress) {
		UserData userData = new UserData(
				(String) session.getAttribute(EvoteConstants.MINID_UID_ATTRIBUTE_NAME));

		session.setAttribute(EvoteConstants.MINID_UID_ATTRIBUTE_NAME, null);
		session.setAttribute(EvoteConstants.MINID_SECURITY_LEVEL_ATTRIBUTE_NAME, null);

		return userData;
	}

	private InetAddress getClientAddressFromRequest(HttpServletRequest httpServletRequest) throws UnknownHostException {
		return InetAddress.getByName(httpServletRequest.getRemoteAddr());
	}

	private void handleUnknownOperator(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, HttpSession session, UserData userData)
			throws IOException {
		try {
			auditLoggerService.error(userData, "doFilter", getClass().getName(), null,
					new Exception().getStackTrace()[0], "User passed ID-porten login but did not have an operator in the system");
		} finally {
			LOGGER.error(userData.getUid() + " doFilter "
					+ getClass().getName() + " " + "User passed ID-porten login but did not have an operator in the system");
		}
		session.invalidate();
		httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/welcome.xhtml?type=error");
	}

	private void rememberThatThisIsAScanningLoginViaTmpLogin(HttpSession session) {
		session.setAttribute(SelectRoleFilter.SCANNING_LOGIN_VIA_TMP_LOGIN_SESSION_KEY, true);
	}

	@Override
	public void destroy() {
		LOGGER.debug("Shut down HttpConnectionManager");
	}

}
