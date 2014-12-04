package no.evote.service;

import java.io.Serializable;
import java.util.List;

import no.evote.model.Affiliation;
import no.evote.model.Contest;
import no.evote.model.Locale;
import no.evote.model.Operator;
import no.evote.model.Party;
import no.evote.security.UserData;

public interface AffiliationService extends Serializable {
	/**
	 * Creates new affiliation.
	 */
	Affiliation create(UserData userData, Affiliation affiliation);

	Affiliation createNewAffiliation(UserData userData, Contest contest, Party party, Locale locale, Operator operator, int ballotStatus);

	Affiliation getAffiliationById(UserData userData, String value, Long contestPk);

	List<Affiliation> findByContest(UserData userData, Long pk);

	List<Affiliation> findApprovedByContest(UserData userData, Long pk);

	List<Affiliation> findApprovedByElectionEvent(UserData userData, String electionEventId);

	List<Affiliation> findByBallotStatusAndContest(UserData userData, Long pk, int ballotStatus);

	Affiliation findByBallot(Long ballotPk);

	Affiliation createNewPartyAndAffiliation(UserData userData, Contest currentContest, Party newParty, Locale locale);

	boolean hasAffiliationPartyId(UserData userData, String partyId, Long electionEventPk);

	Affiliation update(UserData userData, Affiliation affiliation);

	Affiliation findByPk(UserData userData, Long pk);

	Affiliation saveColumns(UserData userData, Affiliation currentAffiliation);

	List<Affiliation> swapDisplayOrder(UserData userData, Affiliation affiliatonOver, Affiliation affiliationUnder);

	void delete(UserData userData, Long pk);

	boolean hasMailRecipients(UserData userData, Affiliation affiliation);

	String[] getMailRecipients(UserData userData, Affiliation affiliation);

	String generateFeed(UserData userData, String electionEventId, String electionGroupId, String electionId, String contestId);

	List<Affiliation> changeDisplayOrder(UserData userData, Affiliation affiliation, int fromPosition, int toPosition);
}
