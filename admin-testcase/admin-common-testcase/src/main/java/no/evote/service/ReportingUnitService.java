package no.evote.service;

import java.io.Serializable;
import java.util.List;

import no.evote.model.Contest;
import no.evote.model.ContestReport;
import no.evote.model.MvArea;
import no.evote.model.MvElection;
import no.evote.model.ReportingUnit;
import no.evote.model.views.ContestRelArea;
import no.evote.security.UserData;
import no.valg.eva.admin.common.AreaPath;
import no.valg.eva.admin.common.counting.constants.ReportingUnitTypeId;

public interface ReportingUnitService extends Serializable {

	ReportingUnit findByMvElectionMvArea(MvElection mvElection, MvArea mvArea);

	List<ReportingUnit> findClosestReportingUnitForElection(UserData userData, MvElection mvElection, MvArea mvArea);

	List<ContestReport> getContestReports(UserData userData, Long rupk);

	ReportingUnit update(UserData userData, ReportingUnit reportingUnit);

	ReportingUnit findByPk(Long pk);

	List<ReportingUnit> findByExactAreaAndByElectionDownLevels(UserData userData, MvElection mvElection, MvArea mvArea);

	ReportingUnit findByExactAreaAndByElectionUpLevels(MvElection mvElection, MvArea mvArea);

	ReportingUnit findLowerReportingUnitByAreaAndLevel(UserData userData, MvElection mvElectionContest, MvArea mvArea, int wantedAreaLevel, MvArea wantedArea);

	List<ReportingUnit> findStemmestyrerByAreaDownLevelAndByElectionDownLevels(UserData userData, MvElection mvElection, MvArea mvArea);

	List<ReportingUnit> findByAreaDownLevelAndByElectionDownLevels(UserData userData, MvElection mvElection, MvArea mvArea);

	/**
	 * Checks if one or more reporting units have been created for the municipality
	 * 
	 * @param municipalityMvAreaPk
	 *            Pk for the MvArea representing the municipality
	 * @return true if one or more reporting units false otherwise
	 */
	List<ReportingUnit> getMunicipalityReportingUnits(UserData userData, Long municipalityMvAreaPk);

	/**
	 * Retrieves the count electoral board reporting unit (opptellingsvalgstyret) that is conected to the contest
	 * 
	 * @return electoral board reporting unit
	 */
	ReportingUnit findCountElectoralBoardByContest(UserData userData, Contest contest);

	List<ReportingUnit> findAllForElectionEvent(UserData userData, long electopnEventPk);

	List<ReportingUnit> create(UserData userData, List<ReportingUnit> reportingUnits);

	/**
	 * Finds a reporting unit for contest on a specified area
	 */
	ReportingUnit getReportingUnit(UserData userData, ContestRelArea contestRelArea);

	/**
	 * Returns a list of all ReportingUnits accessible by the operatorRole
	 */
	List<ReportingUnit> getAccessibleReportingUnits(UserData userData, MvElection mvElection, MvArea mvArea);

	ReportingUnit findByAreaPathAndType(AreaPath areaPath, ReportingUnitTypeId typeId);
}
