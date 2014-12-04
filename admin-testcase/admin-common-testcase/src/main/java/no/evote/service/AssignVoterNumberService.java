package no.evote.service;

import no.evote.model.ElectionEvent;
import no.evote.security.UserData;
import no.evote.service.cache.CacheInvalidate;

public interface AssignVoterNumberService {

	/**
	 * Runs assignVoterNumbersSynchronous asynchronously
	 *
	 */
	@CacheInvalidate(entityClass = ElectionEvent.class, entityParam = 1)
	void assignVoterNumbers(UserData userData, Long electionEventPk);

	/**
	 * First calls the postgreSQL function assign_voter_numbers to generate voter_numbers for the election event. When the function has finished, page number
	 * and line numbers, for voters in districts using polling stations, are regenerated. For every new polling station the page number will be increased and
	 * the line number start again from 1.
	 *
	 */
	void assignVoterNumbersSynchronous(UserData userData, Long electionEventPk);

	/**
	 * Checks if the voter numbers are generated or are being generated for election event
	 *
	 * @param electionEvent
	 * @return
	 */
	Boolean isVoterNumbersGenerated(UserData userData, ElectionEvent electionEvent);

}
