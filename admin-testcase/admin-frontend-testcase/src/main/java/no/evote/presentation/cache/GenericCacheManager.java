package no.evote.presentation.cache;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import no.evote.util.EvoteProperties;

import org.apache.log4j.Logger;

/**
 * Utility class for getting local or distributed EHCache instances (local is used if there is only one node)
 */
@ApplicationScoped
public class GenericCacheManager implements Serializable {

	private static final String CACHE_TYPE_PROPERTY = "no.evote.presentation.cache.EntityCache.type";
	private static final String LOCAL_CACHE = "local";
	private static final Logger LOG = Logger.getLogger(GenericCacheManager.class);
	private transient CacheManager manager;

	@PostConstruct
	public void init() {
		LOG.debug("init()");
		if (LOCAL_CACHE.equals(EvoteProperties.getProperty(CACHE_TYPE_PROPERTY, true))) {
			LOG.debug("Using local cache");
			CacheManager.create(getClass().getClassLoader().getResourceAsStream("ehcache-local.xml"));
			manager = CacheManager.getInstance();
		} else {
			LOG.debug("Using clustered cache");
			CacheManager.create(getClass().getClassLoader().getResourceAsStream("ehcache-cluster.xml"));
			manager = CacheManager.getInstance();
		}
	}

	@PreDestroy
	public void shutdown() {
		manager.shutdown();
	}

	public Cache getCache(final String name) {
		return manager.getCache(name);
	}

}
