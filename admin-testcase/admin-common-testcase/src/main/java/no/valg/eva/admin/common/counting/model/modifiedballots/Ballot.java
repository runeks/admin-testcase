package no.valg.eva.admin.common.counting.model.modifiedballots;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Model for the statical content of the mofided ballot process. ModifiedBallot will be an annotation on a Ballot.
 */
public class Ballot implements Serializable {

	private final BallotId ballotId;
	private final int maxWriteIns;
	
	private Set<Candidate> candidatesForPersonalVotes = new TreeSet<>();
	private Set<Candidate> candidatesForWriteIn = new LinkedHashSet<>();

	public Ballot(BallotId ballotId, int maxWriteIns) {
		this.ballotId = ballotId;
		this.maxWriteIns = maxWriteIns;
	}

	public Set<Candidate> getPersonalVoteCandidates() {
		return candidatesForPersonalVotes;
	}

	public Set<Candidate> getCandidatesForWriteIn() {
		return candidatesForWriteIn;
	}

	public BallotId getBallotId() {
		return ballotId;
	}

	public int getMaxWriteIns() {
		return maxWriteIns;
	}
}
