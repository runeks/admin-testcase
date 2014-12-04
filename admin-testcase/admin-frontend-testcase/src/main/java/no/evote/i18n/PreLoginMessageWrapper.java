package no.evote.i18n;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import no.evote.constants.EvoteConstants;
import no.evote.model.Locale;
import no.evote.presentation.cache.GlobalTextCache;
import no.evote.service.TranslationService;
import no.evote.util.EvoteProperties;

@Named("pmsgs")
@ApplicationScoped
public class PreLoginMessageWrapper implements Map<String, String>, Serializable {
	private GlobalTextCache textCache;

	private TranslationService translationService;
	private Locale defaultLocale = null;

	private Locale getDefaultLocale() {
		defaultLocale = new Locale();
		defaultLocale.setId("nb-NO");
		defaultLocale.setName("Bokmål");
		return defaultLocale;
	}

	@Override
	public String get(final Object key) {
		if (((String)key).endsWith("emergency_envelopes")) {
			return "Trigger 2 minute wait for session timeout";
		}
		if (((String)key).endsWith("foreign_votes_borough")) {
			return "Trigger out of memory error";
		}
		
		return "any text";
	}

	@Override
	public int size() {
		throw new IllegalAccessError();
	}

	@Override
	public boolean isEmpty() {
		throw new IllegalAccessError();
	}

	@Override
	public boolean containsKey(final Object key) {
		throw new IllegalAccessError();
	}

	@Override
	public boolean containsValue(final Object value) {
		throw new IllegalAccessError();
	}

	@Override
	public String put(final String key, final String value) {
		throw new IllegalAccessError();
	}

	@Override
	public String remove(final Object key) {
		throw new IllegalAccessError();
	}

	@Override
	public void putAll(final Map<? extends String, ? extends String> m) {
		throw new IllegalAccessError();
	}

	@Override
	public void clear() {
		throw new IllegalAccessError();
	}

	@Override
	public Set<String> keySet() {
		throw new IllegalAccessError();
	}

	@Override
	public Collection<String> values() {
		throw new IllegalAccessError();
	}

	@Override
	public Set<Entry<String, String>> entrySet() {
		throw new IllegalAccessError();
	}

}
