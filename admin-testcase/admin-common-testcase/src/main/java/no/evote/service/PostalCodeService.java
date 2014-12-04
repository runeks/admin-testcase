package no.evote.service;

import java.util.List;

import no.evote.model.PostalCode;

public interface PostalCodeService {

	PostalCode findByIdAndElectionEvent(String postalCodeId, Long electionEventPk);

	List<PostalCode> findPostalCodeByElectionEvent(Long electionEventPk);

}
