package no.evote.service;

import java.util.List;
import java.util.Set;

import no.evote.dto.ListProposalValidationData;
import no.evote.model.Affiliation;
import no.evote.model.Candidate;
import no.evote.model.MvArea;
import no.evote.model.views.CandidateAudit;
import no.evote.security.UserData;

public interface CandidateService extends ProposalPersonService {
	void deleteProposalPerson(UserData userData, Long proposalPersonPk);

	Candidate findByPk(UserData userData, Long candidatePk);

	Candidate createNewCandidate(UserData userData, Affiliation affiliation);

	List<Candidate> convertRowsToCandidateList(UserData userData, List<String[]> rowCandidates, Affiliation affiliation, int maxVotes, MvArea mvArea);

	List<Candidate> findByAffiliation(UserData userData, Long affiliationPk);

	Candidate findCandidateByName(String name);

	List<Candidate> findByIdInOtherBallot(UserData userData, String proposalId, Long ballotPk, Long electionPk);

	List<Candidate> findByIdInAnotherElection(UserData userData, String proposalId, Long ballotPk, Long electionGroupPk);

	Candidate findByBallotAndOrder(UserData userData, Long ballotPk, int displayOrder);

	Candidate findByBallotAndId(UserData userData, Long bpk, String id);

	List<Candidate> swapDisplayOrder(UserData userData, Candidate candidateOver, Candidate candidateUnder);

	List<Candidate> changeDisplayOrder(UserData userData, Candidate candidate, int fromPosition, int toPosition);

	List<CandidateAudit> getCandidateAuditByBallot(UserData userData, Long bPk);

	List<Candidate> mergeAllCandidates(UserData userData, List<Candidate> candidateList);

	ListProposalValidationData isCandidatesValid(UserData userData, List<Candidate> importedCandidates, Long ballotPk, int maximumBaselineVotes);

	void createAllBelow(UserData userData, List<Candidate> importedCandidates, Long affiliationPk, Long ballotPk);

	void deleteAll(UserData userData, List<Candidate> candidateList);

	ListProposalValidationData approveCandidates(UserData userData, ListProposalValidationData approved, String electionEventId, Set<MvArea> mvAreas,
			Affiliation affiliation);

	Long findPkByBallotAndOrder(UserData userData, Long ballotPk, int displayOrder);
}
