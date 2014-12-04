package no.valg.eva.admin.common.configuration.service;

import java.util.List;

import no.evote.security.UserData;
import no.valg.eva.admin.common.counting.model.CountCategory;

public interface ConfigurationService {
	/**
	 * Find allowed vote count categories for the operator on the level that he has been assigned.
	 *
	 * @return list of IDs for VoteCountCategories
	 */
	List<CountCategory> getActualVoteCountCategories(UserData userData);
}
