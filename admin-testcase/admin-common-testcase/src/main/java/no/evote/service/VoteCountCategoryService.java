package no.evote.service;

import java.io.Serializable;
import java.util.List;

import no.evote.model.VoteCountCategory;
import no.evote.security.UserData;
import no.evote.service.cache.Cacheable;
import no.valg.eva.admin.common.counting.model.CountCategory;

public interface VoteCountCategoryService extends Serializable {

	@Cacheable
	VoteCountCategory findByPk(UserData userData, Long pk);

	@Cacheable
	VoteCountCategory findById(UserData userData, String id);

	@Cacheable
	List<VoteCountCategory> findAll(UserData userData, CountCategory... excludedCategories);

	List<VoteCountCategory> findByContest(UserData userData, Long cpk);

	List<VoteCountCategory> findByMunicipality(UserData userData, Long municipalityPk, Long electionGroupPk);

	List<VoteCountCategory> findNotElectronicByContest(UserData userData, Long cpk);

	List<VoteCountCategory> findNotElectronicByMunicipality(UserData userData, Long municipalityPk, Long electionGroupPk);

}
