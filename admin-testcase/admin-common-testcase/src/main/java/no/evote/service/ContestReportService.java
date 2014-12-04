package no.evote.service;

import java.io.Serializable;
import java.util.List;

import no.evote.model.ContestReport;
import no.evote.model.VoteCount;
import no.evote.security.UserData;

public interface ContestReportService extends Serializable {
	boolean hasContestReport(UserData userData, Long pk);

	ContestReport findByReportingUnitContest(UserData userData, Long rupk, Long cpk);

	List<VoteCount> getVoteCounts(UserData userData, Long crpk);

	ContestReport update(final UserData userData, final ContestReport contestReport);

	ContestReport findByReportingUnitTypeAndContest(UserData userData, int reportingUnitTypeId, Long contestPk);

	ContestReport findByContestAndArea(UserData userData, Long contestPk, Long mvAreaPk);

	List<ContestReport> findByElectionEvent(UserData userData, Long electionEventPK);

	List<ContestReport> createContestReports(UserData userData, List<ContestReport> entities);

	ContestReport createContestReport(UserData userData, ContestReport entity);

	/**
	 * Finds contest report using primary key.
	 * @param userData
	 * @param contestReportPk
	 * @return contest report instance
	 */
	ContestReport findByPk(UserData userData, Long contestReportPk);
}
