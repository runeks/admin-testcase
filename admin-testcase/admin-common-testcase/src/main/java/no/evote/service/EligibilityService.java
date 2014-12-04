package no.evote.service;

import java.util.List;

import no.evote.model.Voter;
import no.evote.model.views.Eligibility;
import no.evote.security.UserData;

public interface EligibilityService {

	List<Eligibility> findEligibilityForVoterInEvent(UserData userData, final Voter voter);

	List<Eligibility> findEligibilityForVoterInGroup(UserData userData, final Voter voter, final Long electionGroupPk);

	List<Eligibility> findTheoreticalEligibilityForVoterInGroup(UserData userData, final Voter voter, final Long electionGroupPk);

	boolean hasEligibilityToAllContestsInGroup(UserData userData, final Voter voter, final Long electionGroupPk);
}
