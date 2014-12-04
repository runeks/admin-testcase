package no.evote.service;

import java.util.Date;
import java.util.List;

import no.evote.model.Contest;
import no.evote.model.ElectionGroup;
import no.evote.model.Municipality;
import no.evote.model.MvArea;
import no.evote.model.MvElection;
import no.evote.model.Party;
import no.evote.model.ReportingUnit;
import no.evote.model.Voter;
import no.evote.security.UserData;

/**
 * Generates various reports that are not included in the reporting module.
 */
public interface ReportService {

	byte[] generateElectionCard(UserData userData, Long vpk, MvArea mvArea, MvElection mvElection);

	byte[] generateElectoralRoll(UserData userData, MvArea mvArea, Integer nrrows, boolean checkbox, ElectionGroup electionGroup);

	byte[] generateSticker(UserData userData, Integer num, MvElection mvElection, MvArea mvArea, String vcId, Integer type, ReportingUnit reportingUnit);

	byte[] generateBallot(UserData userData, List<String> candidateList, int affiliationVoteCount, String partyName, Contest contest, Date electionDay,
			String ballotLocationNr, int candidateVoteCountForOtherParties);

	byte[] generateRejectedVotings(UserData userData, String mid, Long egpk);

	byte[] generateEmptyElectionCard(UserData userData, Voter voter, MvElection mvElection, MvArea mvArea, String pollingDistrictId, String pollingPlaceName);

	byte[] generateBallot(UserData userData, List<List<String>> candidateList, List<String> partyNameList, List<Integer> affiliationVoteCountList,
			Contest contest, String electionName, Date electionDay);

	/**
	 * Generates a report over changes in electoral roll for a municipality
	 */
	byte[] getHistoryForMunicipality(UserData userData, Municipality municipality, char endringstype, Date startDate, Date endDate, Long electionEventPk,
			String selectedSearchMode, Boolean searchOnlyApproved);

	byte[] generateProposerTemplate(UserData userData, Party party, Contest contest, List<String> proposerList);

	byte[] generateAdvanceVotingPickList(UserData userData, Long pollingPlacePk, Municipality municipality, Long electionGroupPk, Date startDate, Date endDate,
			int votingNumberStart, int votingNumberEnd);

	/**
	 * Generates report for pick list for election day votes.  Includes late validated advance votes if flag includeLateValidatedAdvanceVotes is true.
	 * This is done by using another report picklist_election_day_advance.  If flag is false, the report picklist_election_day is generated.
	 * 
	 *
	 * @param userData userData
	 * @param municipality kommune
	 * @param electionGroupPk primary key of election group
	 * @param start start of løpenummer
	 * @param end end of løpenummer
	 * @return array of bytes representing pdf file
	 */
	byte[] generateElectionDayVotePickList(UserData userData, Municipality municipality, Long electionGroupPk, int start, int end);
}
