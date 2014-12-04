package no.evote.constants;

public enum ElectionLevelEnum {
	NONE(-1), ELECTION_EVENT(0), ELECTION_GROUP(1), ELECTION(2), CONTEST(3);

	private final int level;

	ElectionLevelEnum(final int level) {
		this.level = level;
	}

	public static ElectionLevelEnum getLevel(final int level) {
		if (level == ELECTION_EVENT.level) {
			return ELECTION_EVENT;
		}
		if (level == ELECTION_GROUP.level) {
			return ELECTION_GROUP;
		}
		if (level == ELECTION.level) {
			return ELECTION;
		}
		if (level == CONTEST.level) {
			return CONTEST;
		}

		return NONE;
	}

	public int getLevel() {
		return level;
	}

	public String getLevelDescription() {
		return name().replace('_', ' ').toLowerCase();
	}
}
