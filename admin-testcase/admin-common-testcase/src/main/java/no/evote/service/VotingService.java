package no.evote.service;

import java.util.Date;
import java.util.List;

import no.evote.dto.PickListItem;
import no.evote.dto.VotingDto;
import no.evote.model.Contest;
import no.evote.model.ElectionGroup;
import no.evote.model.MvArea;
import no.evote.model.MvElection;
import no.evote.model.PollingPlace;
import no.evote.model.Voter;
import no.evote.model.Voting;
import no.evote.model.VotingCategory;
import no.evote.model.views.ElectionDayVotings;
import no.evote.model.views.ForeignEarlyVoting;
import no.evote.security.UserData;
import no.evote.service.cache.Cacheable;
import org.apache.commons.lang3.tuple.Pair;

public interface VotingService {

	List<Voting> getApprovedVotingsForVoterByElectionGroup(UserData userData, Long voterPk, final Long electionGroupPk);

	void requestRemoveAllVotingsForVoter(UserData userData, final Long voterPk, final Long electionGroupPk, final String requestRemovalReason);

	List<Voting> getVotingsByElectionGroupAndVoter(UserData userData, Long voterPk, Long electionPk);

	List<Voting> getVotingsByElectionGroupVoterAndMunicipality(UserData userData, Long voterPk, Long electionPk, Long municpalityPk);

	List<Voting> getReceivedVotingsByElectionGroupAndVoter(UserData userData, Long voterPk, Long electionPk);

	List<Voting> getVotingsReadyForRemovalByPollingPlace(UserData userData, final Long pollingPlacePk, final Long electionGroupPk);

	List<Voting> getRejectedVotingsByElectionGroupAndMunicipality(UserData userData, final String municipalityId, final Long electionGroupPk);

	@Deprecated
	Voting create(UserData userData, Voting voting);

	@Deprecated
	Voting update(UserData userData, Voting voting);

	@Deprecated
	void delete(final UserData userData, Long pk);

	@Deprecated
	Voting findByPk(UserData userData, Long pk);

	/**
	 * Returns a list of Voting Data transfer objects which each represent statistics of a voting category. The parameters is used to filter what data is used.
	 * 
	 * @param userData
	 * @param pollingPlacePk
	 * @param municipalityPk
	 * @param electionGroupPk
	 * @param startDate
	 * @param endDate
	 * @param votingNumberStart
	 * @param votingNumberEnd
	 * @param includeLateValidation
	 * @param votingCategories
	 * @param includeLateAdvanceVotings
	 *            flag telling whether to in addition also include advance votings where late_validation is true
	 * @return
	 */
	List<VotingDto> findVotingStatistics(final UserData userData, final Long pollingPlacePk, final long municipalityPk, final Long electionGroupPk,
			final Date startDate, final Date endDate, final int votingNumberStart, final int votingNumberEnd, final boolean includeLateValidation,
			final String[] votingCategories, final boolean includeLateAdvanceVotings);

	/**
	 * Get votings that must be manually processed in a negative approval process. The method does not return votings with late validation. The the parameters
	 * is used to filter what data is used.
	 * 
	 * @param userData
	 * @param pollingPlacePk
	 * @param municipalityPk
	 * @param electionGroupPk
	 * @param startDate
	 * @param endDate
	 * @param votingNumberStart
	 * @param votingNumberEnd
	 * @return
	 */
	List<PickListItem> findAdvanceVotingPickList(final UserData userData, final Long pollingPlacePk, final long municipalityPk, final Long electionGroupPk,
			final Date startDate, final Date endDate, final int votingNumberStart, final int votingNumberEnd);

	/**
	 * Finds election day votes for negative approval.
	 * 
	 * 
	 * 
	 * @param userData
	 * @param municipalityPk
	 *            municipality pk
	 * @param electionGroupPk
	 *            election group pk
	 * @param votingNumberStart
	 *            start of search interval
	 * @param votingNumberEnd
	 *            end of search interval
	 * @param votingCats
	 * @return list of PickListDto
	 */
	List<PickListItem> findElectionDayVotingPickList(final UserData userData, final long municipalityPk, final Long electionGroupPk,
			final int votingNumberStart, final int votingNumberEnd, String... votingCats);

	/**
	 * Approves every advance vote with the filter criteria, this is used to approve votings in a negative approval process. The method does not approve votings
	 * with late validation. The the parameters is used to filter what data is used.
	 * 
	 * @param userData
	 * @param pollingPlacePk
	 * @param municipalityPk
	 * @param electionGroupPk
	 * @param startDate
	 * @param endDate
	 * @param votingNumberStart
	 * @param votingNumberEnd
	 * @return
	 */
	int updateAdvanceVotingsApproved(final UserData userData, final Long pollingPlacePk, final long municipalityPk, final Long electionGroupPk,
			final Date startDate, final Date endDate, final int votingNumberStart, final int votingNumberEnd);

	/**
	 * Approves election day votes
	 * 
	 * @param userData
	 * @param municipalityPk
	 * @param electionGroupPk
	 * @param votingNumberStart
	 * @param votingNumberEnd
	 * @param votingCats
	 * @return
	 */
	int updateElectionDayVotingsApproved(final UserData userData, final long municipalityPk, final Long electionGroupPk, final int votingNumberStart,
			final int votingNumberEnd, String... votingCats);

