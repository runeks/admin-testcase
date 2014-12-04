package no.evote.service;

import java.io.Serializable;
import java.util.List;

import no.evote.dto.MvElectionMinimal;
import no.evote.model.Contest;
import no.evote.model.ElectionLevel;
import no.evote.model.ElectionType;
import no.evote.model.MvElection;
import no.evote.security.UserData;
import no.evote.service.cache.Cacheable;
import no.valg.eva.admin.common.ElectionPath;

public interface MvElectionService extends Serializable {

	MvElection findRoot(long electionEvent);

	List<MvElection> findByPathAndChildLevel(MvElection mvElection);

	List<MvElection> findByPathAndLevel(String path, int level);

	/**
	 * @deprecated use MvElectionService#findSingleByPath(ElectionPath) instead
	 */
	@Deprecated
	MvElection findSingleByPath(String path);

	MvElection findSingleByPath(ElectionPath path);

	boolean hasElectionsWithElectionType(MvElection mvElection, ElectionType electionType);

	MvElection findByPk(Long pk);

	Boolean hasAccessToPkOnLevel(MvElection mvElection, long pk, int level);

	MvElectionMinimal findSingleByPathMinimal(UserData userData, String path);

	List<MvElectionMinimal> findByPathAndChildLevelMinimal(UserData userData, Long mvElectionPk, boolean includeContestsAboveMyLevel);

	boolean hasElectionsWithElectionTypeMinimal(MvElectionMinimal mvElection, ElectionType electionTypeFilter);

	MvElection findByContest(Contest contest);

	@Cacheable
	List<ElectionLevel> findAllElectionLevelsUnderLevel(UserData userData, Integer level);

	@Cacheable
	List<ElectionLevel> findAllElectionLevels(UserData userData);

	MvElectionMinimal getMvElectionMinimal(UserData userData, MvElection mvElection);

	MvElection findByPkAndLevel(long pk, int level);

	@Cacheable
	ElectionLevel findElectionLevelByPk(UserData userData, Long pk);

	MvElection findSingleByPkAndLevel(long pk, int level);
}
