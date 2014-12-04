package no.valg.eva.admin.common.counting.service;

import java.io.Serializable;
import java.util.List;

import no.evote.security.UserData;
import no.valg.eva.admin.common.counting.model.BallotCount;
import no.valg.eva.admin.common.counting.model.BallotCountRef;
import no.valg.eva.admin.common.counting.model.BatchId;
import no.valg.eva.admin.common.counting.model.modifiedballots.ModifiedBallotBatch;

/**
 * Operations related to batch handling of modified ballots
 */
public interface ModifiedBallotBatchService extends Serializable {

	/**
	 * @return The identifier of the newly created modified ballot batch
	 */
	ModifiedBallotBatch createModifiedBallotBatch(UserData userData, BallotCount ballotCount, int noOfModifiedBallotsInBatch);

	ModifiedBallotBatch findActiveBatchByBatchId(UserData userData, BatchId modifiedBallotBatchId);

	boolean hasModifiedBallotBatchForBallotCountPks(List<BallotCountRef> ballotCountPks);
}
