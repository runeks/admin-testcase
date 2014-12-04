package no.evote.service;

import java.io.Serializable;
import java.util.List;

import no.evote.model.ElectionGroup;
import no.evote.model.Municipality;
import no.evote.model.ReportCountCategory;
import no.evote.security.UserData;

public interface ReportCountCategoryService extends Serializable {
	List<ReportCountCategory> findReportCountCategories(UserData userData, Municipality municipality, ElectionGroup electionGroup);

	List<ReportCountCategory> findAllReportCountCategoriesForElectionEvent(UserData userData, Long electionEventPk, final boolean detachFromSession);

	ReportCountCategory findByMunAndEGAndVCC(UserData userData, Long mPk, Long egPk, Long vccPk);

	ReportCountCategory create(UserData userData, ReportCountCategory reportCountCategory);

	ReportCountCategory update(UserData userData, ReportCountCategory reportCountCategory);

	void delete(UserData userData, Long pk);

	List<ReportCountCategory> findByContest(UserData userData, Long cpk);

	List<ReportCountCategory> findByMunicipalityAndElectionGroup(UserData userData, Long municipalityPk, Long pk);

	/**
	 * Deletes categories for the given municipality and election group and inserts list of new ones. Existing categories are deleted before saving, therefore
	 * the primary key of the report count category instance is set to null.
	 * 
	 * @param userData
	 *            userData
	 * @param municipality
	 *            municipality instance
	 * @param electionGroup
	 *            election group instance
	 * @param categories
	 *            list of report count categories to be inserted
	 */
	void replaceCategories(UserData userData, Municipality municipality, ElectionGroup electionGroup, List<ReportCountCategory> categories);
}
