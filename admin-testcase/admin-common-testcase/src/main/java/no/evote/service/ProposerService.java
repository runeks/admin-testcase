package no.evote.service;

import java.util.List;
import java.util.Set;

import no.evote.dto.ListProposalValidationData;
import no.evote.model.Affiliation;
import no.evote.model.Ballot;
import no.evote.model.MvArea;
import no.evote.model.Proposer;
import no.evote.model.ProposerRole;
import no.evote.security.UserData;
import no.evote.service.cache.Cacheable;

public interface ProposerService extends ProposalPersonService {

	List<Proposer> findByBallot(UserData userData, Long ballotPk);

	Proposer createNewProposer(Ballot ballot);

	List<Proposer> updateAll(UserData userData, List<Proposer> proposerList);

	void createDefaultProposers(UserData userData, Ballot ballot);

	int countByBallot(UserData userData, Long ballotPk);

	Proposer findByBallotAndId(UserData userData, Long ballotPk, String id);

	Proposer findByBallotAndOrder(UserData userData, Long ballotPk, int displayOrder);

	ListProposalValidationData isOcrProposersDataSet(UserData userData, ListProposalValidationData approved);

	ListProposalValidationData approveProposers(UserData userData, ListProposalValidationData approved, String electionEventId, Set<MvArea> mvAreas,
			Affiliation affiliation);

	void deleteProposalPerson(UserData userData, Long pk);

	Proposer findByPk(UserData userData, Long pk);

	@Cacheable
	List<ProposerRole> findSelectiveProposerRoles();

	@Cacheable
	ProposerRole findProposerRoleByPk(UserData userData, Long proposerRolePk);

	@Cacheable
	ProposerRole findProposerRoleById(String proposerRoleId);

	Proposer create(UserData userData, Proposer toProposer);

	List<Proposer> changeDisplayOrder(UserData userData, Proposer proposer, int fromPosition, int toPosition);

}
