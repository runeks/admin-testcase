package no.evote.service;

import java.util.List;

import no.evote.dto.PollingStationDivisionDto;
import no.evote.model.PollingPlace;
import no.evote.model.PollingStation;
import no.evote.security.UserData;

public interface PollingStationService {

	List<PollingStationDivisionDto> getPollingStationDivision(UserData userData, List<PollingStationDivisionDto> divisionList, PollingPlace pollingPlace);

	List<PollingStationDivisionDto> getPollingStationDivision(UserData userData, Integer numberOfPollingStations, PollingPlace pollingPlace);

	List<PollingStationDivisionDto> getDivisionListForPollingPlace(UserData userData, PollingPlace pollingPlace);

	void savePollingStationConfiguration(UserData userData, PollingPlace pollingPlace, List<PollingStationDivisionDto> divisionList);

	List<PollingStation> findByPollingPlace(UserData userdata, PollingPlace pollingPlace);
}
