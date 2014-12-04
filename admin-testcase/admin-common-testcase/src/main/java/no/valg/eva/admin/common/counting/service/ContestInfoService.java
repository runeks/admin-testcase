package no.valg.eva.admin.common.counting.service;

import java.io.Serializable;
import java.util.List;

import no.evote.constants.AreaLevelEnum;
import no.valg.eva.admin.common.AreaPath;
import no.valg.eva.admin.common.ElectionPath;
import no.valg.eva.admin.common.counting.model.ContestInfo;

/**
 * Interface for retrieving info about contests.
 */
public interface ContestInfoService extends Serializable {
	ContestInfo findContestInfoByPath(ElectionPath contestPath);

	ContestInfo findContestInfoByElectionAndArea(ElectionPath electionPath, AreaPath areaPath);

	ElectionPath findContestPathByElectionAndArea(ElectionPath electionPath, AreaPath areaPath);

	boolean hasContestsForElectionAndArea(ElectionPath electionPath, AreaPath areaPath);

	List<ContestInfo> contestOrElectionByAreaPath(AreaPath areaPath);

	List<ContestInfo> contestsByAreaPath(AreaPath areaPath, AreaLevelEnum areaLevelFilter);
}
