package no.evote.service;

import java.util.List;

import no.evote.model.ReportingUnit;
import no.evote.model.Responsibility;
import no.evote.model.ResponsibleOfficer;
import no.evote.security.UserData;

public interface ResponsibleOfficerService {

	List<ResponsibleOfficer> findResponsibleOfficersForReportingUnit(UserData userData, ReportingUnit reportingUnit);

	ResponsibleOfficer update(UserData userData, ResponsibleOfficer responsibleOfficer);

	void delete(UserData userData, Long responsibleOfficerPk);

	ResponsibleOfficer findByPk(UserData userData, Long pk);

	List<Responsibility> findAllResponsibilities(UserData userData);

	/**
	 * Finds new display order before creating responsible office entity
	 *
	 * @param userData
	 * @param newResponsibleOfficer new entity
	 * @return created entity
	 */
	ResponsibleOfficer createAndSetDisplayOrder(UserData userData, ResponsibleOfficer newResponsibleOfficer);
}
