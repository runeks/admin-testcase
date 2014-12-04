package no.valg.eva.admin.common;

import static java.lang.String.format;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import no.evote.constants.ElectionLevelEnum;

/**
 * Value object for election path: EEVENT.EG.EL.CONTES where EEVENT is election event, EG country, EL election and CONTES contest.
 */
public class ElectionPath implements Serializable {
	public static final String ELECTION_PATH_REGEX = "^(\\d{6})(\\.(\\d{2})(\\.(\\d{2})(\\.(\\d{6}))?)?)?$";
	private static final Pattern ELECTION_PATH_PATTERN = Pattern.compile(ELECTION_PATH_REGEX);
	private final String path;
	private final ElectionLevelEnum level;
	private final String electionEventId;
	private final String electionGroupId;
	private final String electionId;
	private final String contestId;

	public ElectionPath(final String path) {
		this.path = path;

		Matcher pathMatcher = ELECTION_PATH_PATTERN.matcher(path);
		if (!pathMatcher.matches()) {
			throw new IllegalArgumentException(format("illegal path <%s>, must match <%s>", path, ELECTION_PATH_REGEX));
		}

		// regex groups
		int electionEvent = 1;
		// CSOFF: MagicNumber
		int electionGroup = 3;
		int election = 5;
		int contest = 7;
		// CSON: MagicNumber

		electionEventId = pathMatcher.group(electionEvent);
		ElectionLevelEnum level = ElectionLevelEnum.ELECTION_EVENT;
		electionGroupId = pathMatcher.group(electionGroup);
		if (electionGroupId != null) {
			level = ElectionLevelEnum.ELECTION_GROUP;
		}
		electionId = pathMatcher.group(election);
		if (electionId != null) {
			level = ElectionLevelEnum.ELECTION;
		}
		contestId = pathMatcher.group(contest);
		if (contestId != null) {
			level = ElectionLevelEnum.CONTEST;
		}
		this.level = level;
	}

	public static ElectionPath from(final String contestPath) {
		return new ElectionPath(contestPath);
	}

	public String path() {
		return path;
	}

	public ElectionLevelEnum getLevel() {
		return level;
	}

	public String getElectionEventId() {
		return electionEventId;
	}

	public String getElectionGroupId() {
		return electionGroupId;
	}

	public String getElectionId() {
		return electionId;
	}

	public String getContestId() {
		return contestId;
	}

	/**
	 * @return true if this contains subPath, that is this is a parent of subPath
	 */
	public boolean contains(final ElectionPath subPath) {
		return subPath.path.startsWith(path);
	}

	/**
	 * @return true if this is part of parentPath
	 */
	public boolean isSubpathOf(final ElectionPath parentPath) {
		return path.startsWith(parentPath.path);
	}

	public ElectionPath toElectionGroupPath() {
		if (electionGroupId == null) {
			throw new IllegalStateException(format("path to election group can not be found for <%s>", path));
		}
		return new ElectionPath(electionEventId + "." + electionGroupId);
	}

	public ElectionPath add(String id) {
		return ElectionPath.from(path() + "." + id);
	}

    public ElectionPath toElectionPath() {
        if (electionId == null) {
            throw new IllegalStateException(format("path to election can not be found for <%s>", path));
        }
        return new ElectionPath(electionEventId + '.' + electionGroupId + '.' + electionId);
    }

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
        }
		if (!(o instanceof ElectionPath)) {
			return false;
        }
		ElectionPath areaPath = (ElectionPath) o;
		return path.equals(areaPath.path);
	}

	@Override
	public int hashCode() {
		return path.hashCode();
	}

	public String toString() {
		return path;
	}
}
