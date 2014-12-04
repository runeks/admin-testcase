package no.evote.service;

import no.evote.model.VoterImportBatch;
import no.evote.security.UserData;

public interface VoterImportBatchService {

	VoterImportBatch findSingelByElectionEvent(final Long electionEventPk);

	VoterImportBatch update(UserData userData, VoterImportBatch voterImportBatch);

}
