package no.evote.presentation;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import no.valg.eva.admin.frontend.security.TmpLoginDetector;

@Named
@RequestScoped
public class WelcomePageController {
	private TmpLoginDetector tmpLoginDetector = new TmpLoginDetector();

	public String getLoginUrl() {
		if (tmpLoginDetector.isTmpLoginEnabled()) {
			return "/tmpLogin";
		} else {
			return "/saml/idporten";
		}
	}

}
