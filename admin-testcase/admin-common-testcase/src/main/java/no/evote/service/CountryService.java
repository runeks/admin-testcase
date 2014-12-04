package no.evote.service;

import java.util.List;

import no.evote.model.Country;
import no.evote.security.UserData;

public interface CountryService {

	Country findCountryById(UserData userData, Long electionEventPk, String id);

	Country create(UserData userData, Country country);

	Country update(UserData userData, Country country);

	void delete(UserData userData, Long countryPk);

	Country findByPk(Long pk);

	List<Country> getCountriesForElectionEvent(UserData userData, Long electionEventPk);

	List<Country> getCountriesWithoutCounties(UserData userData, Long electionEventPk);
}
