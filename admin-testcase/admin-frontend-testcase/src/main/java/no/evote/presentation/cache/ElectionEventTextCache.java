package no.evote.presentation.cache;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import no.evote.model.LocaleText;
import no.evote.service.TranslationService;

import org.apache.log4j.Logger;

@ApplicationScoped
public class ElectionEventTextCache implements Serializable {

	private final Logger log = Logger.getLogger(ElectionEventTextCache.class);
	private Cache cache;

	private TranslationService translationService;

	@Inject
	private GenericCacheManager genericCacheManager;

	@PostConstruct
	public void init() {
		cache = genericCacheManager.getCache("textCache");
	}

	public String get(final long electionEventPk, final long localePk, final String textId) {
		String cacheKey = getCacheKey(textId, localePk, electionEventPk);
		Element cacheElem = cache.get(cacheKey);

		if (cacheElem != null) {
			log.trace("Cache hit: " + cacheKey);
			return (String) cacheElem.getValue();
		} else {
			LocaleText lt = translationService.getLocaleTextByElectionEvent(electionEventPk, localePk, textId);
			String value = null;
			if (lt == null || lt.getLocaleText() == null) {
				value = "???" + textId + "???";
			} else {
				value = lt.getLocaleText();
			}

			cache.put(new Element(cacheKey, value));
			log.trace("Cache miss: " + cacheKey);
			return value;
		}
	}

	private String getCacheKey(final String textId, final long localePk, final long electionEventPk) {
		return new StringBuffer(textId).append(",").append(localePk).append(",").append(electionEventPk).toString();
	}

	@PreDestroy
	public void shutdown() {
		cache.dispose();
	}

}
