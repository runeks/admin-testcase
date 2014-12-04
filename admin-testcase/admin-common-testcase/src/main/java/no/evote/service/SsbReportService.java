package no.evote.service;

import java.util.List;
import java.util.Set;

import no.evote.model.Contest;
import no.evote.model.MvArea;
import no.evote.model.SsbReport;
import no.evote.security.UserData;

public interface SsbReportService {

	List<SsbReport> findBySsbEntityCode(UserData userData, Contest contest, MvArea mvArea, String formId, boolean sametingsvalg);

	SsbReport update(UserData userData, SsbReport ssbReport);

	Set<String> getCountQualifiersReadyForReporting(Long contestPk, String mvAreaPath);

	List<SsbReport> findSsbReportForPollingDistrictByMunicipality(UserData userData, Contest contest, MvArea mvArea);
}
