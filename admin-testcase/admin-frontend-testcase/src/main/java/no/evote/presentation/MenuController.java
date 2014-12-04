package no.evote.presentation;

import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import no.evote.constants.SecObjAccess;
import no.evote.model.ElectionType;
import no.evote.model.MvElection;
import no.evote.security.UserData;
import no.evote.service.ElectionService;
import no.evote.service.MvElectionService;
import no.valg.eva.admin.common.configuration.service.ConfigurationService;
import no.valg.eva.admin.common.counting.model.CountCategory;

/**
 * Utility for conditionals in the UI menus.
 */
@Named
@SessionScoped
public class MenuController extends BaseController {

	private static final long serialVersionUID = -4122944411716810867L;

	@Inject
	private UserData userData;
	@Inject
	private AccessCacheWrapper accessCache;

	private List<CountCategory> validVoteCountCategories = null;

	public boolean hasElectionsWithElectionType(final String electionTypeId) {
//		MvElection mvElection = userData.getOperatorRole().getMvElection();
//
//		if (mvElection.getElectionLevel() > 2) {
//			return mvElection.getElection().getElectionType().getId().equals(electionTypeId);
//		}
//		ElectionType electionType = electionService.findElectionTypeById(electionTypeId);
//		return mvElectionService.hasElectionsWithElectionType(mvElection, electionType);
		return true;
	}

	public boolean isValidCountCategoryId(final String countCategory) {
//		if (validVoteCountCategories == null) {
//			String[] countSecObj = { SecObjAccess.COUNT_ADVANCE_VOTES_READ, SecObjAccess.COUNT_ELECTION_DAY_VOTES_READ};
//			if (accessCache.hasAccess(countSecObj)) {
//				validVoteCountCategories = configurationService.getActualVoteCountCategories(userData);
//			} else {
//				return false;
//			}
//		}
//		return validVoteCountCategories.contains(CountCategory.fromId(countCategory));
		return true;
	}

	public boolean canConfigurePollingStations() {
//		if (userData.getOperatorRole().getMvArea().getMunicipality() != null && userData.getOperatorRole().getMvArea().getMunicipality().isElectronicMarkoffs()) {
//			return false;
//		}
		return true;
	}

	/**
	 * Checks if electronic voting is configured for the user's current operator role.
	 * 
	 * @return true if electronic voting is configured, else false
	 */
	public boolean isEvoteConfigured() {
//		return userData.getOperatorRole().isEvoteConfigured();
		return false;
	}

	/**
	 * Checks if electronic mark offs are configured for the user's current operator role.
	 * 
	 * @return true if electronic mark offs are configured, else false
	 */
	public boolean isElectronicMarkoffsConfigured() {
//		return userData.getOperatorRole().isElectronicMarkoffsConfigured();
		return true;
	}

	/*
	 * Only to be used for mocking purpose
	 */
	public void setUserData(final UserData userData) {
		this.userData = userData;
	}
}
