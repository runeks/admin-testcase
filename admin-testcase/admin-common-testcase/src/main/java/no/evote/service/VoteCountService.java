package no.evote.service;

import java.io.Serializable;
import java.util.List;

import no.evote.model.BallotCount;
import no.evote.model.CandidateVote;
import no.evote.model.ModifiedBallot;
import no.evote.model.MvArea;
import no.evote.model.MvElection;
import no.evote.model.VoteCount;
import no.evote.security.UserData;

public interface VoteCountService extends Serializable {

	Long countUniqueKind(Long cqpk, Long crpk, Long pdpk, Long vccpk);

	Long countUniqueKindWithStatus(Long cqpk, Long crpk, Long pdpk, Long vccpk, int statusId);

	List<VoteCount> findListByContestReportPollingDistrictCountQualifierAndCategory(UserData userData, Long contestPk, Long pollingDistrictPk,
			Long countQualifierPk, Long voteCountCategoryPk);

	VoteCount findByCoRePdVoCoQuCoCaApproved(UserData userData, Long crpk, Long pdpk, Long cqpk, Long vccpk);

	List<BallotCount> getBallotCounts(UserData userData, Long vcpk);

	VoteCount create(UserData userData, VoteCount voteCount);

	VoteCount findByPk(UserData userData, Long pk);

	VoteCount updateVoteCount(UserData userData, VoteCount voteCount);

	VoteCount saveVoteCountAndManualRejected(UserData userData, VoteCount voteCount, List<BallotCount> ballotRejections, List<BallotCount> ballotsFinal);

	List<VoteCount> findByMunicipalityAndContest(UserData userData, Long contestPk, Long municipalityPk, String voteCountCategoryId, Long reportingUnitPk,
			Boolean ignoreManualCount);

	int countPollingDistricts(UserData userData, Long contestPk, Long municipalityPk, String countCategoryId);

	int countReadyForApproveRejection(UserData userData, Long contestPk, Long municipalityPk, String countCategoryId, Long reportingUninitPk);

	List<VoteCount> setAllToSettlement(UserData userData, List<VoteCount> voteCountFinal);

	void deleteVoteCounts(UserData userData, MvElection mvElection, MvArea mvArea, Integer selectedVoteCountCategoryPk, Integer selectedReportingUnitTypeId);

	VoteCount findSingleByContestPollingDistrictCountQualifierAndCategory(UserData userData, Long contestPk, Long pollingDistrictPk, Long countQualifierPk,
			Long voteCountCategoryPk);

	void delete(UserData userData, Long pk);

	List<VoteCount> findByContestCountQualifierAreaLevel(UserData userData, Long contestPk, Long countQualifierPk, int areaLevel);

	List<VoteCount> findByContestCountQualifierMvArea(UserData userData, Long contestPk, Long countQualifierPk, long mvAreaPk);

	/**
	 * Set that rejected ballots have been processed for all supplied vote counts.
	 * 
	 * @param voteCountFinal
	 *            List of vote counts to be updated
	 * @return Updated vote counts
	 */
	List<VoteCount> ballotsProcessed(UserData userData, List<VoteCount> voteCountFinal);

	int sumTechnicalVotingsForBorough(UserData userData, Long contestReportPk, Long votCountCategoryPk, Long countQualifierPk, Long pollingDistrictPk);

	List<VoteCount> findListByContestPollingDistrictCountQualifierAndCategory(UserData userData, Long contestPk, Long pollingDistrictPk, Long countQualifierPk,
			Long voteCountCategoryPk);

	List<VoteCount> createVoteCounts(UserData userData, List<VoteCount> entities);

	List<BallotCount> createBallotCounts(UserData userData, List<BallotCount> entities);

	List<ModifiedBallot> createModifiedBallots(UserData userData, List<ModifiedBallot> entities);

	List<CandidateVote> createCandidateVotes(UserData userData, List<CandidateVote> entities);

	VoteCount createVoteCount(UserData userData, VoteCount entity);

	BallotCount createBallotCount(UserData userData, BallotCount entity);

	ModifiedBallot createModifiedBallots(UserData userData, ModifiedBallot entity);
}
