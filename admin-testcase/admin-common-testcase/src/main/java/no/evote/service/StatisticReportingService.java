package no.evote.service;

import java.util.List;

import no.evote.dto.SsbFile;
import no.evote.model.Contest;
import no.evote.model.ElectionGroup;
import no.evote.model.MvArea;
import no.evote.model.SsbReport;
import no.evote.model.views.ContestRelArea;
import no.evote.security.UserData;

public interface StatisticReportingService {

	/**
	 * Creates zip of party data for each contest in election
	 *
	 * @param userData
	 * @param selectedContest contest user operates on.  Used for finding all contests in election.
	 * @param areaLevelPath
	 * @param electionPathLevel2
	 * @return bytes of zip file
	 */
	byte[] exportElectionDataFg02(UserData userData, Contest selectedContest, String areaLevelPath, String electionPathLevel2);

	/**
	 * Creates zip of party data and elected candidates for each contest in election
	 * @param userData
	 * @param selectedContest contest user operates on.  Used for finding all contests in election.
	 * @param areaLevelPath
	 * @param electionPathLevel2
	 * @return bytes of zip file
	 */
	byte[] exportElectionDataSfv02(UserData userData, Contest selectedContest, String areaLevelPath, String electionPathLevel2);

	/**
	 * Exports xml file containing "Valgresultater, kommune".
	 * @param userData
	 * @param contestRelArea
	 * @param operatorContest
	 * @param ssbReport
	 * @param electionGroup
	 */
	void exportMunicipalityCountDataSkv01(UserData userData, ContestRelArea contestRelArea, Contest operatorContest, SsbReport ssbReport, ElectionGroup electionGroup);

	/**
	 * Exports xml file containing "Korrigerte valgresultater, fylke"
	 * @param userData
	 * @param contestRelArea
	 * @param county
	 * @param ssbReport
	 * @param operatorContest
	 */
	void exportCountyCountDataSfv01(UserData userData, ContestRelArea contestRelArea, MvArea county, SsbReport ssbReport, Contest operatorContest);

	/**
	 * Lists report files sent to SSB
	 *
	 * @param userData
	 * @param countyId
	 *@param municipalityId
	 * @param pollingDistrictId null if no polling district selected
	 * @param sametingsvalg    @return
	 */
	List<SsbFile> listFilesForMunicipality(UserData userData, String countyId, String municipalityId, String pollingDistrictId, boolean sametingsvalg);

	/**
	 * Exports municipality count data for municipalities with less than 30 voters in electoral roll.
	 * @param userData
	 * @param contestRelArea
	 * @param operatorContest
	 * @param operatorArea samekrets (fylke), county
	 * @param selectedSsbReport
	 */
	void exportSametingMunicipalityCountDataSsv01(UserData userData, ContestRelArea contestRelArea, Contest operatorContest, MvArea operatorArea,
                                                  SsbReport selectedSsbReport);
}
