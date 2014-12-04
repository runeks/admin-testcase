package no.evote.service;

import java.io.Serializable;
import java.util.List;

import no.evote.model.Election;
import no.evote.model.ElectionType;
import no.evote.security.UserData;
import no.evote.service.cache.Cacheable;

public interface ElectionService extends Serializable {

	Election findElectionById(UserData userData, Long electionGroupPk, String id);

	Election create(UserData userData, Election election, boolean isAutoGenerateContests);

	Election update(UserData userData, Election election);

	void delete(UserData userData, Long pk);

	Election findByPk(UserData userData, Long pk);

	List<Election> getElections(final UserData userData, Long electionGroupPk);

	Election findElectionInEvent(final UserData userData, final String electionId, final String electionEventId);

	@Cacheable
	ElectionType findElectionTypeById(String id);

	@Cacheable
	List<ElectionType> findAllElectionTypes(UserData userData);

	List<Election> getElectionsWithoutContests(UserData userData, Long electionEventPk);
}
