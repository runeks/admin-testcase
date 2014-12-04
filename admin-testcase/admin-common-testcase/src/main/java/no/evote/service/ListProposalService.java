package no.evote.service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import no.evote.dto.ListProposalValidationData;
import no.evote.model.Affiliation;
import no.evote.model.Candidate;
import no.evote.model.MvArea;
import no.evote.model.Proposer;
import no.evote.security.UserData;

public interface ListProposalService extends Serializable {
	ListProposalValidationData validateListProposalAndCheckAgainstRoll(UserData userData, Affiliation affiliation, List<Candidate> candidateList,
			List<Proposer> proposerList, String electionEventId, Set<MvArea> candidateMvAreaRestrictions, Set<MvArea> proposerMvAreaRestrictions);

	void removeElectoralRollData(UserData userData, List<Candidate> candidateList, List<Proposer> proposerList, Long ballotPk);

	ListProposalValidationData isProposalPersonSizeBetweenInterval(UserData userData, ListProposalValidationData validationData);
}
