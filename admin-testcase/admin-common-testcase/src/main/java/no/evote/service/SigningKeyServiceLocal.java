package no.evote.service;

import no.evote.util.EvoteSamlCredentials;

public interface SigningKeyServiceLocal {
	EvoteSamlCredentials getDIFICredential();
}
