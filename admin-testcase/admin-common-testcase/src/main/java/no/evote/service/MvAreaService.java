package no.evote.service;

import java.io.Serializable;
import java.util.List;

import no.evote.model.AreaLevel;
import no.evote.model.MvArea;
import no.evote.security.UserData;
import no.evote.service.cache.Cacheable;
import no.valg.eva.admin.common.AreaPath;

public interface MvAreaService extends Serializable {

	MvArea findRoot(Long eepk);

	List<MvArea> findByPathAndChildLevel(MvArea mvArea);

	List<MvArea> findByPathAndLevel(String path, int level);

	/**
	 * @deprecated use MvAreaService#findSingleByPath(AreaPath) instead
	 */
	@Deprecated
	MvArea findSingleByPath(String path);

	MvArea findSingleByPath(AreaPath path);

	MvArea findByPk(Long pk);

	Boolean hasAccessToPkOnLevel(MvArea mvArea, long pk, int level);

	Long findByLevelAndId(UserData userData, int i, Long pk);

	MvArea findByPollingDistrict(UserData userData, Long pollingDistrictPk);

	@Cacheable
	AreaLevel findAreaLevelById(Integer id);

	@Cacheable
	List<AreaLevel> findAllAreaLevelsUnderLevel(UserData userData, Integer level);

	@Cacheable
	List<AreaLevel> findAllAreaLevels(UserData userData);

	@Cacheable
	AreaLevel findAreaLevelByPk(UserData userData, Long pk);

	MvArea findByMunicipality(UserData userData, Long municipalityPk);
	
	MvArea findByMunicipalityAndPollingPlaceId(UserData userData, Long municipalityPk, String pollingPlaceId);
}
