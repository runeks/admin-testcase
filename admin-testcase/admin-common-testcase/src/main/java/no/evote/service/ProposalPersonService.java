package no.evote.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import no.evote.model.ElectionEvent;
import no.evote.model.MaritalStatus;
import no.evote.model.MvArea;
import no.evote.model.ProposalPerson;
import no.evote.model.Voter;
import no.evote.security.UserData;

public interface ProposalPersonService extends Serializable {

	ProposalPerson create(UserData userData, ProposalPerson proposalPerson, Long ballotPk);

	ProposalPerson update(UserData userData, ProposalPerson proposalPerson);

	ProposalPerson setMockIdForEmptyId(UserData userData, ProposalPerson proposalPerson, Long ballotPk, Map<String, ProposalPerson> existingIds);

	List<Voter> searchVoter(UserData userData, ProposalPerson proposalPerson, String electionEventId, Set<MvArea> mvAreas);

	Voter convertProposalPersonToNewVoter(ProposalPerson proposalPerson, ElectionEvent electionEvent);

	ProposalPerson convertVoterToProposalPerson(ProposalPerson proposalPerson, Voter voter);

	ProposalPerson validate(UserData userData, ProposalPerson proposalPerson, Long ballotPk);

	void deleteAndReorder(UserData userData, ProposalPerson proposalPerson, Long ballotPk);

	MaritalStatus findMaritalStatusByPk(UserData userData, long pk);
}
