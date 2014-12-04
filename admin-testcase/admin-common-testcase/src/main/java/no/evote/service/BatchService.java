package no.evote.service;

import java.util.List;

import no.evote.model.Access;
import no.evote.model.Batch;
import no.evote.model.BatchStatus;
import no.evote.model.ElectionEvent;
import no.evote.security.UserData;

public interface BatchService {

	/**
	 * Returns the status id for a batch job. The meaning of a status id is defined in EvoteConstants
	 */
	int checkStatus(UserData userData, long batchPk);

	byte[] getBatchData(UserData userData, long pk, boolean verifyAccess);

	byte[] getLatestBatchData(UserData userData, String category, boolean verifyAccess);

	/**
	 * Get a list of primary keys for batches associated with a specific election event and access path.
	 */
	long[] listBatches(UserData userData, String category);

	boolean isLatestBatch(UserData userData, Long pk);

	/**
	 * Makes a batch job of a file, if is a count eml zip it gets validated
	 */
	Batch saveFile(UserData userData, byte[] file, String fileName, String category);

	Batch createBatch(UserData userData, Batch batch);

	Batch updateBatch(UserData userData, Batch batch);

	BatchStatus getBatchStatusByID(int id);

	/**
	 * Get batch jobs added by the specified user
	 */
	List<Batch> listMyBatches(UserData userData, Long orpk);

	Batch createBatch(final UserData userData, final String accessString, final Integer runNumber, final String fileName);

	Batch createErrorBatchForImportElectoralRoll(final UserData userData, final String accessString, final Integer runNumber, final String fileName,
			final String message);

	Batch updateBatchSuccessForImportElectoralRoll(final UserData userData, final Batch batch);

	Batch updateBatchWithError(final UserData userData, final Batch batch);

	List<Batch> listBatchesByEventAndAccess(final UserData userData, final String accessString, final String electionEventId);

	/**
	 * Asynchronous import of a batch job
	 */
	void importFile(UserData userData, int id, Long electionEventPk, String accessPath);

	Access getAccess(UserData userData, Long pk);

	/**
	 * Creates a new batch job for generation of voter numbers,
	 * if a generation is already started or finished an EvoteException is thrown
	 * @param userData
	 * @param accessString
	 * @return The created batch
	 */
	Batch createBatchForGenerateVoterNumber(UserData userData, String accessString);

	Batch updateBatchSuccessForGenerateVoterNumber(UserData userData, Batch batch);

	Batch createBatchForDeleteVoters(UserData userData, String accessString, String mvAreaString);

	Batch updateBatchSuccessForDeleteVoters(UserData userData, Batch batch);

	/**
	 * Finds a unique batch
	 */
	Batch findUniqueBatch(UserData userData, int id, Long electionEventPk, String accessPath);

	Batch createBatch(UserData userData, String accessString, BatchStatus batchStatus, String infoText, String fileName);

	/**
	 * Checks if the vote number generation batch has started.
	 * @param userData
	 * @param access
	 * @param electionEvent
	 * @return True if the job has started. Otherwise false.
	 */
	boolean isVoterNumberGenertionBatchStarted(UserData userData, ElectionEvent electionEvent);

	/**
	 * Checks if the vote number generation batch has successfully finished.
	 * @param userData
	 * @param access
	 * @param electionEvent
	 * @return True if the job has finished. Otherwise false.
	 */
	boolean isVoterNumberGenerationBatchSuccessfullyFinished(UserData userData, ElectionEvent electionEvent);

}
