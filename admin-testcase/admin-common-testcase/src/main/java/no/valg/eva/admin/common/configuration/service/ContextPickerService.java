package no.valg.eva.admin.common.configuration.service;

import java.io.Serializable;

import no.evote.security.UserData;
import no.valg.eva.admin.common.AreaPath;
import no.valg.eva.admin.common.ElectionPath;
import no.valg.eva.admin.common.configuration.filter.BoroughFilterFactory;
import no.valg.eva.admin.common.configuration.filter.ElectionFilterFactory;
import no.valg.eva.admin.common.configuration.filter.PollingDistrictFilterFactory;
import no.valg.eva.admin.common.configuration.model.AreaPicker;
import no.valg.eva.admin.common.configuration.model.CountCategoryPicker;
import no.valg.eva.admin.common.configuration.model.ElectionPicker;

public interface ContextPickerService extends Serializable {
	// Valghierarki

	// ElectionPicker buildElectionPickerForElectionEvents(...); -- kan lages senere ved behov

	ElectionPicker buildElectionPickerForElectionGroups(UserData userData, ElectionPath electionEventPath);

	ElectionPicker buildElectionPickerForElections(UserData userData, ElectionPath electionGroupPath, ElectionFilterFactory electionFilterFactory);

	ElectionPicker buildElectionPickerForContests(UserData userData, ElectionPath electionPath);

	// Områdehierarki

	// AreaPicker buildAreaPickerForElectionEvents(...); -- kan lages senere ved behov

	// AreaPicker buildAreaPickerForCountries(...); -- kan lages senere ved behov

	AreaPicker buildAreaPickerForCounties(UserData userData, ElectionPath electionPath, AreaPath countryPath);

	AreaPicker buildAreaPickerForMunicipalities(UserData userData, AreaPath countyPath);

	AreaPicker buildAreaPickerForBoroughs(UserData userData, AreaPath municipalityPath, BoroughFilterFactory boroughFilterFactory);

	AreaPicker buildAreaPickerForPollingDistricts(UserData userData, AreaPath boroughPath, PollingDistrictFilterFactory boroughFilter);

	// AreaPicker buildAreaPickerForPollingPlaces(...); -- kan lages senere ved behov

	// ** støttefunksjoner

	boolean isElectionOnBoroughLevel(ElectionPath electionPath);

	CountCategoryPicker buildCountCategoryPicker(UserData userData, ElectionPath electionPath);
}
