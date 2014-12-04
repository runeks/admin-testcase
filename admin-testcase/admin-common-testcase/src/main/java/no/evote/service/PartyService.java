package no.evote.service;

import java.util.List;

import no.evote.model.Party;
import no.evote.model.PartyCategory;
import no.evote.security.UserData;
import no.evote.service.cache.Cacheable;

public interface PartyService {

	List<Party> getPartyWithoutAffiliationList(UserData newParam, Long contestPk);

	List<String> validateParty(UserData userData, Party newParty);

	Party create(UserData userData, Party party);

	Party update(UserData newParam, Party party);

	List<String> validatePartyForDelete(UserData userData, Party party);

	void delete(UserData userData, final Party party);

	Party findByPk(UserData userData, Long partyPk);

	List<Party> findAll(UserData userData);

	@Cacheable
	PartyCategory findPartyCategoryByPk(Long pk);

	@Cacheable
	PartyCategory findPartyCategoryById(String id);

	@Cacheable
	List<PartyCategory> findAllPartyCategories();

	List<Party> findAllPartiesButNotBlank(UserData userData, Long electionEventPk);

	List<Party> findAllPartiesInEvent(UserData userData, Long electionEventPk);

	Party findPartyByIdAndEvent(UserData userData, String partyId, Long electionEventPk);

	Party findPartyByShortCodeAndEvent(UserData userData, Integer shortCode, Long electionEventPk);

}
