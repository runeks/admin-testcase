package no.valg.eva.admin.common.counting.model.modifiedballots;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import no.valg.eva.admin.common.counting.model.BatchId;

/**
 * Contains a batch of modified ballots (annotations) for a given ballot.
 */
public class ModifiedBallotBatch implements Serializable {
	private BatchId batchId;
	private List<ModifiedBallot> modifiedBallots;
	private Ballot ballot;

	public ModifiedBallotBatch(BatchId batchId, List<ModifiedBallot> modifiedBallots, Ballot ballot) {
		this.batchId = batchId;
		this.modifiedBallots = modifiedBallots;
		this.ballot = ballot;
	}

	public BatchId getBatchId() {
		return batchId;
	}

	public List<ModifiedBallot> getModifiedBallots() {
		return modifiedBallots;
	}

	public Collection<Candidate> getWriteInCandidates() {
		return ballot.getCandidatesForWriteIn();
	}

	public Collection<Candidate> getPersonalVoteCandidates() {
		return ballot.getPersonalVoteCandidates();
	}

	public void addPersonalVoteCandidateFor(Candidate candidate) {
		ballot.getPersonalVoteCandidates().add(candidate);
	}

	public void addWriteInCandidateFor(Candidate candidate) {
		ballot.getCandidatesForWriteIn().add(candidate);
	}

	public Ballot getBallot() {
		return ballot;
	}

	/**
	 * Intended to be used to figure out if handling of all ballots in a batch has been completed (so that the "Done" button can be enabled)
	 */
	public boolean isDone() {
		for (ModifiedBallot modifiedBallot : modifiedBallots) {
			if (!modifiedBallot.isDone() && !modifiedBallot.isModified()) {
				return false;
			}
		}
		
		return true;
	}
}
