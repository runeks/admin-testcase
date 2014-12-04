package no.evote.service;

import java.util.List;

import no.evote.model.ElectionGroup;
import no.evote.model.ElectionVoteCountCategory;
import no.evote.security.UserData;
import no.valg.eva.admin.common.counting.model.CountCategory;

/**
 * Defines services for storing and retrieving ElectionVoteCountCategory.
 */
public interface ElectionVoteCountCategoryService {

	/**
	 * Finds ElectionVoteCountCategory based on election group.
	 * 
	 * @param userData
	 *            userData
	 * @param electionGroup
	 *            electionGroup
	 * @param detachFromSession
	 * @return list of ElectionVoteCountCategory for election group, empty list if none is found.
	 */
	List<ElectionVoteCountCategory> findElectionVoteCountCategories(UserData userData, ElectionGroup electionGroup, final boolean detachFromSession);

	/**
	 * @return election vote count categories for election group, optionally without excluded categories
	 */
	List<ElectionVoteCountCategory> findElectionVoteCountCategories(UserData userData, ElectionGroup electionGroup, CountCategory... excludedCategories);

	/**
	 * Updates instances in database.
	 * @param userData
	 *            userData.
	 * @param categories
	 *            instances to update
	 */
	void update(UserData userData, List<ElectionVoteCountCategory> categories);
}
