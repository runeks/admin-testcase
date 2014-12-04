package no.evote.service;

import java.util.List;

import no.evote.dto.ReportingUnitTypeDto;
import no.evote.model.MvElection;
import no.evote.model.MvElectionReportingUnits;
import no.evote.model.ReportingUnitType;
import no.evote.security.UserData;

public interface ReportingUnitTypeService {

	List<ReportingUnitTypeDto> populateReportingUnitTypeDto(final String electionEventId);

	List<MvElectionReportingUnits> findMvElectionReportingUnitByElectionAndType(final Long reportingUnitTypePk, final Long mvElectionPk);
	
	List<ReportingUnitType> findAll(final UserData userData);

	void updateMvElectionReportingUnits(UserData userData, List<MvElection> mvElectionList, long reportingUnitTypePk);

}
