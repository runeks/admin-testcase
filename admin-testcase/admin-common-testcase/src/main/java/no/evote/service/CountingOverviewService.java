package no.evote.service;

import java.io.Serializable;
import java.util.List;

import no.evote.dto.countingoverview.CountingAreaOverviewDto;
import no.evote.security.UserData;
import no.valg.eva.admin.common.AreaPath;
import no.valg.eva.admin.common.ElectionPath;

public interface CountingOverviewService extends Serializable {

	/**
	 * Provides counting overview for Bydelsvalg
	 *
	 * @param userData
	 *            security context object
	 * @param electionPath
	 *            path to election which is parent of all borough contests
	 * @return list of CountingAreaOverviewDto instances
	 */
	List<CountingAreaOverviewDto> getCountingOverviewForBoroughs(UserData userData, ElectionPath electionPath);

	/**
	 * Provides counting overview for kommunevalg and fylkestingsvalg
	 *
	 * @param userData
	 *            security context object
	 * @param contestPath
	 *            path to contest on county or municipality level
	 * @return list of CountingAreaOverviewDto instances
	 */
	List<CountingAreaOverviewDto> getCountingOverviewForMunicipalities(UserData userData, ElectionPath contestPath, AreaPath areaPath);

}
