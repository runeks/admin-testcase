package no.evote.i18n;

import java.io.Serializable;

import no.evote.service.TranslationService;

/**
 * Wrapper class to make sure we have the ResourceBundleControlDatabase implementation available for the classloader.
 */
public class ResourceBundleControlDatabase extends BaseResourceBundleControlDatabase implements Serializable {
	public ResourceBundleControlDatabase(final TranslationService translationService) {
		super(translationService);
	}
}
