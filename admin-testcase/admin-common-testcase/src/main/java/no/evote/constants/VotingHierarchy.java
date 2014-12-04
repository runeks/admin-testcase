package no.evote.constants;


public enum VotingHierarchy {
	NONE(0), AREA_HIERARCHY(1), ELECTION_HIERARCHY(2), ELECTORAL_ROLL(3), VOTING(4);

	private int level = 0;

	private VotingHierarchy(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public static VotingHierarchy getVotingHierarchy(boolean copyAreas, boolean copyElections, boolean copyElectoralRoll, boolean copyVotings) {
		if (copyVotings) {
			return VOTING;
		} else if (copyElectoralRoll) {
			return ELECTORAL_ROLL;
		} else if (copyElections) {
			return ELECTION_HIERARCHY;
		} else if (copyAreas) {
			return AREA_HIERARCHY;
		}
		return NONE;
	}
}
