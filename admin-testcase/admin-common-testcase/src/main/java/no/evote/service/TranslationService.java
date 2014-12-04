package no.evote.service;

import java.util.List;
import java.util.Map;

import no.evote.model.ElectionEvent;
import no.evote.model.Locale;
import no.evote.model.LocaleText;
import no.evote.model.TextId;
import no.evote.security.UserData;

public interface TranslationService {

	void createLocaleTexts(final UserData userData, final ElectionEvent electionEvent, final List<TextId> textIds, final List<LocaleText> localeTexts);

	void createGlobalLocaleTexts(final UserData userData, final List<TextId> textIds, final List<LocaleText> localeTexts);

	void updateLocaleTexts(final UserData userData, final ElectionEvent electionEvent, final Long localePk, final Map<String, String> localeTexts,
			final boolean addNewTextIds);

	void updateGlobalLocaleTexts(final UserData userData, final Long localePk, final Map<String, String> localeTexts, final boolean addNewTextIds);

	void updateTextIdDescriptions(final UserData userData, final ElectionEvent electionEvent, final Map<String, String> textIdDescriptions);

	void updateGlobalTextIdDescriptions(final UserData userData, final Map<String, String> textIdDescriptions);

	List<Locale> getAllLocales();

	List<Locale> getAdminLocales();

	List<TextId> getTextIds(final Long electionEventPk);

	TextId findTextIdByElectionEvent(final ElectionEvent electionEvent, final String textIdStr);

	List<TextId> findTextIdsByElectionEvent(final ElectionEvent electionEvent);

	Map<String, String> getLocaleTexts(final ElectionEvent electionEvent, final Long localePk);

	Locale findLocaleById(String id);

	LocaleText getLocaleTextByElectionEvent(final Long electionEventPk, final Long localePk, final String textId);

	void deleteLocaleText(UserData userData, LocaleText localeText);

	void deleteTextId(UserData userData, TextId textId);

	Locale findLocaleByPk(Long pk);

	TextId findTextIdByPk(UserData userData, Long pk);

	LocaleText getGlobalLocaleText(Long localePk, String textId);

	Map<String, String> getTextsByElectionEvent(UserData userData, Long electionEventPk, Long pk, List<String> keys);

	void refreshResourceBundleBackend();
}
