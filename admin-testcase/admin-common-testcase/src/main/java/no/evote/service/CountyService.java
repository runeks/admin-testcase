package no.evote.service;

import java.util.List;

import no.evote.model.Country;
import no.evote.model.County;
import no.evote.security.UserData;

public interface CountyService {

	County create(UserData userData, County county);

	County update(UserData userData, County county);

	void delete(UserData userData, Long pk);

	County findCountyById(UserData userData, Long countryPk, String id);

	County findByPk(Long pk);

	County findByMunicipality(Long pk);

	List<County> getCountiesByCountry(UserData userData, Long pk);

	List<County> findWithoutMunicipalitiesByCountries(UserData userData, List<Country> countries);

	boolean hasCounties(UserData userData, Country country);

	List<County> getCountiesWithoutMunicipalities(UserData userData, Long electionEventPk);
}
