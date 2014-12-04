package no.valg.eva.admin.common.counting.model.modifiedballots;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import no.valg.eva.admin.common.counting.model.BatchId;

/**
 * Annotates a given Ballot instance with personal votes (personstemmer), write ins (slengere), strike outs (strykninger) and renumberings.
 */
public class ModifiedBallot implements Serializable {
	
	private BatchId batchId;
	private int serialNumber;
	private String affiliation;
	private boolean done;

	private BallotId ballotId;
	private Set<Candidate> personalVotes = new TreeSet<>();
	private Set<Candidate> writeIns = new LinkedHashSet<>();

	/**
	 * @param serialNumber position in batch
	 * @param affiliation party id
	 * @param ballotId ref to ballot this annotates
	 */
	public ModifiedBallot(BatchId batchId, int serialNumber, String affiliation, BallotId ballotId, boolean done) {
		this.batchId = batchId;
		this.serialNumber = serialNumber;
		this.affiliation = affiliation;
		this.ballotId = ballotId;
		this.done = done;
	}

	public ModifiedBallot(int serialNumber, String partyName) {
		this.serialNumber = serialNumber;
		this.affiliation = partyName;
	}

	public int getSerialNumber() {
		return serialNumber;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public void validate(Ballot ballot) {
		if (!ballot.getBallotId().equals(ballotId)) {
			throw new IllegalArgumentException("Must validate against ballot with same ballotId");
		}
		if (!ballot.getPersonalVoteCandidates().containsAll(personalVotes)) {
			throw new IllegalStateException("Personal vote candidates not on ballot!");
		}
		if (!ballot.getCandidatesForWriteIn().containsAll(writeIns)) {
			throw new IllegalStateException("Write in candidates not on ballot!");
		}
		if (writeIns.size() > ballot.getMaxWriteIns()) {
			throw new IllegalStateException("There is a maximum of " + ballot.getMaxWriteIns()
				+ " write-ins for this ballot. Currently " + writeIns.size() + " write-ins are added");
		}
	}
	
	public void addPersonalVoteFor(Candidate candidate) {
		personalVotes.add(candidate);
	}

	public void addWriteInFor(Candidate candidate) {
		writeIns.add(candidate);
	}

	public BatchId getBatchId() {
		return batchId;
	}

	public Set<Candidate> getWriteIns() {
		return writeIns;
	}

	public Set<Candidate> getPersonalVotes() {
		return personalVotes;
	}

	public void setPersonalVotes(Set<Candidate> personalVotes) {
		this.personalVotes = personalVotes;
	}

	public BallotId getBallotId() {
		return ballotId;
	}

	public void setWriteIns(Set<Candidate> writeIns) {
		this.writeIns = writeIns;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public boolean isDone() {
		return done;
	}

	public boolean isModified() {
		return personalVotes.size() > 0 || writeIns.size() > 0;
	}
}
