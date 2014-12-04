package no.evote.service;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

import no.evote.model.MvArea;
import no.evote.model.MvElection;
import no.evote.model.views.ContestRelArea;
import no.evote.security.UserData;

public interface ContestRelAreaService extends Serializable {

	ContestRelArea findUnique(UserData userData, Long mvElectionPk, Long mvAreaPk);

	List<ContestRelArea> findAllAllowed(UserData userData, MvElection mvElection, MvArea mvArea);

	List<LinkedHashMap<String, Object>> findPollingDistrictBelow(UserData userData, MvElection mvElection, MvArea mvArea, String vccID);

	int countPollingDistrictBelow(UserData userData, Long mvElectionPk, Long mvAreaPk, String vccID);

	int countCountsOnPollingDistrict(UserData userData, Long mvElectionPk, Long mvAreaPk, String vccID, Long pollingDistrictPk);

}
