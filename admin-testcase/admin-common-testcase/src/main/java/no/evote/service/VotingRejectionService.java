package no.evote.service;

import java.util.List;

import no.evote.model.Voting;
import no.evote.model.VotingRejection;
import no.evote.security.UserData;
import no.evote.service.cache.Cacheable;

public interface VotingRejectionService {

	List<VotingRejection> findByEarlyAndElectronicVoting(UserData userData, final Voting voting);

	@Cacheable
	VotingRejection findByPk(UserData userData, Long pk);

	/**
	 * Returns the VotingRejection object that corresponds to the id parameter
	 * @param userData
	 * @param id
	 * @return VotingRejection object that corresponds to the id parameter if id is unique, else returns null
	 */
	VotingRejection findById(UserData userData, String id);

}
