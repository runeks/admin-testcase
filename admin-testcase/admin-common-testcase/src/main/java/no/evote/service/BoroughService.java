package no.evote.service;

import java.util.List;

import no.evote.dto.ConfigurationDto;
import no.evote.model.Borough;
import no.evote.model.Country;
import no.evote.security.UserData;

public interface BoroughService {
	Borough create(UserData userData, Borough borough);

	Borough update(UserData userData, Borough borough);

	void delete(UserData userData, Long pk);

	Borough findBoroughById(UserData userData, final Long municipalityPk, final String id);

	Borough findByPk(Long boroughPk);

	List<Borough> findWithoutPollingDistricts(UserData userData, List<Country> countries);

	List<Borough> findByMunicipality(UserData userData, Long municipalityPk);

	List<ConfigurationDto> getBoroughsWithoutPollingDistricts(UserData userData, Long electionEventPk);
}
