package no.evote.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import no.evote.model.BallotCount;
import no.evote.model.BallotRejection;
import no.evote.model.Candidate;
import no.evote.model.CandidateVote;
import no.evote.model.ModifiedBallot;
import no.evote.model.Contest;
import no.evote.model.ContestReport;
import no.evote.model.CountQualifier;
import no.evote.model.ManualContestVoting;
import no.evote.model.Municipality;
import no.evote.model.MvArea;
import no.evote.model.MvElection;
import no.evote.model.PollingDistrict;
import no.evote.model.ReportCountCategory;
import no.evote.model.ReportingUnit;
import no.evote.model.VoteCount;
import no.evote.model.VoteCountCategory;
import no.evote.model.VoteCountStatus;
import no.evote.model.views.ElectionDayVotings;
import no.evote.security.UserData;
import no.evote.service.cache.Cacheable;

public interface CountingService extends Serializable {

	ContestReport makeContestReport(UserData userData, Contest contest, ReportingUnit reportingUnit);

	ReportCountCategory getReportCountCategoryFromMunicipality(UserData userData, Municipality municipality, Contest contest,
			VoteCountCategory voteCountCategory);

	List<BallotCount> populateBallotCounts(UserData userData, VoteCount voteCount, ContestReport contestReport);

	List<BallotCount> populateBallotRejectedCounts(UserData userData, VoteCount voteCount, VoteCountCategory voteCountCategory);

	BallotRejection findBallotRejectionById(UserData userData, String id);

	BallotRejection findBallotRejectionByPk(UserData userData, Long pk);

	List<BallotRejection> findBallotRejectionsByEarlyVoting(UserData userData, Boolean ev);

	VoteCountStatus findVoteCountStatusById(UserData userData, Integer id);

	String generateId(UserData userData, CountQualifier countQualifier, ContestReport contestReport, PollingDistrict pollingDistrict,
			VoteCountCategory voteCountCategory);

	ModifiedBallot updateModifiedBallot(UserData userData, ModifiedBallot modifiedBallot);

	ModifiedBallot createModifiedBallot(UserData userData, ModifiedBallot modifiedBallot);

	void deleteCandidateVote(UserData userData, Long pk);

	void deleteModifiedBallot(UserData userData, Long pk);

	List<CandidateVote> findCandidateVoteByModifiedBallot(UserData userData, Long pk);

	List<ModifiedBallot> findModifiedBallotsByBallotCount(UserData userData, Long pk);

	CandidateVote createCandidateVote(UserData userData, CandidateVote candidateVote);

	ModifiedBallot createCandidatesModifiedBallot(UserData userData, BallotCount ballotCount, List<Candidate> candidateList, Map<Long, Boolean> strikeout,
												  Map<Long, Integer> renumbering);

	ModifiedBallot updateModifiedBallotBallotCount(UserData userData, ModifiedBallot modifiedBallotPk, BallotCount newBallotCount, boolean wasApproved);

	BallotCount findBallotCountByPk(UserData userData, Long pk);

	ModifiedBallot updateCandidatesModifiedBallot(UserData userData, ModifiedBallot modifiedBallot, BallotCount ballotCount, List<Candidate> candidateList,
												  Map<Long, Boolean> strikeout, Map<Long, Integer> renumbering);

	int countPollingDistricts(UserData userData, Long cpk, Long apk, String cat);

	Long getModifiedBallotCountForBallotCount(UserData userData, Long ballotCountPk);

	@Cacheable
	CountQualifier findCountQualifierById(UserData userData, String id);

	BallotCount createBallotCount(UserData userData, BallotCount ballotCount);

	BallotCount updateBallotCount(UserData userData, BallotCount ballotCount);

	void deleteBallotCount(UserData userData, Long pk);

	ReportingUnit findLowerReportingUnit(UserData userData, List<ReportingUnit> reportingUnits, CountQualifier countQualifier, PollingDistrict pollingDistrict,
			VoteCountCategory voteCountCategory);

	void validateCountingStatus(UserData userData, ReportingUnit reportingUnit, Long mvElectionPk, VoteCountCategory voteCountCategory, Contest contest);

	int countChildPollingDistricts(UserData userData, Long pollingDistrictPk);

	int countReadyChildPollingDistricts(UserData userData, Long contestPk, Long pollingDistrictPk);

	int countProtocolPollingDistricts(UserData userData, Long municipalityPk);

	int countReadyProtocolPollingDistricts(UserData userData, Long contestPk, Long municipalityPk);

	List<ManualContestVoting> getManualContestVotingsForPollingDistrictByCategoryAndContest(UserData userData, Long contestPk, Long mvAreaPk,
			Long votingCategoryPk);

	void saveProtocol(UserData userData, ContestReport contestReport, VoteCount voteCountProtocol, BallotCount ballotCountProtocol, boolean electronicMarkoffs,
			List<ElectionDayVotings> contestVotings);

	void toApprovalProtocol(UserData userData, ContestReport contestReport, VoteCount voteCountProtocol, BallotCount ballotCountProtocol,
			boolean electronicMarkoffs, List<ElectionDayVotings> contestVotings);

	void approveProtocol(UserData userData, VoteCount voteCountProtocol, BallotCount ballotCountProtocol);

	void rejectProtocol(UserData userData, VoteCount voteCountProtocol, BallotCount ballotCountProtocol);

	void resetProtocol(UserData userData, VoteCount voteCountProtocol, BallotCount ballotCountProtocol);

	void savePreliminary(UserData userData, ContestReport contestReport, VoteCount voteCountPreliminary, List<BallotCount> ballotCountsPreliminary,
			boolean fixCounts, List<ManualContestVoting> manualContestVotings);

	void toApprovalPreliminary(UserData userData, ContestReport contestReport, VoteCount voteCountPreliminary, List<BallotCount> ballotCountsPreliminary,
			boolean fixCounts, List<ManualContestVoting> manualContestVotings);

	void approvePreliminary(UserData userData, VoteCount voteCount);

	void rejectPreliminary(UserData userData, VoteCount voteCount);

	void resetPreliminary(UserData userData, VoteCount voteCount);

	void saveFinal(UserData userData, ContestReport contestReport, VoteCount voteCount, List<BallotCount> ballotCountsFinal,
			List<BallotCount> ballotRejectedCounts);

	void toApprovalFinal(UserData userData, VoteCount voteCount);

	void approveFinal(UserData userData, ContestReport contestReport, List<VoteCount> voteCounts, int activeCount, List<BallotCount> ballotCountsFinal,
			List<BallotCount> ballotRejectedCounts);

	void rejectFinal(UserData userData, VoteCount voteCount, boolean modifiedBallots);

	void resetFinal(UserData userData, VoteCount voteCount);

	VoteCount toSettlementFinal(UserData userData, VoteCount voteCount);

	void validateSelectedPollingDistrictAndCategory(UserData userData, PollingDistrict pollingDistrict, Municipality municipality,
			VoteCountCategory voteCountCategory, ReportCountCategory reportCountCategory, MvArea reportingUnitMvArea, MvElection mvElection);

	List<BallotCount> findUnapprovedBallotCounts(final UserData userData, final VoteCount voteCount, final ContestReport contestReport);
}
