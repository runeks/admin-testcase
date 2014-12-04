package no.valg.eva.admin.frontend.security;

import no.evote.util.EvoteProperties;

/**
 * This class knows whether tmpLogin is enabled or not. Used for feature toggling.
 */
public class TmpLoginDetector {
	public boolean isTmpLoginEnabled() {
		return Boolean.valueOf(EvoteProperties.getProperty("no.valg.eva.admin.login.tmp.enabled", "true"));
	}
}
