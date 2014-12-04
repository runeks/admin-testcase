package no.evote.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.cert.Certificate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import no.evote.model.ElectionEvent;
import no.evote.model.KeyDomain;
import no.evote.model.SigningKey;
import no.evote.security.UserData;

public interface SigningKeyService {

	List<Map<String, Object>> findAllSigningKeys(UserData userData);

	SigningKey findSigningKeyByPk(UserData userData, Long pk);

	KeyDomain findKeyDomainById(UserData userData, String keyDomainId);

	SigningKey updateSigningKey(UserData userData, SigningKey signingKey) throws UnsupportedEncodingException;

	/**
	 * Get all certificates enclosed in the p7b bundle used for verification of scanning counts
	 * 
	 * @return
	 */
	Collection<Certificate> getScanningCountCertsFromBundle();

	SigningKey create(UserData userData, SigningKey signingKey, byte[] bytes, String fileName, String password, ElectionEvent electionEvent) throws IOException;

	boolean isSigningKeySetForElectionEvent(UserData userData, ElectionEvent electionEvent);

	SigningKey getAuditSigningSigningKey();

	SigningKey getSigningKeyForElectionEventSigning(UserData userData, Long electionEventPk);

	SigningKey getSigningKeyForEvoteMarkoffVerification(UserData userData, Long electionEventPk);

	SigningKey getSigningKeyForEvoteCountVerification(UserData userData, Long electionEventPk);

	SigningKey getDIFISigningSigningKey();

}
