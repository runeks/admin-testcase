package no.evote.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import no.evote.constants.CountingHierarchy;
import no.evote.constants.VotingHierarchy;
import no.evote.model.ElectionDay;
import no.evote.model.ElectionEvent;
import no.evote.model.ElectionEventLocale;
import no.evote.model.ElectionEventStatus;
import no.evote.model.Locale;
import no.evote.security.UserData;
import no.evote.service.cache.CacheInvalidate;
import no.evote.service.cache.Cacheable;

public interface ElectionEventService {

	List<ElectionEvent> findAllActiveElectionEvents();

	ElectionEvent create(final UserData userData, final ElectionEvent electionEventTo, final boolean copyRoles, VotingHierarchy votingHierarchy,
			CountingHierarchy countingHierarchy, final boolean copyPostalCodes, final Long electionEventFromPk, final Set<Locale> localeSet);

	@CacheInvalidate(entityClass = ElectionEvent.class, entityParam = 1)
	ElectionEvent update(UserData userData, ElectionEvent electionEvent, Set<Locale> localeSet);

	ElectionEvent findByPk(Long pk);

	ElectionEvent findById(String id);

	List<ElectionEvent> findAll();

	Date findLatestElectionDay(UserData userData, ElectionEvent electionEvent);

	@CacheInvalidate(entityClass = ElectionEvent.class, entityParam = 1)
	void openForLocalConfiguration(UserData userData, Long pk);

	@CacheInvalidate(entityClass = ElectionEvent.class, entityParam = 1)
	void approveConfiguration(UserData userData, Long pk);

	@CacheInvalidate(entityClass = ElectionEvent.class, entityParam = 1)
	void unApproveConfiguration(UserData userData, Long pk);

	boolean hasGroups(UserData userData, Long electionEventPk);

	void copyRoles(UserData userData, final Long electionEventPkFrom, final Long electionEventPkTo);

	void copyAreas(UserData userData, final Long electionEventPkFrom, final Long electionEventPkTo);

	void copyElections(UserData userData, final Long electionEventPkFrom, final Long electionEventPkTo);

	void copyPostalCodes(UserData userData, final Long electionEventPkFrom, final Long electionEventPkTo);

	@Cacheable
	ElectionEventStatus findElectionEventStatusById(int id);

	@Cacheable
	List<ElectionEventStatus> findAllElectionEventStatuses(UserData userData);

	ElectionDay createElectionDay(UserData userData, ElectionDay electionDay);

	ElectionDay updateElectionDay(UserData userData, ElectionDay electionDay);

	void deleteElectionDay(UserData userData, Long electionDayPk);

	void deleteElectionEvent(UserData userData, Long electionEventPk);

	List<ElectionDay> findElectionDaysByElectionEvent(UserData userData, Long electionEventPk);

	ElectionDay findElectionDayByPk(UserData userData, Long electionDayPk);

	List<ElectionEventLocale> getElectionEventLocalesForEvent(UserData userData, ElectionEvent electionEvent);

	void deleteElectionEventLocaleForElectionEvent(UserData userData, ElectionEvent electionEvent, Locale locale);

	List<Locale> getLocalesForEvent(UserData userData, ElectionEvent electionEvent);

	/**
	 * Creates new election event asynchronously
	 */
	void createAsync(UserData userData, ElectionEvent electionEventTo, boolean copyRoles, VotingHierarchy votingHierarchy,
			CountingHierarchy countingHierarchy, boolean copyPostalCodes, Long electionEventFromPk, Set<Locale> localeSet);
}
