package no.evote.service;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import no.evote.dto.CandidateVoteCountDto;
import no.evote.model.AffiliationVoteCount;
import no.evote.model.BallotRejection;
import no.evote.model.CandidateSeat;
import no.evote.model.MvArea;
import no.evote.model.MvElection;
import no.evote.model.ReferendumOptVoteCount;
import no.evote.model.Settlement;
import no.evote.model.VoteCountCategory;
import no.evote.security.UserData;

public interface SettlementService extends Serializable {

	List<BigInteger> countPollingDistrictsByStatus(UserData userData, Long cpk, int voteCountStatus);

	List<BigInteger> countPollingDistricts(UserData userData, Long cpk);

	List<List<Object[]>> collatedCountResults(UserData userData, Long cpk, List<VoteCountCategory> voteCountCategories, Long municipalityPk);

	List<BallotRejection> findAllBallotRejections(UserData userData);

	Settlement findSettlementByContest(UserData userData, Long contestPk);

	List<AffiliationVoteCount> findAffiliationVoteCountsBySettlement(UserData userData, Long settlementPk);

	Map<Long, List<CandidateVoteCountDto>> findCandidateVoteCountsBySettlement(UserData userData, Long settlementPk);

	List<CandidateSeat> findAffiliationCandidateSeatsBySettlement(UserData userData, Long settlementPk);

	Settlement makeSettlement(UserData userData, Long contestPk);

	Settlement findSettlementByPk(UserData userData, long settlementPk);

	List<Integer> findMandatesBySettlement(UserData userData, Long pk);

	List<ReferendumOptVoteCount> findReferendumOptVoteCountsBySettlement(UserData userData, Long pk);

	List<String> getPollingDistrictsByCategoryAndStatus(UserData userData, Long cpk, String voteCountCategory, int voteCountStatus);

	List<String> getPollingDistrictsNotReadyByCategory(UserData userData, Long contestPk, Long reportingUnitPk, String voteCountCategory);

	void deleteSettlement(UserData userData, MvElection mvElection, MvArea mvArea);

	boolean distributeLevelingSeats(UserData userData, long electionPk);

	boolean hasDistributedLevelingSeats(UserData userData, long electionPk);

	void deleteLevelingSeatSettlement(UserData userData, long electionPk);

	boolean areAllSettlementsInElectionFinished(UserData userData, long electionPk);
}
