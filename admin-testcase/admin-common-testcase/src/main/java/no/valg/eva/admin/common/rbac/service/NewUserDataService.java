package no.valg.eva.admin.common.rbac.service;

import java.net.InetAddress;

import javax.ejb.Remote;

import no.evote.model.OperatorRole;
import no.evote.security.UserData;

@Remote
public interface NewUserDataService {

	/**
	 * Validates signature of SAMLAssertion and returns a UserData if valid and user exists
	 */
	@SuppressWarnings("ParameterNameCheck")
	UserData getUserDataForSamlAssertion(String samlassertion, final InetAddress inetAddress);

	//TODO EVADMIN-344 - avhengig her av at UserData faktisk er signert, hvis ikke kan frontend bare lage ett nytt userdata-objekt og dermed
	// omgaa saml-assertion.
	UserData setAccessCacheOnUserData(UserData userData, OperatorRole operatorRole);

}
