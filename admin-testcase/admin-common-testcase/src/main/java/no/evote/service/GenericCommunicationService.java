package no.evote.service;

import java.util.List;

import no.evote.model.GenericCommunication;
import no.evote.model.Municipality;
import no.evote.security.UserData;

public interface GenericCommunicationService {
	List<GenericCommunication> findByMunicipalityAndElectionEvent(UserData userData, final List<Municipality> municipalityList, final Long electionEventPk);

	GenericCommunication create(UserData userData, GenericCommunication genericCommunication);

	GenericCommunication update(UserData userData, GenericCommunication genericCommunication);

	GenericCommunication findByPk(final Long pk);

}
