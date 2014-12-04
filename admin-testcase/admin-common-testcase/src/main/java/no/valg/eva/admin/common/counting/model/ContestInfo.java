package no.valg.eva.admin.common.counting.model;

import java.io.Serializable;

import no.evote.constants.AreaLevelEnum;
import no.valg.eva.admin.common.ElectionPath;

/**
 * Contains key parameters for Contest
 */
public class ContestInfo implements Serializable {
	private final ElectionPath electionPath;
	private final String electionName;
	private final String contestName;
	private final AreaLevelEnum areaLevel;

	public ContestInfo(String electionPath, String electionName, String contestName, Integer areaLevel) {
		this.electionPath = ElectionPath.from(electionPath);
		this.electionName = electionName;
		this.contestName = contestName;
		this.areaLevel = AreaLevelEnum.getLevel(areaLevel);
	}

	public ElectionPath getElectionPath() {
		return electionPath;
	}

	public String getElectionName() {
		return electionName;
	}

	public String getContestName() {
		return contestName;
	}

	public AreaLevelEnum getAreaLevel() {
		return areaLevel;
	}

	@Override
	public String toString() {
		return "ContestInfo{"
				+ "electionPath=" + electionPath
				+ ", electionName='" + electionName + '\''
				+ ", contestName='" + contestName + '\''
				+ ", areaLevel=" + areaLevel
				+ '}';
	}
}
