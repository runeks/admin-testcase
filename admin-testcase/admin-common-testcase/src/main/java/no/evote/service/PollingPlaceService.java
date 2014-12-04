package no.evote.service;

import java.util.List;

import no.evote.model.MvArea;
import no.evote.model.PollingPlace;
import no.evote.model.views.PollingPlaceVoting;
import no.evote.security.UserData;

public interface PollingPlaceService {

	PollingPlace create(UserData userData, PollingPlace pollingPlace);

	PollingPlace update(UserData userData, PollingPlace pollingPlace);

	void delete(UserData userData, Long pollingPlacePk);

	PollingPlace findByPk(Long pk);

	PollingPlace findPollingPlaceById(UserData userData, Long pollingDistrictPk, String id);

	PollingPlace findPollingPlaceByElectionDayVoting(final UserData userData, final Long pollingDistrictPk);

	List<PollingPlaceVoting> findAdvancedPollingPlaceByMunicipality(UserData userData, final Long electionEventPk, final String municipalityId);

	Boolean evaluateHaveAllPollingPlacesConfiguredPollingStations(UserData userData, MvArea parentMvArea);

	List<PollingPlace> findPollingPlacesWithoutPollingStations(UserData userData, long parentMvAreaPk);

	List<Long> evaluateWhatMvAreasToShowInOverview(UserData userData, List<Long> mvAreaPks);

	List<PollingPlace> findAdvanceInBallotBoxPollingPlaceByPollingDistrict(UserData userData, Long pollingDistrictPk);

	PollingPlace findFirstPollingPlace(UserData userData, long pollingDistrictPk);

	List<PollingPlace> findNotAdvanceInBallotBoxPollingPlaceByPollingDistrict(UserData userData, Long pollingDistrictPk);

	List<PollingPlace> findPollingPlacesWithoutGPSCoordinates(UserData userData, Long parentMvAreaPk);

	List<PollingPlace> findByPollingDistrict(UserData userData, Long pollingDistrictPk);
}
