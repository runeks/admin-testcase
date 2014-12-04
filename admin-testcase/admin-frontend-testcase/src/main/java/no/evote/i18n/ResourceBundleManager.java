package no.evote.i18n;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import no.evote.service.TranslationService;

/**
 * @see no.evote.i18n.BaseResourceBundleManager
 */
@ApplicationScoped
public class ResourceBundleManager extends BaseResourceBundleManager {
	private TranslationService translationService;

	@Override
	BaseResourceBundleControlDatabase createResourceBundleControlDatabase() {
		return new ResourceBundleControlDatabase(translationService);
	}

	@Override
	TranslationService getTranslationService() {
		return translationService;
	}
}
