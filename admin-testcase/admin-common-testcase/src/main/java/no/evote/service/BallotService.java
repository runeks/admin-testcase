package no.evote.service;

import java.io.Serializable;
import java.util.List;

import no.evote.model.Affiliation;
import no.evote.model.Ballot;
import no.evote.model.BallotStatus;
import no.evote.model.Contest;
import no.evote.model.Locale;
import no.evote.security.UserData;

public interface BallotService extends Serializable {

	/**
	 * Finds row in ballot table for the given contestPk and ballotId.
	 * @param userData
	 *            user data
	 * @param pk
	 *            primary key for contest
	 * @param id
	 *            id for ballot, party.getId
	 * @return ballot
	 */
	Ballot findByContestAndId(UserData userData, Long pk, String id);

	void updateBallotStatus(UserData userData, Affiliation affiliation, BallotStatus ballotStatus);

	List<Ballot> findByContest(UserData userData, Long pk);

	List<Ballot> findApprovedByContest(UserData userData, Long pk);

	Ballot createNewBallot(UserData userData, Contest contest, String id, Locale locale, int ballotStatus);

	Ballot create(UserData userData, Ballot ballot);

	Ballot update(UserData userData, Ballot ballot);

	void delete(UserData userData, Long ballotPk);

	Ballot findByPk(UserData userData, Long pk);

	void updateBallotStatusPending(UserData userData, Ballot ballot);

	void updateBallotStatusWithdrawn(UserData userData, Ballot ballot);

	BallotStatus findBallotStatusById(UserData userData, int id);

	String getBallotLocationNr(UserData userData, Contest contest, Long affiliationPk);

	Long findPkByContestAndId(UserData userData, Long contestPk, String ballotId);
}
