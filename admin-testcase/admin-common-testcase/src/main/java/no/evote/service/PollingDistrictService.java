package no.evote.service;

import java.util.List;

import no.evote.dto.ConfigurationDto;
import no.evote.model.Borough;
import no.evote.model.PollingDistrict;
import no.evote.security.UserData;

public interface PollingDistrictService {

	List<PollingDistrict> findPollingDistrictsForParent(UserData userData, PollingDistrict pollingDistrictParent);

	List<PollingDistrict> findOrdinaryPollingDistrictsForBorough(UserData userData, Borough borough);

	List<PollingDistrict> findPollingDistrictCandidatesForBorough(UserData userData, Borough borough, PollingDistrict pollingDistrictParent);

	Boolean municipalityProxyExists(UserData userData, Long municipalityPk);

	void deleteParentPollingDistrict(UserData userData, PollingDistrict parentPollingDistrict);

	PollingDistrict findPollingDistrictById(UserData userData, Long boroughPk, String id);

	PollingDistrict create(UserData userData, PollingDistrict pollingDistrict);

	PollingDistrict update(UserData userData, PollingDistrict pollingDistrict);

	void delete(UserData userData, Long pk);

	PollingDistrict findByPk(UserData userData, Long pk);

	int addChildDistrict(UserData userData, PollingDistrict pollingDistrict, List<Long> addPkList);

	int remChildDistrict(UserData userData, PollingDistrict currentPollingDistrict, List<Long> removePkList);

	boolean hasPollingDistricts(UserData userData, Borough borough);

	/**
	 * Returns a list of configurationDTOs representing the polling districts inside the municipality that have not defined any polling place. The list excludes
	 * parent, technical and municipality polling districts.
	 */
	List<ConfigurationDto> getPollingDistrictsMissingPollingPlaces(UserData userData, Long municipalityPk);

	List<ConfigurationDto> getPollingDistrictsMissingVoters(UserData userData, Long municipalityPk);

	Boolean hasMunicipalityProxyConfiguredAdvancePollingPlace(UserData userData, Long municipalityPk, boolean advanceVoteInBallotBox);

	/**
	 * Finds the municipality polling district for a municipality
	 * 
	 * @param municipalityPk
	 *            the municipality in which to find the polling district
	 */
	PollingDistrict findMunicipalityProxy(UserData userData, Long municipalityPk);

	/**
	 * Finds polling districts for municipality
	 * 
	 * @param userData
	 * @param municipalityPk
	 * @return list of polling districts
	 */
	List<PollingDistrict> findPollingDistrictsByMunicipalityPk(UserData userData, Long municipalityPk);

	/**
	 * Updates the properties of a parent polling district
	 * 
	 * @param userData
	 * @param pollingDistrict
	 * @return The updated polling district
	 */
	PollingDistrict updateParentPollingDistrict(UserData userData, PollingDistrict pollingDistrict);

	/**
	 * Creates a parent polling district and connects the children to it
	 * 
	 * @param userData
	 * @param pollingDistrict
	 * @param childrenPollingDistricts
	 * @return The created polling district
	 */
	PollingDistrict createParentPollingDistrict(UserData userData, PollingDistrict pollingDistrict, List<PollingDistrict> childrenPollingDistricts);

	boolean isPollingDistrictFirstTechnicalPollingDistrict(UserData userData, PollingDistrict pollingDistrict);

	List<PollingDistrict> findNotTechnicalPollingDistrictsByMunicipalityPk(UserData userData, Long municipalityPk);

	boolean isPollingDistrictFirstOrdinaryPollingDistrict(UserData userData, PollingDistrict pollingDistrict);

	PollingDistrict findUniquePollingDistrictByElectionEvent(UserData userData, Long electionEventPk, String countryId, String countyId, String municipalityId,
			String boroughId, String pollingDistrictId);

	List<ConfigurationDto> getPollingDistrictsWithoutVoters(UserData userData, Long electionEventPk);

	List<PollingDistrict> findPollingDistrictsForBorough(UserData userData, Borough borough);
}
