package no.evote.service;

import java.util.Date;
import java.util.List;

import no.evote.model.OpeningHours;
import no.evote.model.PollingPlace;
import no.evote.security.UserData;

public interface OpeningHoursService {

	List<OpeningHours> findOpeningHoursForPollingPlace(UserData userData, PollingPlace pollingPlace);

	OpeningHours create(UserData userData, OpeningHours openingHours);

	OpeningHours update(UserData userData, OpeningHours openingHours);

	void delete(UserData userData, Long pk);

	OpeningHours findByPk(UserData userData, Long pk);

	Boolean hasChanged(UserData userData, Long primKey, Date startTime, Date endTime);

	Boolean noElectionDayReferenceExists(final UserData userData, final Long electionDayPk);

	List<PollingPlace> findPollingPlacesWithoutOpeningHours(final UserData userData, final Long municipalityPk);
}
