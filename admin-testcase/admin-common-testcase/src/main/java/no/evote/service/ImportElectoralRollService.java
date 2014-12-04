package no.evote.service;

import no.evote.model.ElectionEvent;
import no.evote.security.UserData;

public interface ImportElectoralRollService {
	void initialImportElectoralRoll(UserData userData, final ElectionEvent electionEvent, final String filePath);

	void scheduledImportElectoralRoll(UserData userData, final ElectionEvent electionEvent);

	boolean doesFileExist(UserData userData, final String filePath);

	boolean isFileInitialBatchFile(UserData userData, final String filePath);

}
