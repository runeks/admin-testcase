package no.evote.util;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import org.opensaml.xml.security.SecurityHelper;
import org.opensaml.xml.security.credential.Credential;

public class EvoteSamlCredentials {

	private Credential credential;

	public EvoteSamlCredentials(final X509Certificate x509Certificate, final PrivateKey privateKey) {
		this.credential = SecurityHelper.getSimpleCredential(x509Certificate, privateKey);
	}

	public EvoteSamlCredentials(final Credential credential) {
		this.credential = credential;
	}

	public Credential toX509Credential() {
		return credential;
	}
}
