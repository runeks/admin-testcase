package no.evote.constants;

public final class SecObjTask {
	public static final String GENERIC_COMMUNICATION = "t.generic_communication";
	public static final String GENERIC_COMMUNICATION_APPROVAL = "t.generic_communication.approval";
	public static final String CONTEST_LOCAL_CONFIG = "t.contest.local_config";
	public static final String CONTEST_APPROVAL = "t.contest.approval";
	public static final String ELECTION_EVENT_LOCAL_CONFIG = "t.election_event.local_config";
	public static final String ELECTION_EVENT_APPROVAL = "t.election_event.approval";
	public static final String ELECTION_APPROVAL = "t.election.approval";
	public static final String ELECTION_LOCAL_CONFIG = "t.election.local_config";
	public static final String MUNICIPALITY_APPROVAL = "t.municipality.approval";
	public static final String MUNICIPALITY_LOCAL_CONFIG = "t.municipality.local_config";
	public static final String VOTE_COUNT_APPROVAL = "t.vote_count.approval";
	public static final String BALLOT_APPROVAL = "t.ballot.approval";
	public static final String BALLOT_PROPOSAL = "t.ballot.proposal";

	private SecObjTask() {
		throw new AssertionError();
	}
}
