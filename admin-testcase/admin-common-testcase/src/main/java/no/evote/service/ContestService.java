package no.evote.service;

import java.io.Serializable;
import java.util.List;

import no.evote.model.Contest;
import no.evote.model.ContestStatus;
import no.evote.model.Election;
import no.evote.model.MvArea;
import no.evote.security.UserData;

public interface ContestService extends Serializable {

	Contest findContestById(UserData userData, Long electionPk, String id);

	Contest create(UserData userData, Contest contest, MvArea associatedArea);

	Contest update(UserData userData, Contest contest);

	void delete(UserData userData, Long pk);

	Contest findByPk(UserData userData, Long pk);

	void updateConfig(UserData userData, Contest contest);

	int getNumberOfFinishedListProposals(UserData userData, Long pk);

	int getNumberOfListProposals(UserData userData, Long pk);

	int getNumberOfRejectedListProposals(UserData userData, Long pk);

	ContestStatus getStatus(UserData userData, Long pk);

	void sendToApproval(final UserData userData, final Long pk);

	void approve(UserData userData, Long contestPk);

	void reject(UserData userData, Long contestPk);

	void createContestsForElection(UserData userData, Election election);

	List<Contest> findByElectionPk(UserData userData, Long electionPk);

	Contest findContest(UserData userData, String contestID, String electionID, String electionGroupID, long electionEventPk);

	List<Contest> getContestsByStatus(UserData userData, Long electionEventPk, Integer contestStatusId);

}
