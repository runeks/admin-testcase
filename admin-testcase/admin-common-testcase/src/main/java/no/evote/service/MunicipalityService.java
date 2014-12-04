package no.evote.service;

import java.io.Serializable;
import java.util.List;

import no.evote.dto.ConfigurationDto;
import no.evote.model.Country;
import no.evote.model.County;
import no.evote.model.Locale;
import no.evote.model.Municipality;
import no.evote.model.MunicipalityStatus;
import no.evote.security.UserData;

public interface MunicipalityService extends Serializable {
	Municipality findMunicipalityById(UserData userData, Long countyPk, String municipalityId);

	List<Municipality> findMunicipalitiesByCountryPk(Long countryPk);

	Municipality create(UserData userData, Municipality municipality);

	Municipality update(UserData userData, Municipality municipality);

	void delete(UserData userData, Long municipalityPk);

	Municipality findByPk(Long municipalityPk);

	Locale getLocale(Municipality currentMunicipality);

	MunicipalityStatus getStatus(UserData userData, Long municipalityPk);

	List<Municipality> findWithoutBoroughsByCountries(UserData userData, List<Country> countries);

	boolean hasMunicipalities(UserData userData, County county);

	List<String> getVotersWithoutPollingDistricts(UserData userData, Long municipalityPk, Long electionEventPk);

	void sendToApproval(UserData userData, Long municipalityPk);

	void reject(UserData userData, Long municipalityPk);

	void approve(UserData userData, Long municipalityPk);

	void close(UserData userData, Long pk);

	List<Municipality> getMunicipalitiesWithStatus(UserData userData, Long electionEventPk, Integer municipalityStatusId);

	List<ConfigurationDto> getElectionGroupsMissingReportCountCategories(UserData userData, Long electionEventPk, Long municipalityPk);

	boolean isReadyForEvotes(UserData userData, Municipality municipality);

	Municipality findByPollingDistrict(Long pollingDistrictPk);

	boolean getHasAnyMunicipalitiesWithNonElectronicMarkoffs(UserData userData);

	List<Municipality> getMunicipalitiesWithPollingPlacesWithoutPollingStations(UserData userData);

	List<Municipality> findByCounty(UserData userData, Long countyPk);

	void setRequiredProtocolCountForElectionEvent(UserData userData, Long electionEventPk, boolean requiredProtocolCount);

	boolean getRequiredProtocolCountForElectionEvent(UserData userData, Long electionEventPk);

	Municipality findUniqueMunicipalityByElectionEvent(UserData userData, Long pk, String countryId, String countyId, String municipalityId);

	List<Municipality> getMunicipalitiesByStatus(UserData userData, Long electionEventPk, Integer status);

	List<Municipality> getMunicipalitiesWithoutEncompassingBoroughs(Long electionEventPk);

	List<Municipality> getMunicipalitiesWithoutEncompassingPollingDistricts(Long electionEventPk);

	List<Municipality> getMunicipalitiesWithoutBoroughs(UserData userData, Long electionEventPk);
}