	Voting findVotingByVotingNumber(final UserData userData, final long municipalityPk, final Long electionGroupPk, final long votingNumber,
			final boolean earlyVoting);

	Long countElectoralRollMarkOffsByCategories(UserData userData, Long pdpk, String[] categories, boolean late, boolean electronicMarkoffs);

	Long countElectoralRollMarkOffsByCategoriesMunicipality(UserData userData, Long mpk, String[] categories, boolean late, boolean electronicMarkoffs);


	@Cacheable
	VotingCategory findVotingCategoryById(UserData userData, String id);

	@Cacheable
	List<VotingCategory> findAdvanceVotingCategories(UserData userData);

	List<ForeignEarlyVoting> findForeignEarlyVotingsSentFromMunicipality(UserData userData, Long electionGroupPk, String municipalityId);

	List<VotingCategory> findPaperVotingCategories(UserData userData);

	void deleteVotings(UserData userData, MvElection mvElection, MvArea mvArea, Integer selectedVotingCategoryPk);

	void deleteSeqVotingNumber(UserData userData, MvElection mvElection, MvArea mvArea);

	Voter getVoterFromVoting(UserData userData, Voting voting);

	List<VotingCategory> findAllVotingCategories(UserData userData);

	/**
	 * Marks off advance vote in electoral roll. (Krysser av for forhåndsstemme i manntall.)
	 * @param userData
	 * @param pollingPlace
	 * @param electionGroup
	 * @param voter
	 * @param pollingDistrictPk
	 * @param isVoterInLoggedInMunicipality
	 * @param selectedVotingCategoryId
	 * @param ballotBoxId
	 * @param isVotingLateAdvance
	 * @return Voting
	 */
	Voting markOffVoterAdvance(UserData userData,
							   PollingPlace pollingPlace,
							   ElectionGroup electionGroup,
							   Voter voter,
							   Long pollingDistrictPk,
							   boolean isVoterInLoggedInMunicipality,
							   String selectedVotingCategoryId,
							   String ballotBoxId,
							   boolean isVotingLateAdvance);

	/**
	 * Marks off election day emergency vote in electoral roll. (Krysser av for beredsskapsstemme i manntall.)
	 *
	 * TODO hvordan gir det mening at det "krysses" for beredsskap.  Skal ikke være elektonisk kryss iallfall..  Endre metode navn til "register.." i stedet..?
	 *
	 * @param userData
	 * @param pollingPlace
	 * @param electionGroup
	 * @param voter
	 * @return
	 */
	Voting markOffVoterElectionDayEmergency(UserData userData,
											PollingPlace pollingPlace,
											ElectionGroup electionGroup,
											Voter voter);

	/**
	 * Register vote from central location in municipality.
	 * 
	 * @param userData
	 * @param electionGroup
	 * @param voter
	 * @param selectedVotingCategoryId  voting category (advance, election day..)
	 * @param currentMvArea mvArea selected by user in picker or associated with user's role
	 * @return voting
	 */
	Voting registerVoteCentrally(UserData userData,
								 ElectionGroup electionGroup,
								 Voter voter,
								 String selectedVotingCategoryId,
								 MvArea currentMvArea);

	/**
	 * Marks off vote for election day, also handles special cover..  bør håndteres litt anderledes.
	 * 
	 * @param userData
	 * @param pollingPlace
	 * @param electionGroup
	 * @param voterSearchResult
	 * @param fremmedstemme
	 * @param saerskiltstemme
	 * @param currentMvArea  mvArea selected by user in picker or associated with user's role
	 * @param voterIsFromSameMunicipalityButOtherPollingDistrict
	 * @return voting
	 */
	Voting markOffVoter(UserData userData,
						PollingPlace pollingPlace,
						ElectionGroup electionGroup,
						Voter voterSearchResult,
						boolean fremmedstemme,
						boolean saerskiltstemme,
						MvArea currentMvArea,
						boolean voterIsFromSameMunicipalityButOtherPollingDistrict);

	/**
	 * Marks off advance vote in ballot box, forhåndsstemme i urne.
	 * 
	 * @param userData
	 * @param pollingPlace
	 * @param electionGroup
	 * @param voterSearchResult
	 * @param isVoterInLoggedInMunicipality
	 * @param currentMvArea mvArea selected by user in picker or associated with user's role
	 * @return voting
	 */
	Voting markOffVoterAdvanceVoteInBallotBox(UserData userData,
											  PollingPlace pollingPlace,
											  ElectionGroup electionGroup,
											  Voter voterSearchResult,
											  boolean isVoterInLoggedInMunicipality,
											  MvArea currentMvArea);

	Long countApprovedElectoralRollMarkOffsOnVotingPollingDistrict(UserData userData, Long votingPollingDistrict);

	/**
	 * Get a list of election days and number of markoffs per day for a specific voting category.
	 */
	List<Pair<String, ElectionDayVotings>> getElectionDaysWithMarkoffs(UserData userData, Contest contest, Long pollingDistrictPk, String votingCategoryId,
			boolean electronicMarkoffs);
}
