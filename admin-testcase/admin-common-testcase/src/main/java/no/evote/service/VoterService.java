package no.evote.service;

import java.util.List;

import no.evote.model.Aarsakskode;
import no.evote.model.IncomingStatus;
import no.evote.model.MvArea;
import no.evote.model.MvElection;
import no.evote.model.PollingDistrict;
import no.evote.model.SpesRegType;
import no.evote.model.Statuskode;
import no.evote.model.Voter;
import no.evote.security.UserData;
import no.evote.service.cache.Cacheable;

public interface VoterService {

	List<Voter> getVoterById(UserData userData, String id, Long electionEventPk);

	boolean isIdInElectoralRoll(UserData userData, String id, Long electionEventPk);

	List<Voter> searchVoter(UserData userData, Voter voter, String countyId, String municipalityId, Integer maxResultsize, boolean approved,
			Long electionEventPk);

	List<Voter> findVotersNotApprovedByMunicipality(UserData userData, String municipalityId, Long electionEventPk);

	List<Voter> getVoterByVoterNumber(UserData userData, String municipalityId, Integer number, Long electionEventPk);

    List<Voter> findVoterByElectionEvent(UserData userData, Long electionEventPk);

	Voter create(UserData userData, Voter voter);

	void updateVoterList(UserData userData, List<Voter> voters);

	boolean areVotersInElectionEvent(UserData userData, Long electionEventPk);

	List<Aarsakskode> findAllAarsakskoder();

	List<SpesRegType> findAllSpesRegTypes();

	List<Statuskode> findAllStatuskoder();

	boolean hasVoters(UserData userData, PollingDistrict pd);

	@Cacheable
	IncomingStatus findIncomingStatusById(UserData userData, int id);

	void deleteVoters(UserData userData, MvElection mvElection, MvArea mvArea);

	void deleteVotersWithoutMvArea(UserData userData, Long electionEventPk);

	void prepareNewInitialLoad(UserData userData, MvElection mvElection, MvArea mvArea);

	Voter createFictitiousVoter(UserData userData, MvArea mvArea);

	Voter updateWithManualData(UserData userData, Voter voter);

	Voter updateWithSkdData(UserData userData, Voter voter);

	List<String> getVotersWithoutPollingDistricts(UserData userData, Long electionEventPk);
}
